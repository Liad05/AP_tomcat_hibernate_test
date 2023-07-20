package model;

import model_foundations.CharacterData;
import model_foundations.GameManager;
import model_foundations.Tile;
import model_foundations.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class Model extends Observable {
	// client attributes
	private int id;
	private String ip;
	private int port;
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private boolean isHost = false;
	private boolean exit = false;

	// game attributes
	private String letterList;
	private ArrayList<String> newLetterList = new ArrayList<>();

	// need to change every instance of gameManager to call to the playerHandler
	private static GameManager gameManager;
	private byte[][] bonus;
	private String[][] updatedBoard;
	private int turn = 0;
	private int lastScore = 0;
	private String scores = "";
	private int numPlayers=0;
	private int numTilesLeft=0;
	// labels
	private String boardState, help, confirm;
	private char letter;
	private int rowCur = -1, colCur = -1;
	String wordSelected=""; // saves the word the user has selected
	private boolean isWordSelected = false; // saves whether the user has selected a word or not
	private CharacterData undoLetter; // saves the letter the user has selected to undo

	// helper attributes
	private HashMap<Character, Integer> letterScores = new HashMap<>(); // temporary saves the letter and its score
	private ArrayList<CharacterData> characterList = new ArrayList<>(); // saves the letters the user has selected to put on the board in the current turn
	private ArrayList<CharacterData> lastEntry = new ArrayList<>(); // saves the letters the user has selected int his previous turn in order to undo in a later turn
	private Vector<Tile> wordTiles = new Vector<>(); // saves the tiles that are part of the word the user has selected


//	}

	public Model() {
		assignLetterScores();
		updatedBoard = new String[15][15];

	}
	public void connectToServer(String ip, int port) {
		try {
			this.ip = ip;
			this.port = port;
			System.out.println("Connecting to server...");
			client = new Socket(this.ip, this.port);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));

			// first step: when the client connects to the server, the server sends him his id
			this.id = Integer.parseInt(in.readLine());
			out.println(ip);
			out.println(port);
			System.out.println("Connected to server, client " + id);
			isHost = id == 0;

			// second step: sending it back to the playerHandler
			out.println(id);
			String msgFromServer = null;

			Thread getMsgFromServer = new Thread(()->{ // listen to the server, and acts accordingly
				//communication protocol: newServer -> model -> playerHandler
				// 1. server sends message type
				// 2. model sends message to playehandler requesting the content
				// 3. playerhandler sends content to player
				while(true)
				{
					try {
						String fullMsg = in.readLine();
						String[] messages = fullMsg.split(",");
						String msg=messages[0];
						String addMsg = "";
						if (messages.length != 1) {
							for (int i = 1; i < messages.length; i++) {
								addMsg += messages[i];  // Concatenate the strings
							}
						}
						System.out.println("[Client] Msg from server: " + fullMsg);

						switch (msg) {

							// protocol: server -> model -> playerHandler

							case protocols.START_GAME:
								out.println(protocols.NEW_GAME);
								break;
							case protocols.GET_BONUS:
								System.arraycopy(messages, 1, messages, 0, messages.length - 1);

								bonus= new byte[15][15];
								for(int i=0;i<15;i++)
								{
									for(int j=0;j<15;j++)
									{
										bonus[i][j]=Byte.parseByte(messages[i*15+j]);
									}
								}
								break;
							case protocols.BOARD_CHANGED:
								//from server: notification that the board is changed
								// to playehandler: get the updated board from the server
								// gets the updated board from the playerhandler
								out.println(protocols.GET_BOARD);
								break;
							case protocols.HAND_CHANGED://hand sent from server
								out.println(protocols.GET_HAND);
								break;
							case protocols.UPDATE_SCORE://score sent from server
								out.println(protocols.GET_SCORE);
								break;

							case protocols.UPDATE_TURN://turn sent from server
								out.println(protocols.GET_TURN);
								break;

							// protocol: playerHandler -> model
							case protocols.NEW_GAME: // playerHandler sends the player's hand
								this.updateHand(addMsg);
								this.setGameStarted();
								break;
							case protocols.NEW_PLAYER:
								this.numPlayers= Integer.parseInt(addMsg);
								setChanged();
								notifyObservers(protocols.NEW_PLAYER);
								break;
							case protocols.END_GAME://message for game end sent from server
								//exit = true;
								String [] finalScores = new String[messages.length - 1];
								System.arraycopy(messages, 1, finalScores, 0, messages.length - 1);
								this.updateScore(finalScores);
								System.out.println("msg from server in end_game: " + Arrays.toString(finalScores));
								setChanged();
								notifyObservers(protocols.END_GAME);
								break;

								case protocols.GET_SCORE://message containing score from server
								String[] scores = new String[messages.length - 1];
								// Concatenate the strings
								System.arraycopy(messages, 1, scores, 0, messages.length - 1);
								this.updateScore(scores);
								break;
							case protocols.GET_HAND://message containing hand from server
								this.updateHand(addMsg);
								break;
							case protocols.GET_BOARD://message containing board from server
								this.updateBoard(addMsg);
								break;
							case protocols.GET_TURN:
								this.turn = Integer.parseInt(addMsg);
								System.out.println("msg from server in get_turn: " + turn);
								setChanged();
								notifyObservers(protocols.GET_TURN);
								break;
							case protocols.GET_LAST_SCORE:
								this.lastScore = Integer.parseInt(addMsg);
								System.out.println("msg from server in get_last_score: " + lastScore);
								endTurn();
								break;
							case protocols.PASS:
								setChanged();
								notifyObservers(protocols.PASS);
								break;

						}

					} catch (IOException e) {
						System.out.println("execption: " + e);
						throw new RuntimeException(e);
					}
				}
			});
			getMsgFromServer.start();
		} catch (IOException e) {
			System.out.println(e);
			System.out.println("Server is not running");

		}


	}

	// update methods that change the view
	private void updateBoard(String msg) {
		// msg is 15 strings, each string is 15 characters combined into one string
		// we translate it to a 2d array of 15X15
		System.out.println("[Update Board]: " + msg);
		updatedBoard = new String[15][15];
		for(int i=0; i<15; i++)
		{
			for(int j=0; j<15; j++)
			{
				//System.out.println("[Update Board]: "+msg.substring(i*15+j, i*15+j+1));
				updatedBoard[i][j] = msg.substring(i*15+j, i*15+j+1);
			}
		}
		setChanged();
		notifyObservers(protocols.BOARD_CHANGED);

	}
	private void updateHand(String msg) {
		// example: "a;b;c;d;e;f;g"
		letterList=msg;
		setChanged();
		notifyObservers(protocols.GET_HAND);
	}
	public void updateScore(String[] msg) {
		// example: "1,3,5,13"
		this.scores = "";
		//String[] score = msg.split(",");
		for(int i=0; i < msg.length; i++)
		{
			System.out.println("score: " + msg[i]);
			this.scores+="Player " + i + ": " + msg[i] + "\n";
		}
		setChanged();
		notifyObservers(protocols.GET_SCORE);
	}
	public String getScore()
	{
		return this.scores;
	}

	public void setGameStarted() {
		setChanged();
		notifyObservers(protocols.START_GAME);
		setChanged();
		notifyObservers(protocols.GET_TURN);

	}




