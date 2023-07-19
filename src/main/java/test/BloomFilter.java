package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class BloomFilter {
	
	private String[] algs;
	BitSet bs;
	final private int size;

	public BloomFilter(int size, String...algs) {
		this.algs=algs;
		bs=new BitSet(size);
		this.size=size;
	}
	
	
	public void add(String word) {
		for(String alg : algs)
			try {
				MessageDigest md=MessageDigest.getInstance(alg);
				byte[] bts=md.digest(word.getBytes());
				BigInteger bi=new BigInteger(bts);
				int i = Math.abs(bi.intValue())%size;
				bs.set(i);
			} catch (NoSuchAlgorithmException e) {}
	}
	
	public boolean contains(String word) {
		for(String alg : algs)
			try {
				MessageDigest md=MessageDigest.getInstance(alg);
				byte[] bts=md.digest(word.getBytes());
				BigInteger bi=new BigInteger(bts);
				int i = Math.abs(bi.intValue())%size;
				if(!bs.get(i))
					return false;
			} catch (NoSuchAlgorithmException e) {}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<bs.length();i++) 
			sb.append(bs.get(i)?"1":"0");
		return sb.toString();
	}
	
	


}
