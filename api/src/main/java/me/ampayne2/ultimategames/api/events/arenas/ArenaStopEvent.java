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

/**
 * An event called after an arena stops.
 */
public class ArenaStopEvent extends ArenaEvent {

    /**
     * Creates a new ArenaStopEvent.
     *
     * @param arena The arena about to stop.
     */
    public ArenaStopEvent(Arena arena) {
        super(arena);
    }
}
