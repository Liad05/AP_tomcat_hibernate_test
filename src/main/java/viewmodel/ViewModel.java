package viewmodel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import model.Model;
import model.protocols;
import test.CharacterData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {

	Model m;
	private BooleanProperty gameStartedProperty, isGameOver;
	public IntegerProperty[][] bonus_vm; // saves the bonus tiles
	public StringProperty wordSelected, tilesLeft, letter, confirm, row, col, wordDirection, playerPoints, turn, numPlayers, id; // all strings that binded to labels in the view
	public SimpleStringProperty[][] board; // saves the letters on the board
	public ObjectProperty<Background>[][] background; // saves the background of the board
	public ListProperty<String> letterList; // saves the 7 letters in the user's hand, binds to currentHand in the view
	public ListProperty<CharacterData> userInput; // saves the letters the user has selected in the current turn
	public ListProperty<CharacterData> lastEntry; // saves the letters the user has selected in the last turn
	public boolean isPlayerTurn;
	public ViewModel(Model m) {
		this.m = m;
		m.addObserver(this);
		initializeProperties();
	}

	@Override
	public void update(Observable obs, Object obj) {
		if (obs != m) {
			return; // If the notification is not from Model, we simply return.
		}

		String newBoardState = getBoardState(obj);
		setChanged();
		notifyObservers(newBoardState);
		System.out.println("ViewModel: " + newBoardState);
		switch (newBoardState) {
			//case "help" -> handleHelpRequest();
			case protocols.START_GAME:
				handleStartGame();
				break;
			case protocols.NEW_PLAYER:
				handleNewPlayer();
				break;
			case "confirmed":
				handleConfirmation();
				break;
			case protocols.BOARD_CHANGED:
				updateBoard(m.getUpdateBoard());
				break;
			case protocols.GET_HAND:
				updateLetterList();
				break;
			case protocols.GET_SCORE:
				updateScore();
				break;
			case protocols.GET_TURN:
				updateTurn();
				break;
			case protocols.CHALLENGE:
				handleChallengeRequest();
				break;
			case "clear":
				handleClearRequest();
				break;
			case protocols.PASS:
				handlePass();
				break;
			case "undo":
				handleUndoRequest();
				break;
			case protocols.END_GAME:
				handleEndGame();
				break;

		}
	}
	//***************************************************************************************************************
	// functions to handle changes in the game
	private void handleStartGame() {
		gameStartedProperty.set(true);
		setBonus_vm(m.getBonus());
		resetGameBoard();
		updateLetterList();
		confirm.set("");
		wordSelected.set("");
		row.set("");
		col.set("");
		wordDirection.set("");
		userInput.clear();
		System.out.println("userInput: "+userInput);

		m.cleanList();
		playerPoints.set(m.getPlayerScore());
	}
	private void handleConfirmation() {

		if(isPlayerTurn)
		{
			Platform.runLater(() -> {
						// update labels
						confirm.set(m.getConfirm());
						wordSelected.set("");

						// this is the function that places the word on the board aka m.getWordSelected()
						m.tryPlaceWord();
						wordSelected.set(m.getWordSelected());

						// update labels
						row.set(m.getRow());
						col.set(m.getCol());

						// sanity check
						System.out.println("word selected: " + wordSelected.get());
						// try to add the word to the board

						// wordselcted is the word that the user has selected
						// if wordSelcted="" then the word is not valid
						// if wordSelected!="", then the word is valid
						if (!wordSelected.get().equals("") )
						{
							wordDirection.set(m.getWordDirection());
						}
						else {
							int wordSize = userInput.size();
							for (int i = 0; i < wordSize; i++) {
								m.undoSelected();
							}
						}

						// after word is confirmed, the letters are added to the board
						// now the server will tell all the players to update their boards

						lastEntry.clear();
						lastEntry.addAll(userInput);
						userInput.clear();

						m.cleanList();
						m.serverSendMessagesToAllClients(protocols.BOARD_CHANGED);
						System.out.println("Message sent: BOARD_CHANGED");
						m.serverSendMessagesToAllClients(protocols.UPDATE_SCORE);
						System.out.println("Message sent: UPDATE_SCORE");
						m.serverSendMessagesToAllClients(protocols.UPDATE_TURN);
						System.out.println("Message sent: UPDATE_TURN");
						turn.set(m.getTurn());

					}
			);


		}



	}
	private void handleChallengeRequest() {
		confirm.set("Challenge");
		wordSelected.set("");
	}
	private void handleEndGame() {
		System.out.println("Game Ended");
		playerPoints.set(m.getScore());
		isGameOver.set(true);
	}

	private void handleNewPlayer() {
		System.out.println("hi");
		Platform.runLater(() -> {
			numPlayers.set("Number Of Player Connected: "+m.getNumPlayers());
		});
	}
	private void handleClearRequest() {
		confirm.set("");
		wordSelected.set("");
		row.set("");
		col.set("");
		wordDirection.set("");
	}
	public void handlePass() {
		if(isPlayerTurn) {
			Platform.runLater(() -> {
				confirm.set("Passed Turn");
				wordSelected.set("");
				row.set("");
				col.set("");
				wordDirection.set("");
				ArrayList<CharacterData> input = m.getCharacterList();
				int wordSize = input.size();
				for (CharacterData characterData : input) {
					board[characterData.getRow()][characterData.getColumn()].set("");
					setBackground(characterData.getRow(), characterData.getColumn());
				}
				userInput.clear();
				System.out.println("userInput: " + userInput);

				m.cleanList();
				updateLetterList();
				m.serverSendMessagesToAllClients(protocols.UPDATE_TURN);
				System.out.println("Message sent: UPDATE_TURN");
				turn.set(m.getTurn());
				//getTilesLeft();
			});
		}
	}
	private void handleUndoRequest() {
		Platform.runLater(() -> {
					CharacterData input = m.getUndoLetter();

					if (input!=null) {
						confirm.set("");
						wordSelected.set("");
						row.set("");
						col.set("");
						wordDirection.set("");
						int i = input.getRow();
						int j = input.getColumn();
						letterList.add(Character.toString(input.getLetter()));
						board[i][j].set("");
						setBackground(i, j);
					}
					//m.serverSendMessagesToAllClients(protocols.BOARD_CHANGED);

				}
		);

	}
	private void updateTurn() {
		isPlayerTurn = m.isPlayerTurn();
	}
	private void updateScore() {
		Platform.runLater(() -> {

			playerPoints.set(m.getScore());
		}
		);
	}

	public BooleanProperty gameStartedProperty() {
		return gameStartedProperty;
	}
	public BooleanProperty isGameOverProperty() {
		return isGameOver;
	}


	public void letterSelected(char letter, int row, int col) {
		if(isPlayerTurn) {
			board[row][col].set(Character.toString(letter)); // updates the board with the letter the user has selected
			userInput.add(new CharacterData(letter, row, col)); // adds the letter to the list of letters the user has selected
			System.out.println("userInput: "+userInput);
			background[row][col].set(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null))); // changes the background of the letter to light blue
			//userBoardList.add(Character.toString(letter)); // adds the letter to the list of letters the user has selected
			letterList.remove(Character.toString(letter)); // removes the letter from the list of letters the user has in his hand
			m.letterSelected(letter, row, col);
		}
		else{
			System.out.println("Not your turn");
			System.out.println("isPlayerTurn: "+ isPlayerTurn);
		}

	}

	public void updateBoard(String[][] newBoard){
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){

					if(!newBoard[i][j].equals("_")) {
						board[i][j].set(newBoard[i][j]);
						background[i][j].set(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null))); // changes the background of the letter to light blue

					}
					else{
						board[i][j].set("");
						setBackground(i,j);
				}

			}
		}
	}

	//***************************************************************************************************************
	// functions that activate the functions in the model

	public void confirmSelected() {

		if (this.isPlayerTurn)
			m.confirmSelected();
	}

	public void undoSelected() {
		if (this.isPlayerTurn)
			m.undoSelected();
	}
	public void challengeSelected() {
		if (this.isPlayerTurn)
			m.challengeSelected();
	}
	public void passSelected() {
		if (this.isPlayerTurn)
			m.passSelected();
	}
	public void getNumPlayers() {
		numPlayers.set("Number Of Players Connected: " +m.getNumPlayers());
	}

	public void setBonus_vm(byte[][] bonus) {
		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++) {
				bonus_vm[i][j] = new SimpleIntegerProperty();
				bonus_vm[i][j].set(bonus[i][j]);
			}

	}

	public IntegerProperty[][] getBonus_vm() {
		return bonus_vm;
	}





	private void updateLetterList() {
		// update the letters the player see
		//test.Player currentPlayer = m.getCurrentPlayer();
		Platform.runLater(() -> {
			ObservableList<String> newLetters = FXCollections.observableArrayList(m.getLetterList());
			letterList.set(newLetters);
		});
	}

	private String getBoardState(Object obj) {
		if (obj instanceof Integer) {
			return Integer.toString((int) obj);
		} else if (obj instanceof Character) {
			return Character.toString((char) obj);
		} else {
			return (String) obj;
		}
	}



	private void setBackground(int i, int j) {
		// set the background color of a spesific tile
		switch (bonus_vm[i][j].get()) {
			case 2:
				background[i][j].set(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
				break;
			case 3:
				background[i][j].set(new Background(new BackgroundFill(Color.DARKBLUE, null, null)));
				break;
			case 20:
				background[i][j].set(new Background(new BackgroundFill(Color.LIGHTPINK, null, null)));
				break;
			case 30:
				background[i][j].set(new Background(new BackgroundFill(Color.DARKRED, null, null)));
				break;
			default:
				background[i][j].set(new Background(new BackgroundFill(Color.WHITE, null, null)));
		}
	}

	private void resetGameBoard() {
		// reset all the game board
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				board[i][j].set("");
				switch (bonus_vm[i][j].getValue()) {
					case 2:
						background[i][j].set(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
						break;
					case 3:
						background[i][j].set(new Background(new BackgroundFill(Color.DARKBLUE, null, null)));
						break;
					case 20:
						background[i][j].set(new Background(new BackgroundFill(Color.LIGHTPINK, null, null)));
						break;
					case 30:
						background[i][j].set(new Background(new BackgroundFill(Color.DARKRED, null, null)));
						break;
					default:
						background[i][j].set(new Background(new BackgroundFill(Color.WHITE, null, null)));
						break;
				}
			}
		}
	}

	private void initializeProperties() {
		gameStartedProperty = new SimpleBooleanProperty(false);
		isGameOver = new SimpleBooleanProperty(false);
		board = new SimpleStringProperty[15][15];
		confirm = new SimpleStringProperty();
		tilesLeft = new SimpleStringProperty();
		letter = new SimpleStringProperty();
		wordSelected = new SimpleStringProperty();
		row = new SimpleStringProperty();
		col = new SimpleStringProperty();
		wordDirection = new SimpleStringProperty();
		playerPoints = new SimpleStringProperty();
		numPlayers = new SimpleStringProperty();
		userInput = new SimpleListProperty<CharacterData>(FXCollections.observableArrayList());
		lastEntry = new SimpleListProperty<CharacterData>(FXCollections.observableArrayList());
		letterList = new SimpleListProperty<String>();
		turn = new SimpleStringProperty();
		id = new SimpleStringProperty();
		//userBoardList = new ArrayList<>();
		bonus_vm = new IntegerProperty[15][15];
		background = new ObjectProperty[15][15];
		//setBonus_vm(m.getBonus());
		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++) {
				board[i][j] = new SimpleStringProperty();
				background[i][j] = new SimpleObjectProperty<>();
			}
	}


	public void getId() {
		this.id.set(m.getId());
	}
	public String getPlayerPoints() {
		System.out.println("playerPoints: "+this.playerPoints.get());
		return this.playerPoints.get();
	}

	public void saveGame()
	{
		m.saveGame();
	}

	public void loadGame() {
		m.loadGame();
	}
}
