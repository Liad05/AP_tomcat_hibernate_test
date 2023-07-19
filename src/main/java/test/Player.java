package test;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class Player {

    private int id;
    private int score = 0;
    private ArrayList<Tile> hand;

    public Player(int id) {
        this.id = id;
        this.score = 0;
        this.hand = new ArrayList<>();
    }
    // get
    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }
    public String getHand() { // used for playerHandler to send the hand to the model
        String handString = "";
        for (int i = 0; i < hand.size()-1; i++)
        {
            handString +=  hand.get(i) + ";";
        }
        handString +=  hand.get(hand.size()-1);
        return handString;
    }

    public ArrayList<Tile> gethand() {
        return hand;
    }
    // useful functions
    public void incrementScore(int score) {
        this.score += score;
    }
    public void addTile(Tile t)
    {
        hand.add(t);
    }
    public void removeTiles()
    {
        hand.clear();
    }
    public void refillHand(Tile.Bag tileBag) {
        // addding tiles to the hand until it reaches 7 tiles
        while (hand.size() < 7) {
            Tile tile = tileBag.getRand();
            if (tile != null) {
                hand.add(tile);
            } else {
                break; // If no more tiles left in the bag
            }
        }
    }

    public void resetScore() {
        score = 0;
    }

    public void removeWord(Word w)
    {
        	for(Tile t:w.getTiles())
        	{
        		hand.remove(t);
        	}
    }

    public int choice()
    {   Scanner Scanner = new Scanner(System.in);
        System.out.println("Player "+id+" turn");
        System.out.println(hand);
        System.out.println("1. Place a word, 2.pass");
        return Scanner.nextInt();
    }

    public Word getWord() {

        Vector<Tile> wordTiles = new Vector<>(); // saves the tiles that are part of the word the user has selected
        Scanner Scanner = new Scanner(System.in);
        int input;
        while (true) {
            System.out.println(hand);
            System.out.println("Enter the index of the char (enter '0' to stop):");
            input = Scanner.nextInt();
            if (input == 0) {
                break;
            } else {
                wordTiles.add(hand.get(input-1));
            }
        }
        System.out.println("Enter row");
        int row = Scanner.nextInt();

        System.out.println("Enter col");
        int col = Scanner.nextInt();
        System.out.println("Enter vertical or horizontal (1 for vertical, 2 for horizontal)");
        int dir = Scanner.nextInt();
        boolean vert;
        if(dir==1)
        {
            vert=true;
        }
        else
        {
            vert=false;
        }
        Tile[] array = new Tile[wordTiles.size()];
        wordTiles.toArray(array);
        return new Word(array,row,col,vert);

    }



    public String toString() {
        return "Player " + id + " score: " + score ;
    }
}