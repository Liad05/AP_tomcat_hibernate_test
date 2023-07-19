package model;

import test.ClientHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class newServer {
    static newServer  instance;
    ServerSocket server;
    int port;
    ClientHandler ch;
    boolean stop;
    static ArrayList<Socket> clients;
    int counter;
    ExecutorService threadPool;

    public newServer(int port, ClientHandler ch) {
        this.port = port;
        this.ch = ch;
        this.threadPool = Executors.newFixedThreadPool(4);
        counter=0;
        clients=new ArrayList<>();
    }
    public static newServer get()
    {
        if (instance == null) {
            instance = new newServer(9999, new PlayerHandler());
            instance.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public void start() {
        stop = false;

        new Thread(this::startsServer).start();

    }

    private void clientMethod(Socket client) {//creates new clienthandler for every client, closes socket and stream when finished.
        try {
            Class<? extends ClientHandler> chClass = this.ch.getClass();
            ClientHandler chNew = chClass.getDeclaredConstructor().newInstance();

            OutputStream outputStream = client.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            // first step: send the client his id

            writer.println(counter); // Send the ID to the client
            System.out.println("Client number "+counter+" connected");
            counter++;
            chNew.handleClient(client.getInputStream(), client.getOutputStream());
            // get the exisiting client's id
            chNew.close();
            client.close();
            //System.out.println("Client number "+exitId+" disconnected");
            //counter--;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startsServer() {//takes care of every client in a different thread with threadpool
        try {
            server = new ServerSocket(port);
            //server.setSoTimeout(1000);//waiting restarts every second
            while (!stop) {
                try {
                    Socket client = server.accept();
                    clients.add(client);
                    // client added now
                    // im assuming that the client wont exit mid game

                    threadPool.execute(()->clientMethod(client));
                    // client removed now
                    //System.out.println("number of threads: "+Thread.activeCount());



                } catch (SocketTimeoutException e) {
                    System.out.println("Problem with client");
                }
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        stop = true;
        threadPool.shutdown();
        counter=0;
        clients=new ArrayList<>();
    }
    public void sendMessagesToAllClients(String msg){
        for (Socket client:clients) {
            try {
                OutputStream outputStream = client.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                writer.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        newServer server = new newServer(9999, new PlayerHandler());
        server.start();
    }
}

