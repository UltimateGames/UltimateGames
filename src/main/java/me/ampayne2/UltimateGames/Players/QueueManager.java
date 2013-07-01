package me.ampayne2.UltimateGames.Players;

import java.util.HashMap;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;

public class QueueManager {

	private UltimateGames ultimateGames;
	private HashMap<String, Arena> queue = new HashMap<String, Arena>();
	
	public QueueManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	
}
