package me.ampayne2.UltimateGames;

import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Enums.PlayerType;
import me.ampayne2.UltimateGames.Games.Arena;
import me.ampayne2.UltimateGames.Games.Game;
import me.ampayne2.UltimateGames.Games.GameDescription;

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
		//will return the arena named arenaName, with the game named gameName.
		//temporarily returns a new arena with a blank game named gameName, blank arena named arenaName, and maxPlayers of 8
		Game game = new Game();
		GameDescription gameDescription = new GameDescription(gameName, null, null, null, null, null, null, null, null, PlayerType.CONFIGUREABLE);
		game.setGameDescription(gameDescription);
		Arena arena = new Arena(game, arenaName, 8);
		arena.setStatus(ArenaStatus.OPEN);
		return arena;
	}
	
}
