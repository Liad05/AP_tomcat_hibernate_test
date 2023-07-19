package view;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		SceneController sceneController = new SceneController(primaryStage);
		try {
			sceneController.loadScenes();
			sceneController.showMainMenu();  // Start with main menu
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
