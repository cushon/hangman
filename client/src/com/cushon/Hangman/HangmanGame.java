package com.cushon.Hangman;

public class HangmanGame {
	public enum GameStatus { ALIVE, DEAD, FREE }
	
	public GameStatus status;
	public String token;
	public int remainingGuesses;
	public String state;
}