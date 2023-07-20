package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import view.MainWindowController;
import viewmodel.ViewModel;
import view.SceneController;

class MainWindowControllerTest {

    private MainWindowController controller;
    private ViewModel mockViewModel;
    private SceneController mockSceneController;

    @BeforeEach
    void setUp() {
        mockViewModel = mock(ViewModel.class);
        mockSceneController = mock(SceneController.class);
        controller = new MainWindowController();
        controller.setViewModel(mockViewModel);
        controller.setSceneController(mockSceneController);
    }

    @Test
    void testShowHelp() {
        controller.showHelp();
        verify(mockSceneController, times(1)).showHelpMenu();
    }

    @Test
    void testShowConfirm() {
        controller.showConfirm();
        verify(mockViewModel, times(1)).confirmSelected();
    }

    @Test
    void testUndo() {
        controller.undo();
        verify(mockViewModel, times(1)).undoSelected();
    }

    @Test
    void testPass() {
        controller.pass();
        verify(mockViewModel, times(1)).passSelected();
    }

    @Test
    void testChallenge() {
        controller.challenge();
        verify(mockViewModel, times(1)).challengeSelected();
    }

    @Test
    void testSaveGame() {
        controller.saveGame();
        verify(mockViewModel, times(1)).saveGame();
    }

    @Test
    void testLoadGame() {
        controller.loadGame();
        verify(mockViewModel, times(1)).loadGame();
    }

    // Continue with tests for all the other methods.
    // Remember to test edge cases and any relevant interactions.
}