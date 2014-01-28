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
package me.ampayne2.ultimategames.players;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Represents a player spectating an arena.
 */
public class ArenaSpectator {
    private final String playerName;
    private final Arena arena;

    /**
     * Creates a new ArenaSpectator.
     *
     * @param playerName The player's name.
     * @param arena      The arena.
     */
    public ArenaSpectator(String playerName, Arena arena) {
        this.playerName = playerName;
        this.arena = arena;
    }

    /**
     * Gets the player. Calls Bukkit.getPlayerExact(), so is an expensive call.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return Bukkit.getPlayerExact(playerName);
    }

    /**
     * Gets a player's name.
     *
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Gets the player's arena.
     *
     * @return The player's arena.
     */
    public Arena getArena() {
        return arena;
    }
}
