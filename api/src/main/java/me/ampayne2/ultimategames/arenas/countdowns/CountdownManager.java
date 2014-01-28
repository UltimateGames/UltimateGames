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
package me.ampayne2.ultimategames.arenas.countdowns;

import me.ampayne2.ultimategames.arenas.Arena;

/**
 * Manages different types of countdowns for arenas.
 */
public interface CountdownManager {

    /**
     * Checks to see if an arena has a starting countdown running.
     *
     * @param arena The arena.
     * @return If the arena has a starting countdown running.
     */
    boolean hasStartingCountdown(Arena arena);

    /**
     * Checks to see if an arena has an ending countdown running.
     *
     * @param arena The arena.
     * @return If the arena has an ending countdown running.
     */
    boolean hasEndingCountdown(Arena arena);

    /**
     * Gets an arena's ending countdown.
     *
     * @param arena The arena.
     * @return The arena's ending countdown.
     */
    EndingCountdown getEndingCountdown(Arena arena);

    /**
     * Creates a starting countdown for an arena.
     *
     * @param arena   The arena.
     * @param seconds Initial seconds on the countdown.
     */
    void createStartingCountdown(Arena arena, Integer seconds);

    /**
     * Stops a starting countdown for an arena.
     *
     * @param arena The arena.
     */
    void stopStartingCountdown(Arena arena);

    /**
     * Creates an ending countdown for an arena.
     *
     * @param arena      The arena.
     * @param seconds    Initial seconds on the countdown.
     * @param expDisplay Should the exp bar be used to display the countdown?
     */
    void createEndingCountdown(Arena arena, Integer seconds, Boolean expDisplay);

    /**
     * Stops an ending countdown for an arena.
     *
     * @param arena The arena.
     */
    void stopEndingCountdown(Arena arena);
}
