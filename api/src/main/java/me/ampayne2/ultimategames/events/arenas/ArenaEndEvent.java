/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.events.arenas;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.EndType;

/**
 * An event called after an arena ends.
 */
public class ArenaEndEvent extends ArenaEvent {
    private final EndType endType;

    /**
     * Creates a new ArenaEndEvent.
     *
     * @param arena The arena about to end.
     */
    public ArenaEndEvent(Arena arena, EndType endType) {
        super(arena);
        this.endType = endType;
    }

    public EndType getEndType() {
        return endType;
    }
}
