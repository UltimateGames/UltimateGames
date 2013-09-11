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
package me.ampayne2.ultimategames.chests;

import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

/**
 * Represents a chest used for an ultimate game.
 */
public abstract class UGChest {

    private Chest chest;
    private Arena arena;

    /**
     * Creates a new chest
     * @param chest Chest to be turned into UGChest.
     * @param arena Arena of the chest.
     */
    public UGChest(Chest chest, Arena arena) {
        this.chest = chest;
        this.arena = arena;
    }

    /**
     * Resets the contents of a UG Chest.
     */
    public abstract void reset();

    /**
     * Gets the UGChest's Chest.
     * @return The UGChest's Chest.
     */
    public Chest getChest() {
        return chest;
    }

    /**
     * Gets the UGChest's Inventory.
     * @return The UGChest's Inventory.
     */
    public Inventory getInventory() {
        return chest.getInventory();
    }

    /**
     * Gets the UGChest's Arena.
     * @return arena The UGChest's Arena.
     */
    public Arena getArena() {
        return arena;
    }

}
