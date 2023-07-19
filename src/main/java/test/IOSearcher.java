package test;

import java.io.File;
import java.util.Scanner;

interface FileSearcher {
	public boolean search(String word, String...fileNames);	
	public void stop();

}

public class IOSearcher implements FileSearcher{

	boolean stopMe;
	
	public IOSearcher() {
		stopMe=false;
	}
	
	public boolean search(String word, String...fileNames){
		boolean found=false;
		try {
			for(int i=0;!stopMe && i<fileNames.length && !found; i++) {
				Scanner s=new Scanner(new File("text_files/"+fileNames[i]));
				while(s.hasNext() && !found && !stopMe)
				{
//					System.out.println("searching "+fileNames[i]);
//					System.out.println(s.next().toUpperCase()+","+word);

					if(s.next().toUpperCase().equals(word))
						found=true;
				}

				s.close();
			}
		}catch(Exception e) {
			System.out.println("cannot open book");
		}
		
		return found;
	}
	
	public void stop() {
		stopMe=true;
	}
}
