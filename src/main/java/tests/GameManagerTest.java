package tests;

import model_foundations.GameManager;
import model_foundations.Player;

public class GameManagerTest {
    public static void main(String[] args) {
        GameManager g= new GameManager();
        g.addPlayer(new Player(1));
        g.addPlayer(new Player(2));
        g.runGame();
    }
}
