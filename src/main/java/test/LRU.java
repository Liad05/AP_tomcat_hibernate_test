package test;

import java.util.LinkedList;
import java.util.List;

public class LRU implements CacheReplacementPolicy{

	List<String> list=new LinkedList<>();

	@Override
	public void add(String word) {
		list.remove(word);
		list.add(word);
	}

	@Override
	public String remove() {
		return list.remove(0);
	}

}
