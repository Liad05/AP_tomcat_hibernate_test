package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import viewmodel.ViewModel;

import java.util.Observable;
import java.util.Observer;


public class HelpMenuController extends Observable implements Observer {
    @FXML
    private Button closeButton;
    private MainWindowController mainWindowController;
    private MainMenuController mainMenuController;
    private String returnTo = "";
    private Stage stage;
    ViewModel vm;
    private SceneController sceneController;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }


    @FXML
    public void closeHelp() {
        sceneController.showMainMenu();
    }
    public void setViewModel(ViewModel vm) {
        this.vm = vm;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void update(Observable o, Object arg) {

    }


}