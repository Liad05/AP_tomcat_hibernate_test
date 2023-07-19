package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Model;
import model.newServer;
import viewmodel.ViewModel;

import java.util.Observable;
import java.util.Observer;

public class GuestWindowController extends Observable implements Observer {
    private SceneController sceneController;
    private Stage stage;

    @FXML
    private Button startGameButton;

    @FXML
    private Label numberOfPlayerLabel;

    public static newServer server;
    private Model model;
    private ViewModel viewModel;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(Model model) {
        this.model = model;
    }


    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.getId();
        numberOfPlayerLabel.textProperty().bind(this.viewModel.id);
        viewModel.gameStartedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                //System.out.println("game started");
                if (newValue) {
                    sceneController.showGame();
                }
            });

        });
    }


    @Override
    public void update(Observable o, Object arg) {

    }


}