//	public void addLetter(CharacterData cd) {
//		characterList.stream()
//				.filter(c -> c.compareIndex(cd))
//				.findFirst()
//				.ifPresentOrElse(
//						existingCharacter -> existingCharacter.setLetter(cd.getLetter()),
//						() -> characterList.add(cd)
//				);
//	}
	public void addLetter(CharacterData cd) {
		characterList.stream()
				.filter(c -> c.compareIndex(cd))
				.findFirst()
				.ifPresent(existingCharacter -> existingCharacter.setLetter(cd.getLetter()));

		// If the Optional is empty, add the character data to the list
		if (!characterList.stream().anyMatch(c -> c.compareIndex(cd))) {
			characterList.add(cd);
		}
	}

	public void letterSelected(char letter, int row, int col) {
		addLetter(new CharacterData(letter, row, col));
		System.out.println(characterList);
		this.letter = letter;
		if(this.rowCur == -1 && this.colCur == -1) {
			this.rowCur = row;
			this.colCur = col;
			setChanged();
			notifyObservers("clear");
		}
		setChanged();
		notifyObservers(this.letter);
	}

	public void confirmSelected() {
		// need to check if game is over
		this.confirm = "confirmed";
		this.rowCur = -1;
		this.colCur = -1;
		setChanged();
		notifyObservers(this.confirm);
	}
	public void passSelected() {
		// need to check if game is over
		out.println(protocols.PASS);
//		setChanged();
//		notifyObservers("pass");
	}
	public void challengeSelected() {
		out.println(protocols.CHALLENGE);
		setChanged();
		notifyObservers(protocols.CHALLENGE);
	}

	public String getHelp() {
		return help;
	}
	public char getLetter() {
		return letter;
	}
	public String getConfirm() {
		return confirm;
	}
	public String getRow() {
		if(characterList.size() != 0) {
			return String.valueOf(characterList.get(0).getRow());
		}
		return "";
	}
	public String getCol() {
		if(characterList.size() != 0) {
			return String.valueOf(characterList.get(0).getColumn());
		}
		return "";
	}
	public byte[][] getBonus() {
		return this.bonus;
//		return gameManager.getBonusBoard();
	}

	public void cleanList() {
		characterList.clear();
		wordTiles.clear();
		//gameManager.refillHand();
	}


	public void undoSelected() {
		if(characterList.size() > 0) {
			undoLetter = characterList.get(characterList.size() - 1);
			characterList.remove(characterList.size() - 1);
		}
		else {
			undoLetter = null;
		}
		setChanged();
		notifyObservers("undo");
	}


	public void tryPlaceWord() {
		// transform the word from characterList to actual word
		wordSelected = "";
		System.out.println(" characterList: " + characterList.toString());
		out.println(protocols.GET_TURN);

		if (characterList.size()==0)
			this.isWordSelected = false;

		// checck if the list is horizontal or vertical
		// then add the letters to the wordSelected
		if(getWordDirection().equals("horizontal"))
		{
			this.isWordSelected = true;

			for (int i=0; i<characterList.size()-1; i++) {
				CharacterData ch = characterList.get(i); // get the i item in the list
				CharacterData ch1 = characterList.get(i + 1); // get the i+1 item in the list
				Tile t = new Tile(ch.getLetter(), letterScores.get(ch.getLetter()));
				wordTiles.add(t);
				wordSelected += String.valueOf(ch.getLetter());
				if (ch.getColumn() + 1 != ch1.getColumn()) {
					int numOfNulls = ch1.getColumn() - ch.getColumn() - 1;
					for (int j = 0; j < numOfNulls; j++)
						wordTiles.add(null); // means that there is an already existing letter in the board in the word
				}
			}
			CharacterData ch = characterList.get(characterList.size()-1); // get the last item in the list
			Tile t = new Tile(ch.getLetter(), letterScores.get(ch.getLetter()));
			wordTiles.add(t);
			wordSelected += String.valueOf(ch.getLetter());

		}
		else if(getWordDirection().equals("vertical"))
		{
			this.isWordSelected = true;

			for (int i=0; i<characterList.size()-1; i++)
			{
				CharacterData ch = characterList.get(i); // get the i item in the list
				CharacterData ch1 = characterList.get(i+1); // get the i+1 item in the list
				Tile t = new Tile(ch.getLetter(), letterScores.get(ch.getLetter()));
				wordTiles.add(t);
				wordSelected += String.valueOf(ch.getLetter());
				if (ch.getRow()+1 != ch1.getRow())
				{
					int numOfNulls = ch1.getRow() - ch.getRow() - 1;
					for (int j=0; j<numOfNulls; j++)
						wordTiles.add(null); // means that there is an already existing letter in the board in the word
				}
			}
			CharacterData ch = characterList.get(characterList.size()-1); // get the last item in the list
			Tile t = new Tile(ch.getLetter(), letterScores.get(ch.getLetter()));
			wordTiles.add(t);
			wordSelected += String.valueOf(ch.getLetter());
		}
		else{
			// not continuous word
			this.isWordSelected = false;

		}

		System.out.println("wordTiles: "+wordTiles);
		// need to make a tile array from the wordTiles
		Tile[] array = new Tile[wordTiles.size()];
		wordTiles.toArray(array);
		Word word = new Word(array, characterList.get(0).getRow(), characterList.get(0).getColumn(), isVerticalWord(characterList));
		System.out.println("word: " + word);
		//int score =gameManager.placeWord(word);// if score is 0 then the word is not valid
		// send a msg to playerHandler to place the word on the board
		// if score is 0 then the word is not valid
		out.println(protocols.PLACE_WORD);
		out.println(word);
	}

	public String endTurn()
	{
		if(this.lastScore==0 || !this.isWordSelected || this.turn!=this.id)
		{
			this.isWordSelected = false;
			for(int i=0; i<wordSelected.length(); i++)
			{
				setChanged();
				notifyObservers("undo");
			}
		}
		else {
			out.println(protocols.GET_HAND);
			setChanged();
			notifyObservers(protocols.GET_TURN);

		}
		wordSelected = "";

		return wordSelected;
	}


	public void restart() {
		System.out.println("New Game");
		characterList.clear();
		wordSelected="";
		rowCur=-1; colCur=-1;
		gameManager.restartGame();
		setChanged();
		notifyObservers("restart");
	}

	private static boolean isHorizontalWord(ArrayList<CharacterData> characterList) {
		// return true if the word is horizontal
		characterList.sort(Comparator.comparing(CharacterData::getColumn));
		int row = characterList.get(0).getRow();
		for (CharacterData data : characterList) {
			if (data.getRow() != row) {
				return false;
			}
		}
		return true;
	}

	private static boolean isVerticalWord(ArrayList<CharacterData> characterList) {
		// return true if the word is vertical
		characterList.sort(Comparator.comparing(CharacterData::getRow));
		int column = characterList.get(0).getColumn();
		for (CharacterData data : characterList) {
			if (data.getColumn() != column) {
				return false;
			}
		}
		return true;
	}



	public String getWordDirection() {
			// return the direction of the word
			if(isHorizontalWord(characterList)) {
					return "horizontal";

			} else if(isVerticalWord(characterList)) {
					return "vertical";

			}
		return "Illegal";

	}


