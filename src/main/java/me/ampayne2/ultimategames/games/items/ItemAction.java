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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Performs an action when a GameItem is clicked with a certain interact action.
 */
public abstract class ItemAction {
    private Action action;

    public ItemAction(Action action) {
        this.action = action;
    }

    /**
     * Gets the interact action that performs this ItemAction.
     *
     * @return The Action.
     */
    public Action getAction() {
        return action;
    }

    /**
     * Performs the action.
     *
     * @param arena The arena the action was performed in.
     * @param event The PlayerInteractEvent.
     * @return True if the action was performed successfuly. Used for single use items.
     */
    public abstract boolean perform(Arena arena, PlayerInteractEvent event);
}
