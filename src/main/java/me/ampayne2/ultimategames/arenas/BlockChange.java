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

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Contains basic information about a block used for logging.
 */
public class BlockChange {

    public Arena arena;
    public Material material;
    public byte data;
    public Location location;

    /**
     * Creates a new BlockChange object.
     * @param arena The arena the change took place in.
     * @param material The material of the original block.
     * @param data The data of the original block.
     * @param location The location the change took place at.
     */
    public BlockChange(Arena arena, Material material, byte data, Location location) {
        this.arena = arena;
        this.material = material;
        this.data = data;
        this.location = location;
    }

}
