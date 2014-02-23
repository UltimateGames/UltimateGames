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
package me.ampayne2.ultimategames.api.players.classes;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.games.items.GameItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A class selector game item.
 */
public class ClassSelector extends GameItem {
    private final UltimateGames ultimateGames;
    private static final ItemStack CLASS_SELECTOR;

    /**
     * Creates a new class selector item.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     */
    public ClassSelector(UltimateGames ultimateGames) {
        super(CLASS_SELECTOR, false);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean click(Arena arena, PlayerInteractEvent event) {
        ultimateGames.getGameClassManager().getClassSelector(arena.getGame(), event.getPlayer()).open(event.getPlayer());
        return true;
    }

    static {
        CLASS_SELECTOR = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = CLASS_SELECTOR.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Class Selector");
        CLASS_SELECTOR.setItemMeta(meta);
    }
}
