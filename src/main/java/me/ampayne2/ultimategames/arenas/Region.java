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
package me.ampayne2.ultimategames.arenas;

import me.ampayne2.ultimategames.UltimateGames;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rectangular region in a world.
 */
public class Region {
    private final World world;
    private final Double minX;
    private final Double maxX;
    private final Double minZ;
    private final Double maxZ;

    /**
     * Creates a new region.
     *
     * @param world The world of the region.
     * @param minX  The region's minimum x.
     * @param maxX  The region's maximum x.
     * @param minZ  The region's minimum z.
     * @param maxZ  The region's maximum z.
     */
    public Region(World world, double minX, double maxX, double minZ, double maxZ) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    /**
     * Gets the world of the region.
     *
     * @return The region's world.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Gets the minimum x of the region.
     *
     * @return The region's minX.
     */
    public Double getMinX() {
        return minX;
    }

    /**
     * Gets the maximum x of the region.
     *
     * @return The region's maxX.
     */
    public Double getMaxX() {
        return maxX;
    }

    /**
     * Gets the minimum z of the region.
     *
     * @return The region's minZ.
     */
    public Double getMinZ() {
        return minZ;
    }

    /**
     * Gets the maximum z of the region.
     *
     * @return The region's maxZ.
     */
    public Double getMaxZ() {
        return maxZ;
    }

    /**
     * Converts a region to a string list for saving to config.
     *
     * @return The region in list form.
     */
    public List<String> toList() {
        List<String> list = new ArrayList<String>();
        list.add(world.getName());
        list.add(minX.toString());
        list.add(maxX.toString());
        list.add(minZ.toString());
        list.add(maxZ.toString());
        return list;
    }

    /**
     * Gets the maximum location of the region.
     *
     * @return The region's maximum location.
     */
    public Location getMaximumLocation() {
        return new Location(world, maxX, 0, maxZ);
    }

    /**
     * Gets the minimum location of the region.
     *
     * @return The region's minimum location.
     */
    public Location getMinimumLocation() {
        return new Location(world, minX, 0, minZ);
    }

    /**
     * Converts a region in list form back to a region.
     *
     * @param list The string list.
     * @return The region.
     */
    public static Region fromList(List<String> list) {
        if (list != null && list.size() == 5) {
            try {
                World world = Bukkit.getServer().getWorld(list.get(0));
                Double minX = Double.parseDouble(list.get(1));
                Double maxX = Double.parseDouble(list.get(2));
                Double minZ = Double.parseDouble(list.get(3));
                Double maxZ = Double.parseDouble(list.get(4));
                if (world != null) {
                    return new Region(world, minX, maxX, minZ, maxZ);
                }
            } catch (Exception e) {
                UltimateGames.getInstance().getMessenger().debug(e);
            }
        }
        return null;
    }

    /**
     * Converts two locations into a region.<br>
     * The two locations are automatically converted into min/max, you don't have to worry about it yourself.
     *
     * @param corner1 Location 1 of the region.
     * @param corner2 Location 2 of the region.
     * @return The region.
     */
    public static Region fromCorners(Location corner1, Location corner2) {
        World world = corner1.getWorld();
        Double minX = (corner1.getX() < corner2.getX() ? corner1.getX() : corner2.getX()) + 1;
        Double maxX = (corner1.getX() > corner2.getX() ? corner1.getX() : corner2.getX()) - 1;
        Double minZ = (corner1.getZ() < corner2.getZ() ? corner1.getZ() : corner2.getZ()) + 1;
        Double maxZ = (corner1.getZ() > corner2.getZ() ? corner1.getZ() : corner2.getZ()) - 1;
        return new Region(world, minX, maxX, minZ, maxZ);
    }
}
