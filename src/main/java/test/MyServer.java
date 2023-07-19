package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
    int port;
    ClientHandler ch;
    int numOfThreads;
    boolean stop;

    ExecutorService threadPool;

    MyServer(int port, ClientHandler ch, int numOfThreads) {
        this.port = port;
        this.numOfThreads = numOfThreads;
        this.ch = ch;
        this.threadPool = Executors.newFixedThreadPool(numOfThreads);
    }

    public void start() {
        stop = false;
        new Thread(this::startsServer).start();
    }

    private void clientMethod(Socket client) {//creates new clienthandler for every client, closes socket and stream when finished.
    	try {
        	Class<? extends ClientHandler> chClass = this.ch.getClass();
        	ClientHandler chNew = chClass.getDeclaredConstructor().newInstance();
        	chNew.handleClient(client.getInputStream(), client.getOutputStream());
        	chNew.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startsServer() {//takes care of every client in a different thread with threadpool
        try {
            ServerSocket server = new ServerSocket(port);
            server.setSoTimeout(1000);//waiting restarts every second
            while (!stop) {
                try {
                    Socket client = server.accept();
                    threadPool.execute(()->clientMethod(client));
                } catch (SocketTimeoutException e) {
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
    }
    

}