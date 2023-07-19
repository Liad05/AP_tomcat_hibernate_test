package test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Word implements Serializable {
	private static final long serialVersionUID = 1L;

	private Tile[] tiles;
	private int row,col;
	private boolean vertical;
	
	public Word(Tile[] tiles, int row, int col, boolean vertical) {
		super();
		this.tiles = tiles;
		this.row = row;
		this.col = col;
		this.vertical = vertical;
	}
	public Word(Word w)
	{
		this.tiles = w.tiles;
		this.row = w.row;
		this.col = w.col;
		this.vertical = w.vertical;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isVertical() {
		return vertical;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(tiles);
		result = prime * result + Objects.hash(col, row, vertical);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		return col == other.col && row == other.row && Arrays.equals(tiles, other.tiles) && vertical == other.vertical;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(Tile t : tiles)
			if (t!=null)
				sb.append(t.letter);
			else
				sb.append("_");
		sb.append(",").append(row).append(",").append(col).append(",").append(vertical);
		return sb.toString();
	}
	
}
