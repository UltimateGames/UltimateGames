package me.ampayne2.UltimateGames.Games;

import java.util.ArrayList;

import me.ampayne2.UltimateGames.UltimateGames;

public class GameManager {
	
	private UltimateGames ultimateGames;
	private ArrayList<Game> games;
	
	public GameManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		games = new ArrayList<Game>();
	}
	
	public boolean gameExists(String gameName) {
		for (Game game : games) {
			if (gameName.equals(game.getGameDescription().getName())) {
				return true;
			}
		}
		return false;
	}
	
	public Game getGame(String gameName) {
		for (Game game : games) {
			if (gameName.equals(game.getGameDescription().getName())) {
				return game;
			}
		}
		return null;
	}
	
}
