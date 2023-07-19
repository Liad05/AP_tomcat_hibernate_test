package test;

import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {
    public static GameManager instance;

    private Board board; // the current board state
    private Board lastTurnBoard; // the board state after the last turn
    private ArrayList<Player> players;
    private int turn; // index of the current player's turn
    private Tile.Bag tileBag; // the bag of tiles for the game
    private int lastScore; // the score of the last word placed
    private int numPassed; // number of players who have passed their turn

    public static GameManager get()
    {
        if(instance==null)
        {
            instance = new GameManager();
        }
        return instance;
    }
    public GameManager() {
        this.board =  new Board();
        this.players = new ArrayList<>();
        this.turn = 0;
        this.tileBag =  new Tile.Bag();
        this.numPassed=0;
    }
    public void loadGame(String board, String lastTurnBoard, String players, int turn, String tileBag, int numPassed, int lastScore, HashMap<Character, Integer> letterScores)
    {
        // first the board
        this.setBoard(board);
        // second the last turn board
        this.setLastTurnBoard(lastTurnBoard);

        // third the players
        String[] values = players.split(",");
        ArrayList<Player> playersArr = new ArrayList<>(); // players will save all the players in the game
        for(String value:values)
        {
            String[] playerData = value.split(":");
            Player player = new Player(Integer.parseInt(playerData[0]));
            String[] score_hand = playerData[1].split(";"); // hand will save all the tiles in the player's hand
            player.incrementScore(Integer.parseInt(score_hand[0]));
            for(int i=1; i<score_hand.length; i++)
            {
                String tile = score_hand[i];
                player.addTile(new Tile(tile.charAt(0), letterScores.get(tile.charAt(0))));
            }
            playersArr.add(player);
        }
        this.players = playersArr;

        // fourth the current turn
        this.turn = turn;

        // fifth the tile bag
        char[] chars = tileBag.toCharArray();
        Tile.Bag bag = new Tile.Bag();
        for (char c : chars)
        {
            bag.put(new Tile(c, letterScores.get(c)));
        }
        this.tileBag = bag;

        // sixth the number of passed players
        this.numPassed = numPassed;

        // seventh the last score
        this.lastScore = lastScore;
    }
    public void setBoard(String board)
    {
        this.board = new Board(board);
    }
    public void setLastTurnBoard(String lastTurnBoard)
    {
        this.lastTurnBoard = new Board(lastTurnBoard);
    }
    public byte[][] getBonusBoard(){
        return board.getBonus();
    }
    public String getTileBag()
    {
        return tileBag.toString();
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
    public void addPlayer(int id) {
        players.add(new Player(id));
    }
    // function to get all the players in the game
    public ArrayList<Player> getPlayers() {
        return players;
    }
    public String getPlayersData(){
        String data="";
        for(Player p:players)
        {
            data+=p.getId()+":"+p.getScore()+";"+p.getHand()+",";
        }
        return data;
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }
    public String getScores()
    {
        String scores="";
        for(Player p:players)
        {
            scores+=p.getScore()+",";
        }
        return scores;
    }


    public void startGame()
    {
        board = new Board();
        tileBag = new Tile.Bag();
        lastScore = 0;
        numPassed=0;
        for (Player player : players) {
            player.resetScore();
            player.refillHand(tileBag);
        }
    }
    public void restartGame(){

        board = new Board();
        tileBag = new Tile.Bag();
        lastScore = 0;
        numPassed=0;
        for (Player player : players) {
            player.removeTiles();
            player.resetScore();
            player.refillHand(tileBag);
        }
    }


    public int placeWord(Word word) {
        // need to check if the word is legal
        // if it is legal then place the word and return true
        lastTurnBoard = new Board(board);
        int score=0;
        score = board.tryPlaceWord(word);
        if(score == 0){
            board = lastTurnBoard;
        }
        lastScore = score;
        return score;
    }

    public void passTurn(){
        System.out.println("Player " + players.get(turn).getId() + " has passed their turn");
        lastScore=0;
        numPassed++;
        turn = (turn + 1) % players.size();
    }
    public int getCurrentTurn(){
        return turn;
    }
    public void nextTurn(){
        turn = (turn + 1) % players.size();
    }
    public Player getCurrentPlayer() {
        return players.get(turn);
    }

    public String getBoard() {
        return board.toString();
    }
    public String getLastTurnBoard()
    {
        return lastTurnBoard.toString();
    }
    public int getLastScore()
    {
        return lastScore;
    }
    public int getNumPassed()
    {
        return numPassed;
    }

    public boolean isGameOver()
    { // need to check if any of the players have a word to place
        if(numPassed == 2*players.size())
            return true;
        if(tileBag.size()==0)
            return true;
        for(Player p:players)
        {
            if(p.gethand().size()==0)
                return true;

        }
        return false;
    }



    private Player determineWinner() {
        Player winner = players.get(0);
        for (Player player : players) {
            if (player.getScore() > winner.getScore()) {
                winner = player;
            }
        }
        return winner;
    }

    public void refillBag(int id)
    {
        players.get(id).refillHand(tileBag);
    }
    public void printScores()
    {
        System.out.println("**************************SCORES*********************************");

        for(Player p:players)
        {
            System.out.println("Player " + p.getId() + " has a score of " + p.getScore());
        }
        System.out.println("***********************************************************");

    }

    public void runGame()
    { // main function that runs a scrabble game
        restartGame();
        while(!isGameOver())
        {
            // get the current player
            Player currentPlayer = getCurrentPlayer();
            board.print();
            if(currentPlayer.choice()==1)
            {
                Word word = currentPlayer.getWord();
                while(placeWord(word)==0)
                {
                    word = currentPlayer.getWord();
                }
                numPassed=0;
            }
            else
            {
                numPassed++;
                if(numPassed==players.size())
                {
                    // if all players pass then the game is over
                    break;
                }
            }
            printScores();
        }
        Player winner = determineWinner();
        System.out.println("The winner is player " + winner.getId() + " with a score of " + winner.getScore());
    }
    public boolean challenge() {
        // returns true if the word doesnt exist
        // returns false if the word does exist
        if(board.challenge()) // word doesnt exist
        {
            // challenger loses his turn due to unsuccessful challenge
            players.get((turn + players.size() - 1) % players.size()).incrementScore(-lastScore);
            board = lastTurnBoard;
            return true;
        }
        else // word does exist
        {
            // last player get a score of 0 for his last turn
            // i think that the player won't get back his tiles and will just get new ones instead
            // need to update the board accordingly
            turn = (turn + 1) % players.size();
            return false;

        }
    }

    public Player getPlayer(int index){
        return players.get(index);
    }

}