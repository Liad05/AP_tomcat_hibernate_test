package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import viewmodel.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class EndGameController implements Observer {

    @FXML
    private Label titleLabel;

    @FXML
    private ListView<String> scoresListView;

    @FXML
    private Label winnerLabel;

    @FXML
    private Button backButton;

    private ObservableList<String> scoresList;

    @FXML
    private Label messageLabel;

    private SceneController sceneController;
    private ViewModel vm;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void backToMainMenu() {
        // TODO: Go back to main menu, and handle the server and clients
//        sceneController.showMainMenu();
        System.exit(0);

    }

    public void setViewModel(ViewModel vm) {
        this.vm = vm;
        vm.isGameOverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {

               setScores(vm.getPlayerPoints());
//                setWinner(vm.getWinner());
            }
        });

    }

    public void initialize() {
        scoresList = FXCollections.observableArrayList();
        scoresListView.setItems(scoresList);
    }

    public void setScores(String scores) {
        String[] playerEntries = scores.split("\n");
        int highestScore = Integer.MIN_VALUE;
        String playerWithHighestScore = "";

        for (String entry : playerEntries) {
            scoresList.add(entry);
            String[] parts = entry.split(": ");
            if (parts.length == 2) {
                String player = parts[0].trim();
                int score = Integer.parseInt(parts[1].trim());

                if (score > highestScore) {
                    highestScore = score;
                    playerWithHighestScore = player;
                }
            }

        }
        this.setWinner(playerWithHighestScore);

    }

    public void setWinner(String winner) {
        winnerLabel.setText("Winner: " + winner);
    }

    @Override
    public void update(Observable o, Object arg) {
        // Update the messageLabel text based on game result
        messageLabel.setText((String) arg);
    }
}
