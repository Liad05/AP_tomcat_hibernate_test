package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DictionaryManager {

    private static DictionaryManager instance;
    private Map<String, Dictionary> bookToDictionaryMap;
    ExecutorService es;	

    private DictionaryManager() {
        bookToDictionaryMap = new HashMap<>();
        es = Executors.newCachedThreadPool();
    }

    // Singleton get() method
    public static DictionaryManager get() {
        if (instance == null) {
            instance = new DictionaryManager();
        }
        return instance;
    }

    public int getSize() {
        return bookToDictionaryMap.size();
    }

    public boolean query(String... args) {
        String word = args[args.length - 1];
        String[] books = Arrays.copyOfRange(args, 0, args.length - 1);
		

        Boolean res = false;
        for (String book : books) 
        {
        	if (bookToDictionaryMap.get(book) == null) 
        	{
        		Dictionary newdictionary = new Dictionary(book);
        		bookToDictionaryMap.put(book,newdictionary);
        	}
            if (bookToDictionaryMap.get(book).query(word)) {
                res = true;
                //System.out.println("Word found in: " + book);
            }

        }
        return res;
    }

    public boolean challenge(String... args) {
        // bloom filter can only return false positive but not false negative
        // a query returns either "possibly in set" or "definitely not in set"
        // so if a player drops a word, the next player can challenge it
        System.out.println("Challenge: " + Arrays.toString(args));
    	ArrayList<Future<Boolean>> fs=new ArrayList<>();//for the results from IOSearchers
        String fullWord = args[args.length - 1];
        String[] temp = fullWord.split(",");
        String word = temp[0];
        String[] books = Arrays.copyOfRange(args, 0, args.length - 1);
        Boolean res = false;
        for (String book : books) {
        	fs.add(es.submit(()->{//searching every dictionary in order to update it				
                return bookToDictionaryMap.get(book).challenge(word.toUpperCase());
    		}));
        }
        
        for(Future<Boolean> f : fs) {//checks if found
			try {
				res|=f.get();
			} catch (InterruptedException | ExecutionException e) {
                System.out.println("Error in challenge: " + e.getMessage());
            }
		}
        //restarts es for the next challenge
        es.shutdown();
        es = Executors.newCachedThreadPool();
        return res;
    }
    
    @Override
	public void finalize() {//activated by the garbage collector
		es.shutdown();
	}
    
    
   

}
