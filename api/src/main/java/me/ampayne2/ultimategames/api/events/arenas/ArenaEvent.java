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
package me.ampayne2.ultimategames.api.events.arenas;

import me.ampayne2.ultimategames.api.arenas.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Base for any arena event.
 */
public abstract class ArenaEvent extends Event {
    private final Arena arena;
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Creates an ArenaEvent.
     *
     * @param arena The arena associated with the event.
     */
    public ArenaEvent(Arena arena) {
        this.arena = arena;
    }

    /**
     * Gets the arena of the event.
     *
     * @return The arena.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Gets the ArenaEvent's Handlers.
     *
     * @return The Handlers.
     */
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Gets the ArenaEvent's HandlerList.
     *
     * @return The HandlerList.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
