package model_foundations;

public interface CacheReplacementPolicy{
	void add(String word);
	String remove(); 
}
