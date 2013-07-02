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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;

public class QueueManager {

	private UltimateGames ultimateGames;
	private HashMap<Arena, ArrayList<String>> queue = new HashMap<Arena, ArrayList<String>>();

	public QueueManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	public Boolean isPlayerInQueue(String playerName, Arena arena) {
		if (queue.containsKey(arena)) {
			ArrayList<String> players = queue.get(arena);
			for (String player : players) {
				if (playerName.equals(player)) {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<String> getNextPlayers(Integer amount, Arena arena) {
		if (queue.containsKey(arena)) {
			ArrayList<String> arenaQueue = queue.get(arena);
			ArrayList<String> nextPlayers = new ArrayList<String>();
			for (int i = 0; i < amount; i++) {
				nextPlayers.add(arenaQueue.get(i));
			}
			return nextPlayers;
		}
		return null;
	}

	public void sendJoinMessage(String playerName, Arena arena) {
		HashMap<String, String> replace = new HashMap<String, String>();
		replace.put("%ArenaName%", arena.getName());
		replace.put("%GameName%", arena.getGame().getGameDescription().getName());
		Integer queuePosition = queue.get(arena).size();
		String position = queuePosition.toString() + ultimateGames.getUtils().getOrdinalSuffix(queuePosition);
		replace.put("%Position%", position);
		Integer gamePosition = (int) Math.ceil((double) queue.get(arena).size() / arena.getMaxPlayers());
		if (gamePosition == 1) {
			replace.put("%GamePosition%", "next game");
		} else {
			replace.put("%GamePosition%", gamePosition.toString() + " games from now");
		}
		ultimateGames.getMessageManager().send(playerName, replace, "queues.join");
	}

	public void sendLeaveMessage(String playerName, Arena arena) {
		HashMap<String, String> replace = new HashMap<String, String>();
		replace.put("%ArenaName%", arena.getName());
		replace.put("%GameName%", arena.getGame().getGameDescription().getName());
		ultimateGames.getMessageManager().send(playerName, replace, "queues.leave");
	}

	public void addPlayerToQueue(String playerName, Arena arena) {
		removePlayerFromQueues(playerName);
		if (queue.containsKey(arena)) {
			queue.get(arena).add(playerName);
		} else {
			ArrayList<String> players = new ArrayList<String>();
			players.add(playerName);
			queue.put(arena, players);
		}
		sendJoinMessage(playerName, arena);
	}

	public void removePlayerFromQueues(String playerName) {
		HashMap<Arena, ArrayList<String>> copy = new HashMap<Arena, ArrayList<String>>(queue);
		Iterator<Entry<Arena, ArrayList<String>>> it = queue.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Arena, ArrayList<String>> entry = it.next();
			Arena arena = entry.getKey();
			ArrayList<String> players = new ArrayList<String>(entry.getValue());
			for (String player : players) {
				if (playerName.equals(player)) {
					copy.get(arena).remove(player);
					sendLeaveMessage(playerName, arena);
					if (copy.get(arena).isEmpty()) {
						copy.remove(arena);
					}
				}
			}
		}
		queue.clear();
		queue.putAll(copy);
	}

	public void clearArenaQueue(Arena arena) {
		if (queue.containsKey(arena)) {
			ArrayList<String> players = queue.get(arena);
			for (String player : players) {
				sendLeaveMessage(player, arena);
			}
			queue.remove(arena);
		}
	}

	public void clearAllQueues() {
		Iterator<Entry<Arena, ArrayList<String>>> it = queue.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Arena, ArrayList<String>> entry = it.next();
			for (String player : entry.getValue()) {
				sendLeaveMessage(player, entry.getKey());
			}
		}
	}
}
