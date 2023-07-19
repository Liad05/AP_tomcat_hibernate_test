package test;

import javafx.scene.canvas.Canvas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Board extends Canvas {

	final byte dl=2;	// double letter
	final byte tl=3;	// triple letter
	final byte dw=20;	// double word
	final byte tw=30;	// triple word
	
	private byte[][] bonus= {
			{tw,0,0,dl,0,0,0,tw,0,0,0,dl,0,0,tw},
			{0,dw,0,0,0,tl,0,0,0,tl,0,0,0,dw,0},
			{0,0,dw,0,0,0,dl,0,dl,0,0,0,dw,0,0},
			{dl,0,0,dw,0,0,0,dl,0,0,0,dw,0,0,dl},
			{0,0,0,0,dw,0,0,0,0,0,dw,0,0,0,0},
			{0,tl,0,0,0,tl,0,0,0,tl,0,0,0,tl,0},
			{0,0,dl,0,0,0,dl,0,dl,0,0,0,dl,0,0},			
			{tw,0,0,dl,0,0,0,dw,0,0,0,dl,0,0,tw},
			{0,0,dl,0,0,0,dl,0,dl,0,0,0,dl,0,0},
			{0,tl,0,0,0,tl,0,0,0,tl,0,0,0,tl,0},
			{0,0,0,0,dw,0,0,0,0,0,dw,0,0,0,0},
			{dl,0,0,dw,0,0,0,dl,0,0,0,dw,0,0,dl},
			{0,0,dw,0,0,0,dl,0,dl,0,0,0,dw,0,0},
			{0,dw,0,0,0,tl,0,0,0,tl,0,0,0,dw,0},	
			{tw,0,0,dl,0,0,0,tw,0,0,0,dl,0,0,tw}	
	};
	
	Tile[][] tiles;
	
	boolean isEmpty;
	private ArrayList<Word> lastWords;
	private HashMap<Character, Integer> letterScores = new HashMap<>(); // temporary saves the letter and its score


	public Board() {
		tiles=new Tile[15][15];
		isEmpty=true;
		lastWords=new ArrayList<Word>();
		this.assignLetterScores();
	}
	public Board(String board)
	{
		this();
		for(int i=0; i<15; i++)
		{
			for(int j=0; j<15; j++) {
				char c=board.charAt(i*15+j);
				if(c!='_')
				{
					isEmpty=false;
					tiles[i][j]=new Tile(c,this.letterScores.get(c));
				}

			}
		}
	}

	public Board(Board originalBoard) {
		this.isEmpty = originalBoard.isEmpty;
		this.bonus = new byte[15][15];
		for (int i = 0; i < 15; i++) {
			System.arraycopy(originalBoard.bonus[i], 0, this.bonus[i], 0, 15);
		}
		this.tiles = new Tile[15][15];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (originalBoard.tiles[i][j] != null) {
					this.tiles[i][j] = new Tile(originalBoard.tiles[i][j].letter,originalBoard.tiles[i][j].score); // Assuming the Tile class has a copy constructor
				} else {
					this.tiles[i][j] = null;
				}
			}
		}
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

	public byte[][] getBonus() {
		return bonus;
	}
	public Tile[][] getTiles() {
		return tiles.clone();
	}
	
	
	private boolean inBoard(int row,int col) {
		return (col>=0 && col<15 && row>=0 && row<15);
	}
	
	private boolean onStar(Word w) {
		int i=w.getRow(),j=w.getCol();
		for(int k=0;k<w.getTiles().length;k++) {			
			if(i==7 && j==7)
				return true;
			if(w.isVertical()) i++; else j++;
		}
		return false;
	}

	private boolean check(int i, int j)
	{
		boolean ch1=tiles[i][j]!= null;
		boolean ch2=(inBoard(i + 1, j) && tiles[i + 1][j] != null);
		boolean ch3=(inBoard(i + 1, j + 1) && tiles[i + 1][j + 1] != null);
		boolean ch4=(inBoard(i, j + 1) && tiles[i][j + 1] != null);
		boolean ch5=(inBoard(i - 1, j + 1) && tiles[i - 1][j + 1] != null);
		boolean ch6=(inBoard(i - 1, j) && tiles[i - 1][j] != null);
		boolean ch7=(inBoard(i - 1, j - 1) && tiles[i - 1][j - 1] != null);
		boolean ch8=(inBoard(i, j - 1) && tiles[i][j - 1] != null);
		boolean ch9=(inBoard(i + 1, j - 1) && tiles[i + 1][j - 1] != null);
		//System.out.println("checks:"+ch1+" "+ch2+" "+ch3+" "+ch4+" "+ch5+" "+ch6+" "+ch7+" "+ch8+" "+ch9);
//		return tiles[i][j] != null ||
//				(inBoard(i + 1, j) && tiles[i + 1][j] != null) ||
//				(inBoard(i + 1, j + 1) && tiles[i + 1][j + 1] != null) ||
//				(inBoard(i, j + 1) && tiles[i][j + 1] != null) ||
//				(inBoard(i - 1, j + 1) && tiles[i - 1][j + 1] != null) ||
//				(inBoard(i - 1, j) && tiles[i - 1][j] != null) ||
//				(inBoard(i - 1, j - 1) && tiles[i - 1][j - 1] != null) ||
//				(inBoard(i, j - 1) && tiles[i][j - 1] != null) ||
//				(inBoard(i + 1, j - 1) && tiles[i + 1][j - 1] != null);
		return ch1 || ch2 || ch3 || ch4 || ch5 || ch6 || ch7 || ch8 || ch9;
	}
	private boolean crossTile(Word w) {
		int i=w.getRow(),j=w.getCol();
		for(int k=0;k<w.getTiles().length;k++) {			
			if(check(i,j))
				return true;
			if(w.isVertical()) i++; else j++;
		}
		return false;
	}
	
	private boolean changesTile(Word w) {
		int i=w.getRow(),j=w.getCol();
		for(Tile t : w.getTiles()) {			
			if(tiles[i][j]!=null && tiles[i][j]!=t)//
			{
				System.out.println("tiles[i][j]:"+tiles[i][j]+" t:"+t);

				return true;
			}
			if(w.isVertical()) i++; else j++;
		}
		return false;
	}



	public List<String> getAllFileNames(String folderPath) {
		try {
			return Files.walk(Paths.get(folderPath))
					.filter(Files::isRegularFile)
					.map(path -> path.getFileName().toString())
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public boolean boardLegal(Word w) {
		int row=w.getRow();
		int col=w.getCol();
		
		if(!inBoard(row, col))
			return false;		
		int eCol,eRow;
		if(w.isVertical()) {
			eCol=col;
			eRow=row+w.getTiles().length-1;
//			if(w.getTiles()[w.getTiles().length-1].!=null)
//				return false;

		}else {
			eRow=row;
			eCol=col+w.getTiles().length-1;		
		}
		if(!inBoard(eRow, eCol))
		{
			System.out.println("out of board");
			return false;

		}

//		if(isEmpty && onStar(w) && !isNull(w))
//			return true;

		if(isEmpty && !onStar(w))
		{
			System.out.println("board is empty but word is not on star");
			return false;

		}

		if(!isEmpty && !crossTile(w)) //
		{
			System.out.println("board is not empty but word does not cross any tile");
			return false;

		}

		if(changesTile(w))
		{
			System.out.println("word changes tile");
			return false;

		}
		return true;
	}
	
	public boolean dictionaryLegal(Word w) {
		// query DictionaryManager
		String projectPath = System.getProperty("user.dir");
		Path folderPath = Paths.get(projectPath, "text_files");
		String searchFolderPath = folderPath.toString();
		List<String> bookNames = getAllFileNames(searchFolderPath);
		bookNames.add(w.toString());
		String[] args = bookNames.toArray(new String[0]);
		boolean found = DictionaryManager.get().query(args);
		if (found) {
			System.out.println("Word found in at least one book.");
		} else {
			System.out.println("Word not found in any book.");
		}
		return found;
	}

	public boolean challenge() {
		// challenge DictionaryManager using this.lastWord
		// return true if word not found, false if found
		if(this.lastWords==null || this.lastWords.size()==0)
			return true;
		String projectPath = System.getProperty("user.dir");
		Path folderPath = Paths.get(projectPath, "text_files");
		String searchFolderPath = folderPath.toString();
		//List<String> bookNames = getAllFileNames(searchFolderPath);
		for(int i=0;i<this.lastWords.size();i++)
		{
			List<String> bookNamesTemp = getAllFileNames(searchFolderPath);
			bookNamesTemp.add(this.lastWords.get(i).toString());
			String[] args = bookNamesTemp.toArray(new String[0]);
//			System.out.println("Challenge: "+this.lastWords.get(i).toString());
			boolean found = DictionaryManager.get().challenge(args);
			if (found) {
				System.out.println("Challenge: Word found in at least one book.");
			} else {
				System.out.println("Challenge: Not a Word in my book.");
				return true;
			}


		}
		return false;
	}

	
	private ArrayList<Word> getAllWords(Tile[][] ts){
		ArrayList<Word> ws=new ArrayList<>();
		
		// Horizontal scan
		for(int i=0;i<ts.length;i++) {
			int j=0;
			while(j<ts[i].length) {
				ArrayList<Tile> tal=new ArrayList<>();
				int row=i,col=j;
				while(j<ts[i].length && ts[i][j]!=null) {
					tal.add(ts[i][j]);
					j++;
				}
				if(tal.size()>1) {
					Tile[] tiles=new Tile[tal.size()];
					ws.add(new Word(tal.toArray(tiles), row,col,false));
				}				
				j++;
			}
		}
		
		// Vertical scan
		for(int j=0;j<ts[0].length;j++) {
			int i=0;
			while(i<ts.length) {
				ArrayList<Tile> tal=new ArrayList<>();
				int row=i,col=j;
				while(i<ts.length && ts[i][j]!=null) {
					tal.add(ts[i][j]);
					i++;
				}
				if(tal.size()>1) {
					Tile[] tiles=new Tile[tal.size()];
					ws.add(new Word(tal.toArray(tiles), row,col,true));
				}				
				i++;
			}
		}
		
		return ws;
	}
	
	public ArrayList<Word> getWords(Word w) {
		Tile[][] ts=getTiles(); // a clone...
		ArrayList<Word> before=getAllWords(ts);
		// demo placement of new word
		int row=w.getRow();
		int col=w.getCol();
		for(Tile t : w.getTiles()) {
			ts[row][col]=t;
			if(w.isVertical()) row++; else col++;
		}				
		ArrayList<Word> after=getAllWords(ts);
		after.removeAll(before); // only new words remain...
		return after;
	}
	
	public int getScore(Word w) {		
		int row=w.getRow();
		int col=w.getCol();
		int sum=0;
		int tripleWord=0,doubleWord=0;
		for(Tile t : w.getTiles()) {
			sum+=t.score;
			if(bonus[row][col] == dl)
				sum+=t.score;
			if(bonus[row][col] == tl)
				sum+=t.score*2;
			if(bonus[row][col] == dw)
				doubleWord++;
			if(bonus[row][col] == tw)
				tripleWord++;
			if(w.isVertical()) row++; else col++;			
		}
		
		if(doubleWord>0)
			sum*=(2*doubleWord);
		if(tripleWord>0)
			sum*=(3*tripleWord);
		return sum;

	}
	
	public int tryPlaceWord(Word w) {
		
		Tile[] ts = w.getTiles();
		int row=w.getRow();
		int col=w.getCol();
		for(int i=0;i<ts.length;i++) {
			if(ts[i]==null && tiles[row][col]==null)
			{
				return 0;
			}
			else if(ts[i]==null && tiles[row][col]!=null)
				ts[i]=tiles[row][col];
			if(w.isVertical()) row++; else col++;
		}
		
		Word test=new Word(ts, w.getRow(), w.getCol(), w.isVertical()); 
		
		int sum=0;				
		if(boardLegal(test) ) {
			ArrayList<Word> newWords=getWords(test);
			System.out.println("newWords: "+newWords);
			System.out.println("lastWords: "+lastWords);
			if(lastWords!=null)
				lastWords.clear();
			else {
				lastWords=new ArrayList<>();
			}

			lastWords.addAll(newWords);
			System.out.println("newWords: "+newWords);

			for(Word nw : newWords) {				
				if(dictionaryLegal(nw))
					sum+=getScore(nw);
				else
					return 0;
			}			
		}
		else{
			System.out.println("Illegal");
			return 0;

		}

		// the placement
		row=w.getRow();
		col=w.getCol();
		for(Tile t : w.getTiles()) {
			tiles[row][col]=t;
			if(w.isVertical()) row++; else col++;
		}		

		if(isEmpty) {
			isEmpty=false;
			bonus[7][7]=0;
		}
		System.out.println("Score: "+sum);
		return sum;
	}

	public void print() {
		for(Tile[] ts : tiles) {
			for(Tile t : ts) {
				if(t!=null)
					System.out.print(t.letter);
				else
					System.out.print("_");
			}
			System.out.println();
		}
	}

	public String toString() {
		String boardState = "";
		for(Tile[] ts : tiles) {
			for(Tile t : ts) {
				if(t!=null)
					boardState+=(t.letter);
				else
					boardState+=("_");
			}
			//boardState+="\n";
		}
		return boardState;
	}



}
