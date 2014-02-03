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
package me.ampayne2.ultimategames.api.games.blocks;

import me.ampayne2.ultimategames.api.arenas.Arena;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * An easy way to create blocks that do something when placed.<br>
 * Must be registered with the {@link me.ampayne2.ultimategames.api.games.blocks.GameBlockManager}.
 */
public abstract class GameBlock {
    private final ItemStack item;
    private final boolean canBeBroken;

    /**
     * Creates a new GameBlock.
     *
     * @param item The ItemStack of the GameBlock.
     */
    public GameBlock(ItemStack item, boolean canBeBroken) {
        Validate.notNull(item, "ItemStack cannot be null");
        Validate.isTrue(item.getType().isBlock(), "ItemStack type must be a block");

        this.item = item;
        this.canBeBroken = canBeBroken;
    }

    /**
     * Gets an ItemStack of the GameBlock's Material.
     *
     * @return The ItemStack.
     */
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Gets the GameBlock's Material.
     *
     * @return The Material.
     */
    public Material getMaterial() {
        return item.getType();
    }

    /**
     * Gets if the GameBlock can be broken.
     *
     * @return True if the GameBlock can be broken, else false.
     */
    public boolean canBeBroken() {
        return canBeBroken;
    }

    /**
     * Handles placing the GameBlock.
     *
     * @param arena The arena the GameBlock was placed in.
     * @param event The BlockPlaceEvent.
     * @return True if the block was placed successfully, else false.
     */
    public abstract boolean place(Arena arena, BlockPlaceEvent event);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameBlock gameBlock = (GameBlock) o;

        return item.getType().equals(gameBlock.item.getType()) && item.getItemMeta().getDisplayName().equals(gameBlock.item.getItemMeta().getDisplayName()) && canBeBroken == gameBlock.canBeBroken;
    }

    @Override
    public int hashCode() {
        int result = item.getType().hashCode() + item.getItemMeta().getDisplayName().hashCode();
        result = 31 * result + (canBeBroken ? 1 : 0);
        return result;
    }
}
