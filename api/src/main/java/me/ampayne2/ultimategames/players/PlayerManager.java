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
import org.bukkit.entity.Player;

/**
 * Manages players and spectators in UltimateGames.
 */
public interface PlayerManager {

    /**
     * Checks to see if a player is in an arena.
     *
     * @param playerName The player's name.
     * @return If the player is in an arena or not.
     */
    boolean isPlayerInArena(String playerName);

    /**
     * Checks to see if a player is spectating an arena.
     *
     * @param playerName The player's name.
     * @return If the player is in an arena or not.
     */
    boolean isPlayerSpectatingArena(String playerName);

    /**
     * Gets the arena a player is in or spectating.
     *
     * @param playerName The player's name.
     * @return The arena a player is in or spectating. Null if the player isn't in or spectating an arena.
     */
    Arena getPlayerArena(String playerName);

    /**
     * Turns an ArenaPlayer into an ArenaSpectator.
     *
     * @param player The player to make into a spectator.
     */
    void makePlayerSpectator(Player player);

    /**
     * Adds a player to an arena.
     *
     * @param player      The player.
     * @param arena       The arena.
     * @param sendMessage If a join message should be sent.
     */
    void addPlayerToArena(Player player, Arena arena, Boolean sendMessage);

    /**
     * Adds a spectator to an arean.
     *
     * @param player The spectator's name.
     * @param arena  The arena.
     */
    void addSpectatorToArena(Player player, Arena arena);

    /**
     * Removes a player from an arena.
     *
     * @param player      The player.
     * @param sendMessage If a leave message should be sent.
     */
    void removePlayerFromArena(Player player, Boolean sendMessage);

    /**
     * Removes a spectator from an arena.
     *
     * @param player The spectator.
     */
    void removeSpectatorFromArena(Player player);

    /**
     * Gets the ArenaPlayer of a player in an arena.
     *
     * @param playerName The player's name.
     * @return The ArenaPlayer.
     */
    ArenaPlayer getArenaPlayer(String playerName);

    /**
     * Gets the ArenaSpectator of a spectator spectating an arena.
     *
     * @param playerName The spectator's name.
     * @return The ArenaSpectator.
     */
    ArenaSpectator getArenaSpectator(String playerName);
}
