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
package me.ampayne2.ultimategames.api.arenas;

import org.bukkit.Location;

import java.util.List;

/**
 * Manages the ultimate games arenas.
 */
public interface ArenaManager {

    /**
     * Checks to see if an arena exists.
     *
     * @param arenaName The arena's name.
     * @param gameName  The arena's game's name.
     * @return If the arena exists or not.
     */
    boolean arenaExists(String arenaName, String gameName);

    /**
     * Checks to see if a location is inside an arena.
     *
     * @param location The location.
     * @return If the location is inside an arena or not.
     */
    boolean isLocationInArena(Location location);

    /**
     * Gets the arena a location is inside of.
     *
     * @param location The location.
     * @return The arena. Null if location isn't inside arena.
     */
    Arena getLocationArena(Location location);

    /**
     * Gets the arena from its name and its game's name.
     *
     * @param arenaName The arena's name.
     * @param gameName  The game's name.
     * @return The arena. Null if the arena doesn't exist.
     */
    Arena getArena(String arenaName, String gameName);

    /**
     * Gets the arenas of a specific game.
     *
     * @param gameName The game's name.
     * @return The arenas. Null if the game doesn't exist or if the game has no arenas.
     */
    List<Arena> getArenasOfGame(String gameName);

    /**
     * Opens an arena.
     *
     * @param arena The arena.
     */
    void openArena(Arena arena);

    /**
     * Starts the arena
     *
     * @param arena The arena.
     */
    void startArena(Arena arena);

    /**
     * Begins an arena.
     *
     * @param arena The arena.
     */
    void beginArena(Arena arena);

    /**
     * Ends an arena.
     *
     * @param arena The arena.
     */
    void endArena(Arena arena);

    /**
     * Stops an arena.
     *
     * @param arena The arena.
     */
    void stopArena(Arena arena);

    /**
     * Gets all of the arenas.
     *
     * @return All of the existing arenas.
     */
    List<Arena> getArenas();
}
