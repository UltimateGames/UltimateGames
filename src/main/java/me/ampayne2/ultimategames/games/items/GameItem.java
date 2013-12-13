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
package me.ampayne2.ultimategames.games.items;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameItem {
    private final ItemStack item;
    private final Set<ItemAction> actions;
    private boolean singleUse;

    /**
     * Creates a new GameItem.
     *
     * @param item      The ItemStack of the GameItem.
     * @param actions   The ItemActions of the GameItem.
     * @param singleUse If the GameItem can only be used once.
     */
    public GameItem(ItemStack item, boolean singleUse, ItemAction... actions) {
        this.item = item;
        this.actions = new HashSet<ItemAction>(Arrays.asList(actions));
        this.singleUse = singleUse;
    }

    /**
     * Gets the GameItem's ItemStack.
     *
     * @return The ItemStack.
     */
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Checks to see if a GameItem can only be used once.
     *
     * @return True if the GameItem can only be used once, else false.
     */
    public boolean hasSingleUse() {
        return singleUse;
    }

    /**
     * Gets the GameItem's ItemAction.
     *
     * @return The ItemAction.
     */
    public Set<ItemAction> getActions() {
        return actions;
    }

    /**
     * Handles clicking the GameItem.
     *
     * @param event The PlayerInteractEvent.
     */
    public boolean click(Arena arena, PlayerInteractEvent event) {
        for (ItemAction action : actions) {
            if (event.getAction() == action.getAction()) {
                return action.perform(arena, event);
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameItem gameItem = (GameItem) o;

        return item.getType().equals(gameItem.item.getType()) && item.getItemMeta().getDisplayName().equals(gameItem.getItem().getItemMeta().getDisplayName()) && singleUse == gameItem.singleUse;
    }

    /**@Override
    public int hashCode() {
        int result = item.getType().hashCode() + item.getItemMeta().getDisplayName().hashCode();
        result = 31 * result + (singleUse ? 1 : 0);
        return result;
    }*/
}
