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

	public ArrayList<String> getNextPlayers(Integer amount, Arena arena) {
		if (queue.containsKey(arena)) {
			ArrayList<String> arenaQueue = queue.get(arena);
			ArrayList<String> nextPlayers = new ArrayList<String>();
			for (int i=0; i<amount; i++) {
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
		replace.put("%Position%", Integer.toString(queue.get(arena).size()));
		Integer gamePosition = (int) Math.ceil((double) queue.get(arena).size() / arena.getMaxPlayers());
		if (gamePosition == 1) {
			replace.put("%GamePosition", "next game");
		} else {
			replace.put("%GamePosition", gamePosition.toString() + " games from now");
		}
		ultimateGames.getMessageManager().send(playerName, replace, "queues.join");

	}

	public void sendLeaveMessage(String playerName, Arena arena) {
		HashMap<String, String> replace = new HashMap<String, String>();
		replace.put("%ArenaName%", arena.getName());
		replace.put("%GameName%", arena.getGame().getGameDescription().getName());
		ultimateGames.getMessageManager().send(playerName, replace, "queues.leave");
	}
	
	public void createQueue(Arena arena) {
		clearArenaQueue(arena);
		queue.put(arena, new ArrayList<String>());
	}

	public void addPlayerToQueue(String playerName, Arena arena) {
		removePlayerFromQueues(playerName);
		if (queue.containsKey(arena)) {
			queue.get(arena).add(playerName);
			sendJoinMessage(playerName, arena);
		}
	}

	public void removePlayerFromQueues(String playerName) {
		Iterator<Entry<Arena, ArrayList<String>>> it = queue.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Arena, ArrayList<String>> entry = it.next();
			for (String player : entry.getValue()) {
				if (player.equals(playerName)) {
					entry.getValue().remove(player);
					sendLeaveMessage(playerName, entry.getKey());
				}
			}
		}
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
