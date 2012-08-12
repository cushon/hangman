package com.cushon.Hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BenevolentRobot {
	private Map<String, Integer> freqMap;
	private Map<Integer, List<String>> wordMap;
	
	// Characters that have been rejected.
	Set<Character> exclude = new HashSet<Character>();
	
	// Characters that have been used successfully.
	Set<Character> include = new HashSet<Character>();
	
	/**
	 * @param state is the serialised representation of partially filled in words,
	 * as used by the JSON API.
	 * @return the next character to try.
	 */
	public char NextGuess(String state) {
		/*
		 * Idea: consider every english word that is the length of one of the words
		 * in the game, contains every letter that we know is in that word at the 
		 * correct position, and contains no letters that have been disqualified.
		 * 
		 * For all of these words, record every letter that occurs in them, weighted
		 * by (the frequency of the word / the number of words of that length).
		 * 
		 */
		
		// Parse the game state into a list of words
		List<String> wordList = new ArrayList<String>(
				Arrays.asList(state.split("[^a-z_']+")));
		
		// Build a string containing all of the previously guessed characters
		StringBuilder excludeString = new StringBuilder();
		Iterator<Character> it = exclude.iterator();
		while(it.hasNext()) excludeString.append(it.next());
		
		// Record a weight for each character
		double[] freq = new double[26];
		
		for(String s : wordList) {
			if(!s.contains("_")) continue;
			
			// If the word has a '-' character, match for non-excluded
			// characters only.
			Pattern regex = Pattern.compile(s.replace("_", 
						(excludeString.length() > 0) ?
								String.format("[a-z&&[^%s]]", excludeString)
								: "[a-z]"));
			
			List<String> cands = new ArrayList<String>();
			if(wordMap.containsKey(s.length())) {
		        for(String cand : wordMap.get(s.length())) {
		        	Matcher m = regex.matcher(cand);
		    		if(m.find()) cands.add(cand);
		        }
			}
	        
	        // Build character frequencies for the set of candidates for the current word
	        double[] thisfreq = new double[26];
	        for(String cand : cands) {
	        	int freqValue = (freqMap.containsKey(cand)) ? freqMap.get(cand) : 1;
    			for(int i = 0; i < cand.length(); ++i) {
    				int index = cand.charAt(i) - 'a';
    				// disregard non alphabetic characters ('\'', etc.)
    				if(index < 0 || index >= freq.length) continue;
					thisfreq[index] += freqValue; // Weight by word frequency
				}
	        } 
	        for(int i = 0; i < thisfreq.length; ++i) {
		        // Normalize the character weights by the number of candidates for this word
	        	thisfreq[i] /= cands.size();
	        	// Accumulate this word's results in the global frequency array
	        	freq[i] += thisfreq[i];
	        }
		}
		
		char maxc = 'a';
		double maxi = 0;
		for(int i = 0; i < freq.length; ++i) {			
			if(freq[i] > maxi) {
				char newc = (char)((int)'a' + i);
				if(include.contains(newc) || exclude.contains(newc)) continue;
				maxi = freq[i];
				maxc = newc;
			}
		}
		
		if(maxi == 0) {
			//no characters found more than 0 times => no candidate words found,
			System.out.println("Reduced accuracy!");
			for(char c = 'a'; c <= 'z'; ++c) {
				if(!(include.contains(c) || exclude.contains(c))) {
					return c;
				}
			}
		}
		
		return maxc;
	}
	
	public void GuessResult(char guess, boolean success) {
		// If I'm being paid money to write software, I use the ternary
		// operator less.
		(success ? include : exclude).add(guess);
	}
	
	BenevolentRobot() {
		// On construction, load an english dictionary and word frequency list.
		// Dictionary source: SCOWL (wordlist.sourceforge.net)
		// Frequency count: BNC Frequency list (http://www.kilgarriff.co.uk/bnc-readme.html)

		// Read in (frequency, word) pairs, hash them by word.
		freqMap = new HashMap<String, Integer>();
		for(File f : new File("resource/freq/").listFiles()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(f));
				for(String str; (str = in.readLine()) != null;) {
					String[] tokens = str.split("\\s+");
					assert(tokens.length > 2);
					freqMap.put(tokens[1], Integer.parseInt(tokens[0]) + 
							Utils.coalesce(freqMap.get(tokens[1]), 0));
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		// Read in words, hash them by length.
		wordMap = new HashMap<Integer, List<String>>();
        for(File f : new File("resource/words/").listFiles()) {
        	BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(f));
				for(String str; (str = in.readLine()) != null;) {
					str = str.trim();
					if(!wordMap.containsKey(str.length())) {
						wordMap.put(str.length(), new ArrayList<String>());	
					}
					wordMap.get(str.length()).add(str);
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}
