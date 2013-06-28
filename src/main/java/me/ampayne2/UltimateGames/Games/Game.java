package me.ampayne2.UltimateGames.Games;

import java.io.File;

public class Game {
	
	private File file;
	private GameDescription gameDescription;
	
	public Game(File file, GameDescription gameDescription) {
		this.file = file;
		this.gameDescription = gameDescription;
	}
	
	public File getFile() {
		return file;
	}
	
	public GameDescription getGameDescription() {
		return gameDescription;
	}
	
}
