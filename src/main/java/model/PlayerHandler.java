package model;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;


//import org.json.simple.JSONObject;
import test.ClientHandler;
import test.GameManager;
import test.Tile;
import test.Word;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerHandler implements ClientHandler{
    //private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private boolean exit=false;
    private int playerId;
    private static GameManager gm;
    private newServer server;
    private static AtomicInteger connectedClients = new AtomicInteger(0);
    private static AtomicInteger turn = new AtomicInteger(0);
    private static AtomicBoolean gameStarted = new AtomicBoolean(false);
    private HashMap<Character, Integer> letterScores = new HashMap<>(); // temporary saves the letter and its score

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
        this.assignLetterScores();
        in=new BufferedReader(new InputStreamReader(inFromclient));
        out=new PrintWriter(outToClient,true);
        int totalConnectedClients = connectedClients.incrementAndGet();
        System.out.println("[Server]Total connected clients: " + totalConnectedClients);
        server = newServer.get();
        // first the server sends the new player his id and adds him to the game
        String msgFromPlayer= null;
        try {
            gm=GameManager.get();
            msgFromPlayer=in.readLine();//gets the id from the client
            playerId= Integer.parseInt(msgFromPlayer);
            System.out.println("[Server]client "+msgFromPlayer);
            gm.addPlayer(playerId);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.serverSendMsg(protocols.NEW_PLAYER+","+connectedClients);

        StringBuilder sb = new StringBuilder();
        for (byte[] row : gm.getBonusBoard()) {
            for (byte value : row) {
                sb.append(value);
                sb.append(",");
            }
        }
        out.println(protocols.GET_BONUS+","+ sb);
        // game loop
        // server runs infinitely
        // server waits for a message from the client

            // listen to the server, and acts accordingly
        while(true)
        {	//communication protocol: newServer -> model -> playerHandler
            // 1. server sends message type
            // 2. model sends message to playehandler requesting the content
            // 3. playerhandler sends content to player
            try {
                String fullMsg = in.readLine();
                String[] messages = fullMsg.split(",");
                String msg=messages[0];
                String addMsg = "";
                if(messages.length!=1)
                {
                    addMsg=messages[1];
                    System.out.println("[Server]addMsg: "+addMsg);
                }
                System.out.println("[Server] Msg from server: " + fullMsg);
                switch (msg) {
                    case protocols.NEW_GAME:
                        this.startGame();
                        break;
                    case protocols.SERVER_SEND_MSG:
                        this.serverSendMsg(addMsg);
                        break;
                    // gets
                    case protocols.GET_BOARD:
                        this.sendBoard();
                        break;
                    case protocols.GET_TURN:
                        this.sendTurn();
                        break;
                    case protocols.GET_HAND:
                        this.sendHand();
                        break;
                    case protocols.GET_SCORE:
                        this.sendScores();
                        break;
                    case protocols.GET_CURRENT_PLAYER:
                        this.sendCurrentPlayer();
                        break;

                    // actions
                    case protocols.PLACE_WORD:
                        this.placeWord();
                        break;
                    case protocols.PASS:
                        this.pass();
                        break;
                    case protocols.CHALLENGE:
                        this.challenge();
                        break;
                    case protocols.SAVE_GAME:
                        this.saveGame();
                        break;
                    case protocols.LOAD_GAME:
                        this.loadGame();
                        break;
                    case protocols.END_GAME:
                        this.endGame();
                        break;

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



    }

    private void saveGame(){
//
//        // we need to save the game
//        // board state, players, scores, current player, bag
////        String board = gm.getBoard();
////        ArrayList<Player> players = gm.getPlayers();
////        int currentTurn = gm.getCurrentPlayer().getId();
//
//        JSONObject gameData = new JSONObject();
//        // Add data to the JSON object
//        gameData.put("board", gm.getBoard());
//        gameData.put("lastTurnBoard", gm.getLastTurnBoard());
//        gameData.put("players", gm.getPlayersData());
//        gameData.put("currentTurn", gm.getCurrentPlayer().getId());
//        gameData.put("bag", gm.getTileBag());
//        gameData.put("numPassed", gm.getNumPassed());
//        gameData.put("lastScore", gm.getLastScore());
//        try (FileWriter fileWriter = new FileWriter("gameData.json")) {
//            // Write JSON object to file
//            fileWriter.write(gameData.toJSONString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    private void loadGame() {
//        try (FileReader fileReader = new FileReader("gameData.json")) {
//            // Read the JSON file
//            JSONParser parser = new JSONParser();
//            JSONObject gameData = (JSONObject) parser.parse(fileReader);
//
//            // Retrieve the data from the JSON object
//            String board = (String) gameData.get("board");
//            String lastTurnboard = (String) gameData.get("lastTurnBoard");
//            String playersArray = (String) gameData.get("players");
//            int currentTurn = ((Long) gameData.get("currentTurn")).intValue();
//            String bagString = (String) gameData.get("bag");
//            int numPassed = ((Long) gameData.get("numPassed")).intValue();
//            int lastScore = ((Long) gameData.get("lastScore")).intValue();
//
//            // Reconstruct the game state
//            gm.loadGame(board, lastTurnboard, playersArray, currentTurn, bagString, numPassed, lastScore, this.letterScores);
//
//            // now all the players need to be updated
//            this.serverSendMsg(protocols.GET_BOARD+","+gm.getBoard());
//            this.serverSendMsg(protocols.GET_SCORE+","+gm.getScores());
//            this.serverSendMsg(protocols.GET_TURN+","+gm.getCurrentPlayer().getId());
//            this.serverSendMsg(protocols.HAND_CHANGED);
//
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }
//        Client client = ClientBuilder.newClient();
//        String endpointUrl = "http://loacalhost:9000/test_hibernate/game"; // Replace with the appropriate URL
//
//        try {
//            String response = client
//                    .target(endpointUrl)
//                    .request(MediaType.APPLICATION_JSON)
//                    .get(String.class);
//
//            // Process the response and update the game state
//        } catch (Exception e) {
//            System.out.println("Exception: " + e.getMessage());
//        } finally {
//            client.close();
//        }
    }

    private void endGame() {
        server.close();
    }

    private void sendLastScore(int lastScore) {
        out.println(protocols.GET_LAST_SCORE+","+lastScore);
        //out.println(lastScore);

    }

    private boolean challenge() {
        // returns true if the word doesnt exist

        boolean a =gm.challenge();
        System.out.println("[PlayerHandler] challenge: "+a );
        this.serverSendMsg(protocols.GET_BOARD+","+gm.getBoard());
        this.serverSendMsg(protocols.GET_SCORE+","+gm.getScores());
        this.serverSendMsg(protocols.GET_TURN+","+gm.getCurrentPlayer().getId());
        return true;
    }

    private void pass() {
        gm.passTurn();
        if(gm.isGameOver())
            this.serverSendMsg(protocols.END_GAME+","+gm.getScores());
        else
            out.println(protocols.PASS);

    }

    private void placeWord() {
        try {
            String input = in.readLine();

            String[] parts = input.split(","); // example: word = "hello,1,2,true"
            String letters = parts[0];
            int size = letters.length();
            Tile[] tileArray = new Tile[size];

            char[] chars = letters.toCharArray();
            for(int i=0;i<chars.length;i++) {
                if (chars[i] == '_') {
                    //Tile t = new Tile();
                    tileArray[i] = null;
                    continue;
                }
                Tile t = new Tile(Character.toUpperCase(chars[i]), letterScores.get(Character.toUpperCase(chars[i])));
                tileArray[i] = t;
            }
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            boolean isVertical = Boolean.parseBoolean(parts[3]);

            Word word = new Word(tileArray, row, col, isVertical);
            int score = gm.placeWord(word);
            System.out.println("[PlayerHandler] score: " + score);
            if(score != 0)
            {
                // meaning the word is okay and now we need to remove it from the player's hand
                gm.getPlayer(playerId).removeWord(word);
                gm.refillBag(playerId);
                gm.getPlayer(playerId).incrementScore(score);
                System.out.println("[PlayerHandler] Hand: " + gm.getPlayer(playerId).getHand());
                gm.nextTurn();
            }

            this.sendHand();
            this.sendLastScore(score);
            this.sendScores();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void serverSendMsg(String msg) {

        this.server.sendMessagesToAllClients(msg);

    }

    private void sendCurrentPlayer() {
        out.println(protocols.GET_CURRENT_PLAYER+","+gm.getCurrentPlayer().getId());
        //out.println(gm.getCurrentPlayer().getId());
    }

    private void startGame() {
        // model sends the message to start the game
        gameStarted.set(true);
        if(this.playerId == 0) {
            // if this is the host, he needs to start the game
            gm.startGame();
        }
        else {
            System.out.println("waiting for host to start the game");
        }
        out.println(protocols.NEW_GAME+","+gm.getPlayer(this.playerId).getHand());
        // when starting the game all the players need to get their hand
        //out.println(gm.getPlayer(this.playerId).getHand());

    }

    // send methods to send information to the model
    private void sendScores() {
        out.println(protocols.GET_SCORE+","+gm.getScores());
//        out.println(gm.getScores());
    }

    private void sendHand() {
        out.println(protocols.GET_HAND+","+gm.getPlayer(playerId).getHand());
    }

    private void sendTurn() {
        out.println(protocols.GET_TURN+","+gm.getCurrentTurn());
    }

    private void sendBoard() {// TODO - need to check if true
        System.out.println("[PlayerHandler] board: " + gm.getBoard());
        out.println(protocols.GET_BOARD+","+gm.getBoard());
    }

    @Override
    public void close() {
        try {
            // Close the input and output streams
            in.close();
            out.close();
        } catch (IOException e) {
            // Handle the exception or log the error if necessary
            e.printStackTrace();
        }

    }
    private void assignLetterScores() {
        letterScores.put('A', 1);
        letterScores.put('B', 3);
        letterScores.put('C', 3);
        letterScores.put('D', 2);
        letterScores.put('E',1);
        letterScores.put('F',4);
        letterScores.put('G',2);
        letterScores.put('H',4);
        letterScores.put('I',1);
        letterScores.put('J',8);
        letterScores.put('K',5);
        letterScores.put('L',1);
        letterScores.put('M',3);
        letterScores.put('N',1);
        letterScores.put('O',1);
        letterScores.put('P',3);
        letterScores.put('Q',10);
        letterScores.put('R',1);
        letterScores.put('S',1);
        letterScores.put('T',1);
        letterScores.put('U',1);
        letterScores.put('V',4);
        letterScores.put('W',4);
        letterScores.put('X',8);
        letterScores.put('Y',4);
        letterScores.put('Z',10);
    }

}
