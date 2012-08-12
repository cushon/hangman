package com.cushon.Hangman;

public class Utils {
	// C# makes this rather nicer.
	// reference: http://stackoverflow.com/questions/2768054/
	public static <T> T coalesce(T ...items) {
		for(T i : items) if(i != null) return i;
	    return null;
	}
}
