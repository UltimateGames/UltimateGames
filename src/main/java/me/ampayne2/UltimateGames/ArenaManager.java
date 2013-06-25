package me.ampayne2.UltimateGames;

import me.ampayne2.UltimateGames.Games.Arena;

public class ArenaManager {
	
	private UltimateGames ultimateGames;
	
	public ArenaManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	public boolean arenaExists(String arenaName) {
		//to be replaced with actual arenaExists code
		return true;
	}
	
	public Arena getArena(String arenaName) {
		//to be replaced with actual getArena code
		return new Arena(null);
	}
	
}
