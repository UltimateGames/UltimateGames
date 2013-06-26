package me.ampayne2.UltimateGames;

import me.ampayne2.UltimateGames.Games.Game;


public class GameManager{
	
	private UltimateGames ultimateGames;
	
	public GameManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	public boolean gameExists(String gameName) {
		//to be replaced with actual gameExists code
		return true;
	}
	
	public Game getGame(String gameName) {
		//to be replaced with actual getGame code
		return new Game();
	}
	
}
