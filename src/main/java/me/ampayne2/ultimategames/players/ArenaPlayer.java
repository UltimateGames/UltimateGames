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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ampayne2.ultimategames.arenas.Arena;

public class ArenaPlayer {
    
    private String playerName;
    private Arena arena;
    private boolean editing = false;
    
    /**
     * Represents a player in an arena.
     * @param playerName The player's name.
     * @param arena The arena.
     */
    public ArenaPlayer(String playerName, Arena arena) {
        this.playerName = playerName;
        this.arena = arena;
    }
    
    /**
     * Gets the player.
     * @return The player.
     */
    public Player getPlayer() {
        return Bukkit.getPlayerExact(playerName);
    }
    
    /**
     * Gets a player's name.
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Gets the player's arena.
     * @return The player's arena.
     */
    public Arena getArena() {
        return arena;
    }
    
    /**
     * Checks if the player is in editing mode.
     * @return True if the player is in editing mode, else false.
     */
    public boolean isEditing() {
        return editing;
    }
    
    /**
     * Sets the player's editing mode.
     * @param editing If the player should be in editing mode.
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
    }

}
