package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import viewmodel.ViewModel;

import java.io.IOException;

public class SceneController {
    private Stage primaryStage;
    private ViewModel viewModel;
    private Model model;

    private Scene mainMenuScene;
    private Scene helpMenuScene;
    private Scene HostWaitScene;
    private Scene GuestWaitScene;
    private Scene gameScene;
    private Scene endScene;

    public SceneController(Stage primaryStage) {
        String ip = "1";
        int port = 1;// TODO:  need to change
        this.primaryStage = primaryStage;
        this.model = new Model();// TODO:  need to make IP and Port dynamic
        this.viewModel = new ViewModel(model);
        model.addObserver(viewModel);
        this.primaryStage.setWidth(620);  // Width
        this.primaryStage.setHeight(620);  // Height
        this.primaryStage.setResizable(false); // Making sure the window is not resizable
    }

    public void loadScenes() throws IOException {
        // Load main menu scene
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("/MainMenu.fxml"));
        Parent mainMenuRoot = mainMenuLoader.load();
        MainMenuController mainMenuController = mainMenuLoader.getController();
        mainMenuController.setViewModel(viewModel);
        mainMenuController.setModel(model);
        mainMenuController.setSceneController(this);
        mainMenuScene = new Scene(mainMenuRoot);

        // Load help menu scene
//        FXMLLoader helpMenuLoader = new FXMLLoader(getClass().getResource("/view/HelpWindow.fxml"));
//        Parent helpMenuRoot = helpMenuLoader.load();
//        HelpMenuController helpMenuController = helpMenuLoader.getController();
//        helpMenuController.setViewModel(viewModel);
//        helpMenuController.setSceneController(this);
////        viewModel.addObserver(helpMenuController);
//        helpMenuScene = new Scene(helpMenuRoot);

        // Load Host Wait scene
        FXMLLoader HostWaitLoader = new FXMLLoader(getClass().getResource("/WaitingRoomHost.fxml"));
        Parent HostWaitRoot = HostWaitLoader.load();
        HostWindowController HostWaitController = HostWaitLoader.getController();
        HostWaitController.setViewModel(viewModel);
        HostWaitController.setSceneController(this);
//        viewModel.addObserver(HostWaitController);
        HostWaitScene = new Scene(HostWaitRoot);

        // Load Guest Wait scene
        FXMLLoader GuestWaitLoader = new FXMLLoader(getClass().getResource("/WaitingRoomGuest.fxml"));
        Parent GuestWaitRoot = GuestWaitLoader.load();
        GuestWindowController GuestWaitController = GuestWaitLoader.getController();
        GuestWaitController.setViewModel(viewModel);
        GuestWaitController.setModel(model);
        GuestWaitController.setSceneController(this);
//        viewModel.addObserver(GuestWaitController);
        GuestWaitScene = new Scene(GuestWaitRoot);

        // Load game scene
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        Parent gameRoot = gameLoader.load();
        MainWindowController gameController = gameLoader.getController();
        gameController.setViewModel(viewModel);
        gameController.setSceneController(this);
//        viewModel.addObserver(gameController);
        gameScene = new Scene(gameRoot);

        // Load end scene
        FXMLLoader endLoader = new FXMLLoader(getClass().getResource("/EndGame.fxml"));
        Parent endRoot = endLoader.load();
        EndGameController endController = endLoader.getController();
        endController.setViewModel(viewModel);
        endController.setSceneController(this);
//        viewModel.addObserver(gameController);
        endScene = new Scene(endRoot);
    }

    public void showMainMenu() {
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    public void showHelpMenu() {
        primaryStage.setScene(helpMenuScene);
        primaryStage.show();
    }
    public void showHostWaitingRoom() {
        primaryStage.setScene(HostWaitScene);
        primaryStage.show();
    }
    public void showGuestWaitingRoom() {
        primaryStage.setScene(GuestWaitScene);
        primaryStage.show();
    }

    public void showGame() {
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    public void endGame() {

        primaryStage.setScene(endScene);
        primaryStage.show();
    }
}
