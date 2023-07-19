package test;

import java.util.HashMap;
import java.util.PriorityQueue;

public class LFU implements CacheReplacementPolicy{


	HashMap<String, Integer> map=new HashMap<String, Integer>();
	
	PriorityQueue<String> q=new PriorityQueue<>((s1,s2)->{
		return map.get(s1).intValue() - map.get(s2).intValue();
	});
	
	@Override
	public void add(String word) {
		if(map.containsKey(word)) {
			int hits = map.get(word) +1 ;
			map.put(word, hits );
			q.remove(word);
			q.add(word);
		}else {
			map.put(word, 1);
			q.add(word);
		}
	}

	@Override
	public String remove() {
		String r = q.poll();
		map.remove(r);
		return r;
	}
}
