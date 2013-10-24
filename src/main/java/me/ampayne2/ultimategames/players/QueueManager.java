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
package me.ampayne2.ultimategames.players;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class QueueManager {
	private UltimateGames ultimateGames;
	private Map<Arena, List<String>> queue = new HashMap<Arena, List<String>>();

	public QueueManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	/**
	 * Checks to see if a player is in the queue of an arena.
	 *
	 * @param playerName The player's name.
	 * @param arena      The arena.
	 *
	 * @return If the player is in the arena's queue or not.
	 */
	public boolean isPlayerInQueue(String playerName, Arena arena) {
		if (queue.containsKey(arena)) {
			List<String> players = queue.get(arena);
			for (String player : players) {
				if (playerName.equals(player)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the next players in an arena's queue.
	 *
	 * @param amount The amount of players to get.
	 * @param arena  The arena.
	 *
	 * @return The players.
	 */
	public List<String> getNextPlayers(Integer amount, Arena arena) {
		List<String> nextPlayers = new ArrayList<String>();
		if (queue.containsKey(arena)) {
			List<String> arenaQueue = queue.get(arena);
			for (int i = 0; i < amount; i++) {
				if (arenaQueue.size() > i) {
					nextPlayers.add(arenaQueue.get(i));
				}
			}
		}
		return nextPlayers;
	}

	/**
	 * Sends the player a queue join message.
	 *
	 * @param player The player.
	 * @param arena  The arena.
	 */
	public void sendJoinMessage(Player player, Arena arena) {
		Integer queuePosition = queue.get(arena).size();
		String position = queuePosition.toString() + Utils.getOrdinalSuffix(queuePosition);
		Integer gamePosition = (int) Math.ceil((double) queue.get(arena).size() / arena.getMaxPlayers());
		ultimateGames.getMessageManager().sendMessage(player, "queues.join", arena.getName(), arena.getGame().getName(), position, gamePosition == 1 ? "next game" : gamePosition + " games from now");
	}

	/**
	 * Sends the player a queue leave message.
	 *
	 * @param player The player.
	 * @param arena  The arena.
	 */
	public void sendLeaveMessage(Player player, Arena arena) {
		ultimateGames.getMessageManager().sendMessage(player, "queues.leave", arena.getName(), arena.getGame().getName());
	}

	/**
	 * Adds a player to an arena's queue.
	 *
	 * @param player The player.
	 * @param arena  The arena.
	 */
	public void addPlayerToQueue(Player player, Arena arena) {
		String playerName = player.getName();
		removePlayerFromQueues(player);
		if (queue.containsKey(arena)) {
			queue.get(arena).add(playerName);
		} else {
			List<String> players = new ArrayList<String>();
			players.add(playerName);
			queue.put(arena, players);
		}
		sendJoinMessage(player, arena);
		arena.getGame().getGamePlugin().onPlayerJoinQueue(player, arena);
	}

	/**
	 * Removes a player from all queues.
	 *
	 * @param player The player.
	 */
	public void removePlayerFromQueues(Player player) {
		String playerName = player.getName();
		Map<Arena, List<String>> copy = new HashMap<Arena, List<String>>(queue);
		for (Entry<Arena, List<String>> entry : queue.entrySet()) {
			Arena arena = entry.getKey();
			for (String queuePlayer : new ArrayList<String>(entry.getValue())) {
				if (playerName.equals(queuePlayer)) {
					copy.get(arena).remove(queuePlayer);
					sendLeaveMessage(player, arena);
					if (copy.get(arena).isEmpty()) {
						copy.remove(arena);
					}
				}
			}
		}
		queue.clear();
		queue.putAll(copy);
	}

	/**
	 * Clears an arena's queue.
	 *
	 * @param arena The arena.
	 */
	public void clearArenaQueue(Arena arena) {
		if (queue.containsKey(arena)) {
			for (String playerName : queue.get(arena)) {
				sendLeaveMessage(Bukkit.getPlayerExact(playerName), arena);
			}
			queue.remove(arena);
		}
	}

	/**
	 * Clears all queues.
	 */
	public void clearAllQueues() {
		for (Entry<Arena, List<String>> entry : queue.entrySet()) {
			for (String playerName : entry.getValue()) {
				sendLeaveMessage(Bukkit.getPlayerExact(playerName), entry.getKey());
			}
		}
		queue.clear();
	}
}
