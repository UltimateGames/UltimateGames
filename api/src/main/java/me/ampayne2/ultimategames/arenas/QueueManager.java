/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.arenas;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * Manages arena queues.
 */
public interface QueueManager {

    /**
     * Checks to see if a player is in a queue.
     *
     * @param playerName The player's name.
     * @return If the player is in a queue or not.
     */
    boolean isPlayerInQueue(String playerName);

    /**
     * Checks to see if a player is in the queue of an arena.
     *
     * @param playerName The player's name.
     * @param arena      The arena.
     * @return If the player is in the arena's queue or not.
     */
    boolean isPlayerInQueue(String playerName, Arena arena);

    /**
     * Gets the next players in an arena's queue.
     *
     * @param amount The amount of players to get.
     * @param arena  The arena.
     * @return The players.
     */
    List<String> getNextPlayers(int amount, Arena arena);

    /**
     * Adds a player to an arena's queue.
     *
     * @param player The player.
     * @param arena  The arena.
     */
    void addPlayerToQueue(Player player, Arena arena);

    /**
     * Removes a player from all queues.
     *
     * @param player The player.
     */
    void removePlayerFromQueues(Player player);

    /**
     * Clears an arena's queue.
     *
     * @param arena The arena.
     */
    void clearArenaQueue(Arena arena);
}
