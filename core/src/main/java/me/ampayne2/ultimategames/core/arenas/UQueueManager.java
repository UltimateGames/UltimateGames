/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.arenas;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.QueueManager;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.api.utils.UGUtils;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UQueueManager implements QueueManager {
    private final UG ultimateGames;
    private final Map<Arena, List<String>> queue = new HashMap<>();

    /**
     * Creates a new QueueManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UQueueManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean isPlayerInQueue(String playerName) {
        for (Entry<Arena, List<String>> arena : queue.entrySet()) {
            for (String player : arena.getValue()) {
                if (playerName.equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isPlayerInQueue(String playerName, Arena arena) {
        if (queue.containsKey(arena)) {
            for (String player : queue.get(arena)) {
                if (playerName.equals(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> getNextPlayers(int amount, Arena arena) {
        List<String> nextPlayers = new ArrayList<>();
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

    @Override
    public void addPlayerToQueue(Player player, Arena arena) {
        String playerName = player.getName();
        removePlayerFromQueues(player);
        if (queue.containsKey(arena)) {
            queue.get(arena).add(playerName);
        } else {
            List<String> players = new ArrayList<>();
            players.add(playerName);
            queue.put(arena, players);
        }
        sendJoinMessage(player, arena);
        arena.getGame().getGamePlugin().onPlayerJoinQueue(player, arena);
    }

    @Override
    public void removePlayerFromQueues(Player player) {
        String playerName = player.getName();
        Map<Arena, List<String>> copy = new HashMap<>(queue);
        for (Entry<Arena, List<String>> entry : queue.entrySet()) {
            Arena arena = entry.getKey();
            for (String queuePlayer : new ArrayList<>(entry.getValue())) {
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
     * Sends the player a queue join message.
     *
     * @param player The player.
     * @param arena  The arena.
     */
    public void sendJoinMessage(Player player, Arena arena) {
        Integer queuePosition = queue.get(arena).size();
        String position = queuePosition.toString() + UGUtils.getOrdinalSuffix(queuePosition);
        Integer gamePosition = (int) Math.ceil((double) queue.get(arena).size() / arena.getMaxPlayers());
        ultimateGames.getMessenger().sendMessage(player, UGMessage.QUEUE_JOIN, arena.getName(), arena.getGame().getName(), position, gamePosition == 1 ? "next game" : gamePosition + " games from now");
    }

    /**
     * Sends the player a queue leave message.
     *
     * @param player The player.
     * @param arena  The arena.
     */
    public void sendLeaveMessage(Player player, Arena arena) {
        ultimateGames.getMessenger().sendMessage(player, UGMessage.QUEUE_LEAVE, arena.getName(), arena.getGame().getName());
    }

    @Override
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
