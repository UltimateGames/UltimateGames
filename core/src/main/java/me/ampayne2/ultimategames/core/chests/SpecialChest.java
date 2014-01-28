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
package me.ampayne2.ultimategames.core.chests;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SpecialChest extends UGChest implements Listener {
    private final UG ultimateGames;
    private Set<ChestAction> actions = new HashSet<>();

    public SpecialChest(UG ultimateGames, Chest chest, Arena arena, String label, Set<ChestAction> actions) {
        super(chest, arena, ChestType.SPECIAL, label);
        this.ultimateGames = ultimateGames;
        this.actions = actions;
        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);
    }

    /**
     * Creates a new SpecialChest.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     * @param chest         The chest.
     * @param arena         The arena.
     * @param label         The label.
     */
    public SpecialChest(UG ultimateGames, Chest chest, Arena arena, String label) {
        super(chest, arena, ChestType.SPECIAL, label);
        this.ultimateGames = ultimateGames;
        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);
    }

    /**
     * Adds an action or actions to the chest.
     *
     * @param actions The action(s) to add.
     */
    public void addAction(ChestAction... actions) {
        this.actions.addAll(Arrays.asList(actions));
    }

    @Override
    public void reset() {
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void open(InventoryOpenEvent event) {
        if (event.getInventory().getHolder().equals(getChest())) {
            boolean destroy = false;
            for (ChestAction action : actions) {
                if (action.perform(getArena(), event) && action.destroysOnPerform()) {
                    destroy = true;
                }
            }
            event.setCancelled(true);
            if (destroy) {
                getChest().getBlock().setType(Material.AIR);
            }
        }
    }
}
