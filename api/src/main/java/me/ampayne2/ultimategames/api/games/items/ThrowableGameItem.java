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
package me.ampayne2.ultimategames.api.games.items;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A GameItem that throws an Item entity.<br>
 * Must be registered with the {@link GameItemManager}.
 */
public abstract class ThrowableGameItem extends GameItem {
    private final ItemStack item;
    private final int velocity;

    /**
     * Creates a new ThrowableGameItem.
     *
     * @param item The ItemStack of the ThrowableGameItem.
     * @param velocity The velocity of the ThrowableGameItem.
     */
    public ThrowableGameItem(ItemStack item, int velocity) {
        super(item, true);
        this.item = item.clone();
        this.item.setAmount(1);
        this.velocity = velocity;
    }

    /**
     * Creates a new ThrowableGameItem with the default velocity of 1.
     *
     * @param item The ItemStack of the ThrowableGameItem.
     */
    public ThrowableGameItem(ItemStack item) {
        super(item, true);
        this.item = item.clone();
        this.item.setAmount(1);
        this.velocity = 1;
    }

    @Override
    public boolean click(Arena arena, PlayerInteractEvent event) {
        if (arena.getStatus() == ArenaStatus.RUNNING) {
            Player player = event.getPlayer();
            Item dropped = player.getWorld().dropItem(player.getEyeLocation(), item.clone());
            dropped.setVelocity(player.getEyeLocation().getDirection().clone().multiply(velocity));
            onItemThrow(arena, dropped);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean click(Arena arena, PlayerInteractEntityEvent event) {
        if (arena.getStatus() == ArenaStatus.RUNNING) {
            Player player = event.getPlayer();
            Item dropped = player.getWorld().dropItem(player.getEyeLocation(), item.clone());
            dropped.setVelocity(player.getEyeLocation().getDirection().clone().multiply(velocity));
            onItemThrow(arena, dropped);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Called when the Item is thrown.
     *
     * @param arena The arena.
     * @param item  The Item.
     */
    public abstract void onItemThrow(Arena arena, Item item);
}
