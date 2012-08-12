package com.cushon.Hangman;

import java.util.ArrayList;
import java.util.List;

public class HangmanClient {
	
	/**
	 * Test method. Given a populated hangman game, tests the benevolent robot code.
	 * 
	 * @param start is a populated hangman game.
	 */
	public static int trialRun(String start) {
		start = start.toLowerCase();
		BenevolentRobot r = new BenevolentRobot();
		
		String state = start.replaceAll("[a-z]", "_");
		int tries = 0;
		while(!state.equals(start)) {
			char g = r.NextGuess(state);
			if(start.indexOf(g) > -1) {
				System.out.println(state + " : " + g + ", " + true);
				r.GuessResult(g, true);
				char[] s = start.toCharArray();
				char[] m = state.toCharArray();
				for(int i = 0; i < s.length; ++i) {
					if(s[i] == g) {
						m[i] = g;
					}
				}
				state = new String(m);
			} else {
				++tries;
				System.out.println(state + " : " + g + ", " + false);
				r.GuessResult(g, false);
			}
		}
		System.out.println(state);
		System.out.println(tries + " errors.");
		return tries;
	}
	
	public static void benchmark() {
		String[] captured_games = {
			"the war on the united states",
			"I SEE--HER LADYSHIP'S WAITING-MAID",
			"NEVER SAID THE DORMOUSE",
			"OF THE MIDDLE OF THE SQUARE",
			"TREACLE SAID THE CATERPILLAR CONTEMPTUOUSLY",
			"HENCE THERE WAS A FINE TIME",
			"ILLUSTRATION DINING HALL IN THE CHARTERHOUSE",
			"the war on the united states",
			"i should be deceived again",
			"there are to be avoided",
			"the king had said that day",
			"in most of the time",
			"in the reign of louis xiv",
			"but i am all off colour",
			"it would be an unpleasant thing",
			"of the middle classes fig",
			"you must not disappoint your father",
			"on sexual union chapter i",
			"certainly he replied biting his lips",
			"there ain't no sense in it",
			"the price of the yellow metal",
			"men who talk well",
			"in all approximately fifteen millions",
			"the object of open pleasantry",
			"but to restore the southern states",
			"the crown and with one another",
			"oh merely a couple of case-knives",
			"the games of strength and agility",
			"i had shut the door to",
			"elizabeth listened as little as possible",
			"the question was impossible",
			"of the same werke xiijs",
			"you have said quite enough madam",
			"as a matter of talk only",
			"the embassy to achilles",
			"and who is this k",
			"the cause of troy"};
		
		int games = 0;
		int error_sum = 0;
		int wins = 0;
		List<Integer> errors = new ArrayList<Integer>();
		for(String s : captured_games) {
			int e = trialRun(s);
			errors.add(e);
			++games;
			error_sum += e;
			if(e <= 3) ++wins;
		}
		System.out.println("Won " + wins  + " of " + games  + " games." + error_sum  + " total errors.");
		for(int i : errors) {
			System.out.println(i);
		}
	}
	
	public static void main(String[] args) throws Exception {
		//benchmark();
	
		//trialRun("cnidaria");
		
		BenevolentRobot r = new BenevolentRobot();
		HangmanGame gameState = JsonUtils.newGame();
		int errors = 0;
		
		while(gameState.status == HangmanGame.GameStatus.ALIVE) 
		{
			System.out.println(gameState.state);
			char g = r.NextGuess(gameState.state);
			
			HangmanGame newState = JsonUtils.makeGuess(gameState, g);
			
			if(!gameState.state.equals(newState.state)) {
				r.GuessResult(g, true);
				System.out.println(g + ", " + true);
			} else {
				r.GuessResult(g, false);
				System.out.println(g + ", " + false);
				++errors;
			}
			gameState = newState;
		}
		switch(gameState.status) {
			case DEAD:
				System.out.println(gameState.state);
				System.out.println("The prisoner has been killed by evil robots.");
				break;
			case FREE:
				System.out.println(gameState.state);
				System.out.println("Success with " + errors + " error(s).");
				break;
		}
		
	}
}

