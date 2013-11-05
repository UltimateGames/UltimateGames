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
package me.ampayne2.ultimategames.arenas.spawnpoints;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * The base class for a spawnpoint.
 */
public abstract class SpawnPoint {
    protected final Arena arena;
    protected final Location location;

    /**
     * Creates a new SpawnPoint
     *
     * @param arena    The arena of the spawnpoint.
     * @param location The location of the spawnpoint.
     */
    public SpawnPoint(Arena arena, Location location) {
        this.arena = arena;
        this.location = location;
    }

    /**
     * Gets the spawnpoint's arena.
     *
     * @return The spawnpoint's arena.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Gets the spawnpoint's location.
     *
     * @return The spawnpoint's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Teleports a player to the spawnpoint.
     *
     * @param player The player to teleport.
     */
    public abstract void teleportPlayer(Player player);
}
