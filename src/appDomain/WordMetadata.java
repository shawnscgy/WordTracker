package appDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordMetadata implements Serializable, Comparable<WordMetadata>
{
	private static final long serialVersionUID = -3484076562779861956L;
	private String word;
	private HashMap<String, List<Integer>> occurrences;
	
	public String getWord() 
	{
		return word;
	}
	public HashMap<String, List<Integer>> getOccurrences() 
	{
		return occurrences;
	}
	public WordMetadata(String word)
	{
		this.word = word;
		this.occurrences = new HashMap<>();
	}
	public void addOccurrence(String fileName, int lineNumber)
	{
		// If key exists, process adding to the value, if not, add new key and new value 
		occurrences.computeIfAbsent(fileName, k -> new ArrayList<>()).add(lineNumber);
	}
	@Override
	public int compareTo(WordMetadata other) 
	{
		return this.word.compareToIgnoreCase(other.word);
	}
}
