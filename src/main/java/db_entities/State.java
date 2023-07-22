package db_entities;


public class State{
    private int id;
    private String ip;
    private int port;
    private int currentTurn;
    private int lastScore;
    private String players;
    private String bag;
    private String lastTurnBoard;
    private String board;
    private int numPassed;

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getLastScore() {
        return lastScore;
    }

    public String getPlayers() {
        return players;
    }

    public String getBag() {
        return bag;
    }

    public String getLastTurnBoard() {
        return lastTurnBoard;
    }

    public String getBoard() {
        return board;
    }

    public int getNumPassed() {
        return numPassed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public void setLastTurnBoard(String lastTurnBoard) {
        this.lastTurnBoard = lastTurnBoard;
    }

    public void setBoard(String board) {
        this.board = board;
    }



    public void setNumPassed(int numPassed) {
        this.numPassed = numPassed;
    }

    public String toString()
    {
        return "id:" + id + " ip:" + ip + " port:" + port + " currentTurn:" + currentTurn + " lastScore:" + lastScore + " players:" + players + " bag:" + bag + " lastTurnBoard:" + lastTurnBoard + " board:" + board + " numPassed:" + numPassed;
    }

    public String toSaveString()
    {
        return id + " " + ip + " " + port + " " + currentTurn + " " + lastScore + " " + players + " " + bag + " " + lastTurnBoard + " " + board + " " + numPassed;
    }

    public void fromSaveString(String saveString)
    {
        String[] splitSaveString = saveString.split(" ");
        id = Integer.parseInt(splitSaveString[0]);
        ip = splitSaveString[1];
        port = Integer.parseInt(splitSaveString[2]);
        currentTurn = Integer.parseInt(splitSaveString[3]);
        lastScore = Integer.parseInt(splitSaveString[4]);
        players = splitSaveString[5];
        bag = splitSaveString[6];
        lastTurnBoard = splitSaveString[7];
        board = splitSaveString[8];
        numPassed = Integer.parseInt(splitSaveString[9]);
    }

    public  String toCompString()//comprehensive string
    {
        String retString = "";
        String[] tempPlayer;
        retString += "id:" + id + "\n ";
        String playersStr = players;
        String[] playersList = playersStr.split(",");
        for(String player: playersList){
            tempPlayer = player.split(":");//seperate player id from score and hand

            retString+="player number "+tempPlayer[0]+" score:"+tempPlayer[1].split(";")[0]+"\n ";
        }
        return retString;


    }
}
