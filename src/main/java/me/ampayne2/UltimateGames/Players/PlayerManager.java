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
package me.ampayne2.UltimateGames.Players;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Events.GameJoinEvent;

public class PlayerManager implements Listener{
	
	private UltimateGames ultimateGames;
	private HashMap<String, Arena> playerArenas = new HashMap<String, Arena>();
	private HashMap<String, Boolean> playerInArena = new HashMap<String, Boolean>();
	
	public PlayerManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	/**
	 * Checks to see if a player is in an arena.
	 * 
	 * @param playerName The player's name.
	 * @return If the player is in an arena or not.
	 */
	public Boolean isPlayerInArena(String playerName) {
		if (playerInArena.containsKey(playerName)) {
			return playerInArena.get(playerName);
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the arena a player is in.
	 * 
	 * @param playerName The player's name.
	 * @return The arena a player is in. Null if the player isn't in an arena.
	 */
	public Arena getPlayerArena(String playerName) {
		if (playerArenas.containsKey(playerName)) {
			return playerArenas.get(playerName);
		} else {
			return null;
		}
	}
	
	/**
	 * Adds a player to an arena.
	 * 
	 * @param playerName The player's name.
	 * @param arena The arena.
	 */
	public void addPlayerToArena(String playerName, Arena arena) {
		if (!isPlayerInArena(playerName) && getPlayerArena(playerName) == null) {
			playerInArena.put(playerName, true);
			playerArenas.put(playerName, arena);
			arena.addPlayer(playerName);
			arena.getGame().getGamePlugin().addPlayer(arena, playerName);
			GameJoinEvent gameJoinEvent = new GameJoinEvent(Bukkit.getPlayer(playerName), arena);
			Bukkit.getServer().getPluginManager().callEvent(gameJoinEvent);
			ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
		}
	}
	
	/**
	 * Removes a player from an arena.
	 * 
	 * @param playerName The player's name.
	 * @param arena The arena.
	 */
	public void removePlayerFromArena(String playerName, Arena arena) {
		if (isPlayerInArena(playerName) && getPlayerArena(playerName) != null) {
			playerInArena.remove(playerName);
			playerArenas.remove(playerName);
			arena.removePlayer(playerName);
			arena.getGame().getGamePlugin().removePlayer(arena, playerName);
			for (SpawnPoint spawnPoint : ultimateGames.getSpawnpointManager().getSpawnPointsOfArena(arena)) {
				if (spawnPoint.getPlayer() != null && spawnPoint.getPlayer().equals(playerName)) {
					spawnPoint.lock(false);
					spawnPoint.lock(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String playerName = event.getPlayer().getName();
		playerInArena.put(playerName, false);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		String playerName = event.getPlayer().getName();
		if (isPlayerInArena(playerName) && getPlayerArena(playerName) != null) {
			Arena arena = getPlayerArena(playerName);
			removePlayerFromArena(playerName, arena);
		}
		ultimateGames.getQueueManager().removePlayerFromQueues(playerName);
	}
	
}