//	public Player getCurrentPlayer() {
//		return gameManager.getCurrentPlayer();
//	}

	public String getTilesLeft() {
		return "Tiles left in the bag: "+this.numTilesLeft;
	}
	public String getPlayerScore() {
		return this.scores;
	}

	public String getTurn() {
		//out.println(protocols.GET_CURRENT_PLAYER); // model will get current's player id
		return "Player "+this.turn + "'s turn";
	}
	private void assignLetterScores() {
		letterScores.put('A', 1);
		letterScores.put('B', 3);
		letterScores.put('C', 3);
		letterScores.put('D', 2);
		letterScores.put('E',1);
		letterScores.put('F',4);
		letterScores.put('G',2);
		letterScores.put('H',4);
		letterScores.put('I',1);
		letterScores.put('J',8);
		letterScores.put('K',5);
		letterScores.put('L',1);
		letterScores.put('M',3);
		letterScores.put('N',1);
		letterScores.put('O',1);
		letterScores.put('P',3);
		letterScores.put('Q',10);
		letterScores.put('R',1);
		letterScores.put('S',1);
		letterScores.put('T',1);
		letterScores.put('U',1);
		letterScores.put('V',4);
		letterScores.put('W',4);
		letterScores.put('X',8);
		letterScores.put('Y',4);
		letterScores.put('Z',10);
	}

	public String getNumPlayers() {
//		gameManager = GameManager.get();
//		return ""+gameManager.getNumPlayers();
		return ""+this.numPlayers;
	}

	public List<String> getLetterList() {

		return new ArrayList<>(Arrays.asList(letterList.split(";")));

	}

	public String[][] getUpdateBoard() {
		return this.updatedBoard;
	}

	public void serverSendMessagesToAllClients(String msg) {
		out.println(protocols.SERVER_SEND_MSG+","+msg);

}

	public String getWordSelected() {
		if(!this.isWordSelected || this.lastScore==0 || this.turn!=this.id)
		{
			return "";
		}
		return this.wordSelected;
	}

	public CharacterData getUndoLetter() {

		return this.undoLetter;
	}
	public ArrayList<CharacterData> getCharacterList() {
		return this.characterList;
	}

	public boolean isPlayerTurn() {
		return this.turn==this.id;
	}
	public String getId() {
		return "You Are Player "+this.id;
	}

	public void saveGame()
	{
		out.println(protocols.SAVE_GAME);

	}

	public void loadGame() {

		out.println(protocols.LOAD_GAME);

	}
}
