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
import org.bukkit.World;

/**
 * Represents a rectangular region in a world.
 */
public interface Region {

    /**
     * Gets the world of the region.
     *
     * @return The region's world.
     */
    World getWorld();

    /**
     * Gets the minimum x of the region.
     *
     * @return The region's minX.
     */
    double getMinX();

    /**
     * Gets the maximum x of the region.
     *
     * @return The region's maxX.
     */
    double getMaxX();

    /**
     * Gets the minimum z of the region.
     *
     * @return The region's minZ.
     */
    double getMinZ();

    /**
     * Gets the maximum z of the region.
     *
     * @return The region's maxZ.
     */
    double getMaxZ();

    /**
     * Gets the maximum location of the region.
     *
     * @return The region's maximum location.
     */
    Location getMaximumLocation();

    /**
     * Gets the minimum location of the region.
     *
     * @return The region's minimum location.
     */
    Location getMinimumLocation();
}
