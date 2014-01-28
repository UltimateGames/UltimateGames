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
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * Performs an action when a GameItem is clicked with a certain interact action.
 */
public abstract class ChestAction {
    private boolean destroyChestOnPerform;

    /**
     * Creates a new ChestAction.
     *
     * @param destroyChestOnPerform If the chest should be destroyed when this action is performed successfully.
     */
    public ChestAction(boolean destroyChestOnPerform) {
        this.destroyChestOnPerform = destroyChestOnPerform;
    }

    /**
     * Checks if this ChestAction should destroy the chest on perform.
     *
     * @return True if the chest should be destroyed on perform.
     */
    public boolean destroysOnPerform() {
        return destroyChestOnPerform;
    }

    /**
     * Performs the action.
     *
     * @param arena The arena the action was performed in.
     * @param event The InventoryOpenEvent.
     * @return True if the action was performed successfuly.
     */
    public abstract boolean perform(Arena arena, InventoryOpenEvent event);
}
