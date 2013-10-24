/*
 * This file is part of UltimateGames.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.countdowns;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages different types of countdowns for arenas.
 */
public class CountdownManager {
	private UltimateGames ultimateGames;
	private Map<Arena, Integer> starting = new HashMap<Arena, Integer>();
	private Map<Arena, Integer> ending = new HashMap<Arena, Integer>();

	/**
	 * Creates a new Countdown Manager.
	 *
	 * @param ultimateGames A reference to the UltimateGames instance.
	 */
	public CountdownManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	/**
	 * Checks to see if an arena has a starting countdown running.
	 *
	 * @param arena The arena.
	 *
	 * @return If the arena has a starting countdown running.
	 */
	public boolean isStartingCountdownEnabled(Arena arena) {
		return starting.containsKey(arena);
	}

	/**
	 * Checks to see if an arena has an ending countdown running.
	 *
	 * @param arena The arena.
	 *
	 * @return If the arena has an ending countdown running.
	 */
	public boolean isEndingCountdownEnabled(Arena arena) {
		return ending.containsKey(arena);
	}

	/**
	 * Creates a starting countdown for an arena.
	 *
	 * @param arena   The arena.
	 * @param seconds Initial seconds on the countdown.
	 */
	public void createStartingCountdown(Arena arena, Integer seconds) {
		if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
			if (!starting.containsKey(arena)) {
				starting.put(arena, Bukkit.getScheduler().scheduleSyncRepeatingTask(ultimateGames, new StartingCountdown(ultimateGames, arena, seconds), 20L, seconds * 20));
			}
		}
	}

	/**
	 * Stops a starting countdown for an arena.
	 *
	 * @param arena The arena.
	 */
	public void stopStartingCountdown(Arena arena) {
		if (starting.containsKey(arena)) {
			Bukkit.getScheduler().cancelTask(starting.get(arena));
			starting.remove(arena);
			arena.setStatus(ArenaStatus.OPEN);
		}
	}

	/**
	 * Creates an ending countdown for an arena.
	 *
	 * @param arena      The arena.
	 * @param seconds    Initial seconds on the countdown.
	 * @param expDisplay Should the exp bar be used to display the countdown?
	 */
	public void createEndingCountdown(Arena arena, Integer seconds, Boolean expDisplay) {
		if (!ending.containsKey(arena)) {
			ending.put(arena, Bukkit.getScheduler().scheduleSyncRepeatingTask(ultimateGames, new EndingCountdown(ultimateGames, arena, seconds, expDisplay), 20L, seconds * 20));
		}
	}

	/**
	 * Stops an ending countdown for an arena.
	 *
	 * @param arena The arena.
	 */
	public void stopEndingCountdown(Arena arena) {
		if (ending.containsKey(arena)) {
			Bukkit.getScheduler().cancelTask(ending.get(arena));
			ending.remove(arena);
		}
	}
}
