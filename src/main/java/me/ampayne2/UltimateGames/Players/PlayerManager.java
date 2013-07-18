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

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;

public class PlayerManager implements Listener{
	
	private UltimateGames ultimateGames;
	private HashMap<String, Arena> playerArenas = new HashMap<String, Arena>();
	private HashMap<String, Boolean> playerInArena = new HashMap<String, Boolean>();
	
	public PlayerManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String playerName = event.getPlayer().getName();
		playerInArena.put(playerName, false);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		String playerName = event.getPlayer().getName();
		// if player is in arena
		if (playerArenas.containsKey(playerName)) {
			Arena arena = playerArenas.get(playerName);
			// remove player from arena's personal player list
			arena.removePlayer(playerName);
			// let game handle the removal
			arena.getGame().getGamePlugin().removePlayer(arena, playerName);
			// removes from manager
			playerArenas.remove(playerName);
			// removes player from any spawnpoints
			for (SpawnPoint spawnPoint : ultimateGames.getSpawnpointManager().getSpawnPointsOfArena(arena)) {
				if (spawnPoint.getPlayer() != null && spawnPoint.getPlayer().equals(playerName)) {
					spawnPoint.lock(false);
					spawnPoint.lock(true);
				}
			}
		}
		if (playerInArena.containsKey(playerName)) {
			// removes from manager
			playerInArena.remove(playerName);
		}
		// removes player from queues
		ultimateGames.getQueueManager().removePlayerFromQueues(playerName);
	}
	
}
