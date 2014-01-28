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

import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Location;

import java.util.List;

/**
 * An UltimateGames arena.
 */
public interface Arena {

    /**
     * Gets the name of an arena.
     *
     * @return The name.
     */
    String getName();

    /**
     * Gets the game of an arena.
     *
     * @return The game.
     */
    Game getGame();

    /**
     * Removes a player from the arena's player list.
     *
     * @param playerName The player's name.
     */
    void removePlayer(String playerName);

    /**
     * Removes a spectator from the arena's spectator list.
     *
     * @param playerName The spectator's name.
     */
    void removeSpectator(String playerName);

    /**
     * Checks to see if the arena has a certain player.
     *
     * @param playerName The player's name.
     * @return True if the arena has the player, else false.
     */
    boolean hasPlayer(String playerName);

    /**
     * Checks to see if the arena has a certain spectator.
     *
     * @param playerName The spectator's name.
     * @return True if the arena has the spectator, else false.
     */
    boolean hasSpectator(String playerName);

    /**
     * Gets the players in the arena.
     *
     * @return The players.
     */
    List<String> getPlayers();

    /**
     * Gets the spectators in the arena.
     *
     * @return The spectators.
     */
    List<String> getSpectators();

    /**
     * Gets the min players of the arena.
     *
     * @return The minimum amount of players.
     */
    int getMinPlayers();

    /**
     * Gets the max players of the arena.
     *
     * @return The maximum amount of players.
     */
    int getMaxPlayers();

    /**
     * Gets the status of the arena.
     *
     * @return The status.
     */
    ArenaStatus getStatus();

    /**
     * Gets the allowExplosionDamage setting.
     *
     * @return True if the game allows explosion damage else false.
     */
    boolean allowExplosionDamage();

    /**
     * Gets the allowExplosionBlockBreaking setting.
     *
     * @return True if the game allows explosion block breaking else false.
     */
    boolean allowExplosionBlockBreaking();

    /**
     * Gets the allowMobSpawning setting.
     *
     * @return True if the game allows mob spawning else false.
     */
    boolean allowMobSpawning();

    /**
     * Gets the region of the arena.
     *
     * @return The region.
     */
    Region getRegion();

    /**
     * Gets the amount of times played.
     *
     * @return The amount of times played.
     */
    int getTimesPlayed();

    /**
     * Checks to see if a location is inside the arena.
     *
     * @param location The location.
     * @return If the location is inside the arena or not.
     */
    boolean locationIsInArena(Location location);
}
