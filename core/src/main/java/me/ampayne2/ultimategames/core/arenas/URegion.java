/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.arenas;

import me.ampayne2.ultimategames.api.arenas.Region;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class URegion implements Region {
    private final World world;
    private final double minX;
    private final double maxX;
    private final double minZ;
    private final double maxZ;

    /**
     * Creates a new region.
     *
     * @param world The world of the region.
     * @param minX  The region's minimum x.
     * @param maxX  The region's maximum x.
     * @param minZ  The region's minimum z.
     * @param maxZ  The region's maximum z.
     */
    public URegion(World world, double minX, double maxX, double minZ, double maxZ) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public double getMinX() {
        return minX;
    }

    @Override
    public double getMaxX() {
        return maxX;
    }

    @Override
    public double getMinZ() {
        return minZ;
    }

    @Override
    public double getMaxZ() {
        return maxZ;
    }

    @Override
    public Location getMaximumLocation() {
        return new Location(world, maxX, 0, maxZ);
    }

    @Override
    public Location getMinimumLocation() {
        return new Location(world, minX, 0, minZ);
    }

    /**
     * Converts a region to a string list for saving to config.
     *
     * @return The region in list form.
     */
    public List<String> toList() {
        List<String> list = new ArrayList<>();
        list.add(world.getName());
        list.add(Double.toString(minX));
        list.add(Double.toString(maxX));
        list.add(Double.toString(minZ));
        list.add(Double.toString(maxZ));
        return list;
    }

    /**
     * Converts a region in list form back to a region.
     *
     * @param list The string list.
     * @return The region.
     */
    public static URegion fromList(List<String> list) {
        if (list != null && list.size() == 5) {
            try {
                World world = Bukkit.getServer().getWorld(list.get(0));
                double minX = Double.parseDouble(list.get(1));
                Double maxX = Double.parseDouble(list.get(2));
                Double minZ = Double.parseDouble(list.get(3));
                Double maxZ = Double.parseDouble(list.get(4));
                if (world != null) {
                    return new URegion(world, minX, maxX, minZ, maxZ);
                }
            } catch (Exception e) {
                UG.getInstance().getMessenger().debug(e);
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
    public static URegion fromCorners(Location corner1, Location corner2) {
        World world = corner1.getWorld();
        Double minX = (corner1.getX() < corner2.getX() ? corner1.getX() : corner2.getX()) + 1;
        Double maxX = (corner1.getX() > corner2.getX() ? corner1.getX() : corner2.getX()) - 1;
        Double minZ = (corner1.getZ() < corner2.getZ() ? corner1.getZ() : corner2.getZ()) + 1;
        Double maxZ = (corner1.getZ() > corner2.getZ() ? corner1.getZ() : corner2.getZ()) - 1;
        return new URegion(world, minX, maxX, minZ, maxZ);
    }
}
