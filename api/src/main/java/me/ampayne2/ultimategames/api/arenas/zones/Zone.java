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
import net.canarymod.api.world.position.Location;

/**
 * The base class for a Zone.
 */
public interface Zone {

    /**
     * Gets the zone's arena.
     *
     * @return The zone's arena.
     */
    Arena getArena();

    /**
     * Gets the zone's name.
     *
     * @return The zone's name.
     */
    String getName();

    /**
     * Gets the zone's center location.
     *
     * @return The zone's center location.
     */
    Location getCenterLocation();

    /**
     * Gets the zone's radius.
     *
     * @return The zone's radius.
     */
    int getRadius();

    /**
     * Sets the zone's radius.
     *
     * @param radius The zone's radius.
     */
    void setRadius(int radius);

    /**
     * Gets the zone's radius type.
     *
     * @return The zone's radius type.
     */
    RadiusType getRadiusType();

    /**
     * Sets the zone's radius type.
     *
     * @param radiusType The zone's radius type.
     */
    void setRadiusType(RadiusType radiusType);

    /**
     * Checks if a location is inside the zone.
     *
     * @param location The location.
     * @return True if the location is inside the zone, else false.
     */
    boolean isLocationInZone(Location location);

    /**
     * Gets the ConfigurationSection of the Zone.
     *
     * @return The zone's ConfigurationSection.
     */
    ConfigurationSection getSection();
}
