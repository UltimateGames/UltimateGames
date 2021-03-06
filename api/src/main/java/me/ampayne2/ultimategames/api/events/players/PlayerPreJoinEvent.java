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
package me.ampayne2.ultimategames.api.events.players;

import me.ampayne2.ultimategames.api.arenas.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * An event called before a player joins.
 */
public class PlayerPreJoinEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;

    /**
     * Creates a new PlayerPreJoinEvent.
     *
     * @param player The player about to join.
     * @param arena  The arena the player is about to join.
     */
    public PlayerPreJoinEvent(Player player, Arena arena) {
        super(player, arena);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
