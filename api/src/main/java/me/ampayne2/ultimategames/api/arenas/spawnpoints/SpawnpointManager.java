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
package me.ampayne2.ultimategames.api.arenas.spawnpoints;

import me.ampayne2.ultimategames.api.arenas.Arena;

import java.util.List;

/**
 * Manages arena spawnpoints for players and spectators.
 */
public interface SpawnpointManager {

    /**
     * Checks if an arena has a spawnpoint at the index.
     *
     * @param arena The arena.
     * @param index The index.
     * @return If the arena has a spawnpoint at the index or not.
     */
    boolean hasSpawnPointAtIndex(Arena arena, Integer index);

    /**
     * Get a specific spawnpoint of an arena.
     *
     * @param arena The arena.
     * @param index The spawnpoint index.
     * @return The spawnpoint.
     */
    PlayerSpawnPoint getSpawnPoint(Arena arena, Integer index);

    /**
     * Gets an arena's spectator spawnpoint.
     *
     * @param arena The arena.
     * @return The spawnpoint.
     */
    SpectatorSpawnPoint getSpectatorSpawnPoint(Arena arena);

    /**
     * Get a random spawnpoint of an arena.
     *
     * @param arena The arena.
     * @return The spawnpoint.
     */
    PlayerSpawnPoint getRandomSpawnPoint(Arena arena);

    /**
     * Get a random spawnpoint of an arena at or above the minIndex.
     * Useful if there are certain spawnpoints only used for specific things.
     *
     * @param arena    The arena.
     * @param minIndex The minimum index.
     * @return The spawnpoint.
     */
    PlayerSpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex);

    /**
     * Get a random spawnpoint of an arena within the minIndex and maxIndex.
     * Useful if there are certain spawnpoints only used for specific things.
     *
     * @param arena    The arena.
     * @param minIndex The minimum index.
     * @param maxIndex The maximum index.
     * @return The spawnpoint.
     */
    PlayerSpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex, Integer maxIndex);

    /**
     * Get a certain amount of spawnpoints distributed evenly among the available spawnpoints.
     * Example: You have 8 spawn points. You ask for 4. It gives you the spawnpoints at indexes 0, 2, 4, and 6.
     * Example 2: You have 16 spawn points. You ask for 2. It gives you the spawnpoints at indexes 0 and 8.
     * Useful for spawnpoint setups similar to survival games where players need to be spaced out.
     *
     * @param arena  The arena.
     * @param amount The amount of spawnpoints to get.
     * @return The spawnpoints.
     */
    List<PlayerSpawnPoint> getDistributedSpawnPoints(Arena arena, Integer amount);

    /**
     * Gets all the spawnpoints of an arena.
     *
     * @param arena The arena.
     * @return The spawnpoints.
     */
    List<PlayerSpawnPoint> getSpawnPointsOfArena(Arena arena);
}
