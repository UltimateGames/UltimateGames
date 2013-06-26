package me.ampayne2.UltimateGames;

import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Games.Arena;
import me.ampayne2.UltimateGames.Games.Game;

public class ArenaManager {
	
	private UltimateGames ultimateGames;
	
	public ArenaManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	public boolean arenaExists(String arenaName) {
		//to be replaced with actual arenaExists code
		return true;
	}
	
	public Arena getArena(String arenaName, String gameName) {
		//will return the arena named arenaName and with the game named gameName
		Arena arena = new Arena(new Game(), arenaName);
		arena.setStatus(ArenaStatus.OPEN);
		return arena;
	}
	
}
