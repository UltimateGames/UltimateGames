package me.ampayne2.UltimateGames.Games;

import java.util.HashMap;

public class GameScoreContainer {
	private HashMap<String, Integer> integerScores;
	private HashMap<String, Boolean> booleanScores;
	private HashMap<String, Integer> integerSecondaryScores;
	private HashMap<String, Boolean> booleanSecondaryScores;
	
	public GameScoreContainer() {
		
	}
	
	public HashMap<String, Integer> getIntegerScores() {
		return integerScores;
	}
	
	public HashMap<String, Boolean> getBooleanScores() {
		return booleanScores;
	}
	
	public HashMap<String, Integer> getIntegerSecondaryScores() {
		return integerSecondaryScores;
	}
	
	public HashMap<String, Boolean> getBooleanSecondaryScores() {
		return booleanSecondaryScores;
	}
	
	public void setIntegerScores(HashMap<String, Integer> scores) {
		integerScores = scores;
	}
	
	public void setBooleanScores(HashMap<String, Boolean> scores) {
		booleanScores = scores;
	}
	
	public void setIntegerSecondaryScores(HashMap<String, Integer> scores) {
		integerSecondaryScores = scores;
	}
	
	public void setBooleanSecondaryScores(HashMap<String, Boolean> scores) {
		booleanSecondaryScores = scores;
	}
}
