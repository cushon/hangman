package com.cushon.Hangman;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

public class JsonUtils {
	
	private static final String KEndpointURL = "http://localhost:8000/";

	public static final String kUserEmail = "cushon@";

	public static HangmanGame makeJsonHangmanRequest(String params)
			throws MalformedURLException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
				KEndpointURL + "?code=" + kUserEmail + params)
				.openStream()));
		StringBuilder sb = new StringBuilder();
		for (String str; ((str = in.readLine()) != null); sb.append(str))
			;
		in.close();
		HangmanGame state = new Gson().fromJson(sb.toString(), HangmanGame.class);
		state.state = state.state.toLowerCase();
		return state;
	}

	public static HangmanGame newGame() throws MalformedURLException, IOException {
		return makeJsonHangmanRequest("");
	}

	public static HangmanGame makeGuess(HangmanGame state, char guess)
			throws MalformedURLException, IOException {
		return makeJsonHangmanRequest(String.format("&token=%s&guess=%s",
				state.token, guess));
	}

}
