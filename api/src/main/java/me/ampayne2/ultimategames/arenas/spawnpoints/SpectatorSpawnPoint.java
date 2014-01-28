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
package me.ampayne2.ultimategames.arenas.spawnpoints;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An arena spawn point for spectators.
 */
public class SpectatorSpawnPoint extends SpawnPoint {

    /**
     * Creates a new SpectatorSpawnPoint
     *
     * @param arena    The arena of the spawnpoint.
     * @param location The location of the spawnpoint.
     */
    public SpectatorSpawnPoint(Arena arena, Location location) {
        super(arena, location);
    }

    /**
     * Teleports a player to the spawnpoint.
     *
     * @param player The player to teleport.
     */
    public void teleportPlayer(Player player) {
        player.teleport(getLocation());
    }
}
