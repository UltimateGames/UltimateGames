/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.api.players;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.message.UGMessage;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;

/**
 * Represents a player in an arena.
 */
public class ArenaPlayer {
    private final UltimateGames ultimateGames;
    private final String playerName;
    private final Arena arena;
    private boolean editing = false;

    /**
     * Creates a new ArenaPlayer.
     *
     * @param playerName The player's name.
     * @param arena      The arena.
     */
    public ArenaPlayer(UltimateGames ultimateGames, String playerName, Arena arena) {
        this.ultimateGames = ultimateGames;
        this.playerName = playerName;
        this.arena = arena;
    }

    /**
     * Gets the player.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return Canary.getServer().getPlayer(playerName);
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

    /**
     * Checks if the player is in editing mode.
     *
     * @return True if the player is in editing mode, else false.
     */
    public boolean isEditing() {
        return editing;
    }

    /**
     * Sets the player's editing mode.
     *
     * @param editing If the player should be in editing mode.
     */
    public void setEditing(boolean editing) {
        this.editing = editing;
        ultimateGames.getMessenger().sendMessage(getPlayer(), editing ? UGMessage.ARENA_EDITON : UGMessage.ARENA_EDITOFF);
    }
}
