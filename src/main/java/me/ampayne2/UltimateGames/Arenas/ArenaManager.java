package me.ampayne2.UltimateGames.Arenas;

import java.util.ArrayList;
import java.util.HashMap;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Games.Game;

public class ArenaManager {
	
	private UltimateGames ultimateGames;
	private HashMap<Game, ArrayList<Arena>> arenas;
	
	public ArenaManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		arenas = new HashMap<Game, ArrayList<Arena>>();
	}
	
	public boolean arenaExists(String arenaName, String gameName) {
		if (ultimateGames.getGameManager().gameExists(gameName)) {
			ArrayList<Arena> gameArenas = arenas.get(ultimateGames.getGameManager().getGame(gameName));
			for (Arena arena : gameArenas) {
				if (arenaName.equals(arena.getName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Arena getArena(String arenaName, String gameName) {
		if (ultimateGames.getGameManager().gameExists(gameName)) {
			ArrayList<Arena> gameArenas = arenas.get(ultimateGames.getGameManager().getGame(gameName));
			for (Arena arena : gameArenas) {
				if (arenaName.equals(arena.getName())) {
					return arena;
				}
			}
		}
		return null;
	}
	
	public void addArena(Arena arena) {
		if (arenas.containsKey(arena.getGame())) {
			ArrayList<Arena> gameArenas = arenas.get(arena.getGame());
			for (Arena gameArena : gameArenas) {
				if (arena.getName().equals(gameArena.getName())) {
					return;
				}
			}
			gameArenas.add(arena);
		}
	}
	
}
