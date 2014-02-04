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
package me.ampayne2.ultimategames.api.arenas.zones;

import me.ampayne2.ultimategames.api.arenas.Arena;

import java.util.Map;

/**
 * Manages arena zones.
 */
public interface ZoneManager {

    /**
     * Checks if an arena has a zone of a certain name.
     *
     * @param arena The arena.
     * @param name  The zone's name.
     * @return If the arena has a zone of the certain name or not.
     */
    boolean hasZone(Arena arena, String name);

    /**
     * Get a specific zone of an arena.
     *
     * @param arena The arena.
     * @param name  The zone's.
     * @return The zone.
     */
    Zone getZone(Arena arena, String name);

    /**
     * Gets the zones of an arena.
     *
     * @param arena The arena.
     * @return The zones.
     */
    Map<String, Zone> getZonesOfArena(Arena arena);
}
