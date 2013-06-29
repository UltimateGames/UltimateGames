package me.ampayne2.UltimateGames.Arenas;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Games.Game;

public class ArenaManager {
	
	private UltimateGames ultimateGames;
	private HashMap<Game, ArrayList<Arena>> arenas;
	
	public ArenaManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		arenas = new HashMap<Game, ArrayList<Arena>>();
		FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
		for (String gameKey : arenaConfig.getConfigurationSection("Arenas").getKeys(false)) {
			if (ultimateGames.getGameManager().gameExists(gameKey)) {
				for (String arenaKey : arenaConfig.getConfigurationSection("Arenas." + gameKey).getKeys(false)) {
					if (!arenaExists(arenaKey, gameKey)) {
						Arena arena = new Arena(ultimateGames.getGameManager().getGame(gameKey), arenaKey, arenaConfig.getInt("Arenas."+gameKey+"."+arenaKey+".MaxPlayers"));
						arena.setStatus(ArenaStatus.valueOf(arenaConfig.getString("Arenas."+gameKey+"."+arenaKey+".Status")));
						addArena(arena);
					}
				}
			}
		}
	}
	
	public boolean arenaExists(String arenaName, String gameName) {
		if (ultimateGames.getGameManager().gameExists(gameName) && arenas.containsKey(ultimateGames.getGameManager().getGame(gameName))) {
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
			for (Arena arena : arenas.get(ultimateGames.getGameManager().getGame(gameName))) {
				if (arenaName.equals(arena.getName())) {
					return arena;
				}
			}
		}
		return null;
	}
	
	public ArrayList<Arena> getArenasOfGame(String gameName) {
		if (ultimateGames.getGameManager().gameExists(gameName) && arenas.containsKey(ultimateGames.getGameManager().getGame(gameName))) {
			return arenas.get(ultimateGames.getGameManager().getGame(gameName));
		} else {
			return null;
		}
	}
	
	public void addArena(Arena arena) {
		if (arenas.containsKey(arena.getGame())) {
			arenas.get(arena.getGame()).add(arena);
		} else {
			ArrayList<Arena> gameArenas = new ArrayList<Arena>();
			gameArenas.add(arena);
			arenas.put(arena.getGame(), gameArenas);
		}
	}
	
}
