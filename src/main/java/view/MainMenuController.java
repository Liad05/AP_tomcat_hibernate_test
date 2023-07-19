package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Model;
import model.newServer;
import viewmodel.ViewModel;

public class MainMenuController {

    public TextField ipField;
    public TextField portField;
    private SceneController sceneController;
    private newServer server;
    private ViewModel vm;
    private Model model;

    private String ip;
    private String port;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    public void startGame() {

        sceneController.showGame();
    }
    @FXML
    public void showHostWaitingRoom() {
        this.port = portField.getText();
        this.ip = ipField.getText();
        server = newServer.get();
        this.model.connectToServer(this.ip, Integer.parseInt(this.port));
        vm.getNumPlayers();
        vm.getId();
        sceneController.showHostWaitingRoom();
    }
    @FXML
    public void showGuestWaitingRoom() {
        this.port = portField.getText();
        this.ip = ipField.getText();
        this.model.connectToServer(this.ip, Integer.parseInt(this.port));
        vm.getNumPlayers();
        vm.getId();
        sceneController.showGuestWaitingRoom();
    }

    @FXML
    public void showHelp() {
        sceneController.showHelpMenu();
    }

    @FXML
    public void showStatistics() {
        // load statistics scene
    }

    public void setViewModel(ViewModel vm) {
        this.vm = vm;
    }


    public void setModel(Model m) {
        this.model = m;
    }


}
