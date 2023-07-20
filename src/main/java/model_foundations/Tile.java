package model_foundations;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

public class Tile implements Serializable {
	private static final long serialVersionUID = 1L;

	public final char letter;
	public final int score;

	public Tile(char letter, int score) {
		super();

		this.letter = letter;
		this.score = score;
	}


	@Override
	public int hashCode() {
		return Objects.hash(letter, score);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		return letter == other.letter && score == other.score;
	}

	public String toString() {
		return String.valueOf(letter);
	}
	
	public static class Bag{
		private int[] maxQuantities = {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
		private int[] quantities = maxQuantities.clone();
		private Tile[] tiles = {
				new Tile('A',1),	
				new Tile('B',3),	
				new Tile('C',3),	
				new Tile('D',2),	
				new Tile('E',1),	
				new Tile('F',4),	
				new Tile('G',2),	
				new Tile('H',4),	
				new Tile('I',1),	
				new Tile('J',8),	
				new Tile('K',5),	
				new Tile('L',1),	
				new Tile('M',3),	
				new Tile('N',1),	
				new Tile('O',1),	
				new Tile('P',3),	
				new Tile('Q',10),	
				new Tile('R',1),	
				new Tile('S',1),	
				new Tile('T',1),	
				new Tile('U',1),	
				new Tile('V',4),	
				new Tile('W',4),	
				new Tile('X',8),	
				new Tile('Y',4),	
				new Tile('Z',10)	
		};
		
		Random r;
		int size;
		
		public Bag() {
			r = new Random();
			size=98;
		}
		
		public Tile getRand() {
			if(size>0) {
				int i=r.nextInt(quantities.length);
				while(quantities[i]==0)
					i=r.nextInt(quantities.length);
				size-=1;
				quantities[i]-=1;
				return tiles[i];
			}
			return null;
		}
		
		public Tile getTile(char c) {
			if(c>='A' && c<='Z' && quantities[c-'A']>0) {
				quantities[c-'A']-=1;
				size-=1;
				//System.out.println("getting "+c+" - "+quantities[c-'A']);
				return tiles[c-'A'];
			}
			return null;
		}
		
		public int size() {
			return size;
		}
		
		public void put(Tile t) {
			int i=t.letter-'A';
			if(quantities[i]<maxQuantities[i])
				quantities[i]+=1;
		}
		
		public int[] getQuantities() {
			return quantities.clone();
		}

		public String toString() {
			String s="";
			for(int i=0;i<quantities.length;i++)
			{
				for(int j=0;j<quantities[i];j++)
				{
					s+=tiles[i].letter;
				}
			}
			return s;
		}
		
	}
	
}
