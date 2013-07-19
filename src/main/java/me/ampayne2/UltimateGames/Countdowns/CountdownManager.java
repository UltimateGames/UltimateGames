package me.ampayne2.UltimateGames.Countdowns;

import java.util.ArrayList;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;

public class CountdownManager {
	
	private UltimateGames ultimateGames;
	private ArrayList<Arena> starting = new ArrayList<Arena>();
	private ArrayList<Arena> ending = new ArrayList<Arena>();
	
	public CountdownManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	/**
	 * Checks to see if an arena has a starting countdown running.
	 * 
	 * @param arena The arena.
	 * @return If the arena has a starting countdown running.
	 */
	public Boolean isStartingCountdownEnabled(Arena arena) {
		return starting.contains(arena);
	}
	
	/**
	 * Checks to see if an arena has an ending countdown running.
	 * 
	 * @param arena The arena.
	 * @return If the arena has an ending countdown running.
	 */
	public Boolean isEndingCountdownEnabled(Arena arena) {
		return ending.contains(arena);
	}
	
	/**
	 * Creates a starting countdown for an arena.
	 * 
	 * @param arena The arena.
	 * @param seconds Initial seconds on the countdown.
	 */
	public void createStartingCountdown(Arena arena, Integer seconds) {
		if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
			if (!starting.contains(arena)) {
				starting.add(arena);
			}
			new StartingCountdown(ultimateGames, arena, seconds, seconds);
		}
	}
	
	/**
	 * Stops a starting countdown for an arena.
	 * 
	 * @param arena The arena.
	 */
	public void stopStartingCountdown(Arena arena) {
		if (starting.contains(arena)) {
			starting.remove(arena);
		}
	}
	
	/**
	 * Creates an ending countdown for an arena.
	 * 
	 * @param arena The arena.
	 * @param seconds Initial seconds on the countdown.
	 * @param expDisplay Should the exp bar be used to display the countdown?
	 */
	public void createEndingCountdown(Arena arena, Integer seconds, Boolean expDisplay) {
		if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
			if (!ending.contains(arena)) {
				ending.add(arena);
			}
			new EndingCountdown(ultimateGames, arena, seconds, seconds, expDisplay);
		}
	}
	
	/**
	 * Stops an ending countdown for an arena.
	 * 
	 * @param arena The arena.
	 */
	public void stopEndingCountdown(Arena arena) {
		if (ending.contains(arena)) {
			ending.remove(arena);
		}
	}

}
