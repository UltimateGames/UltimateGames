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
import me.ampayne2.ultimategames.api.events.arenas.ArenaEvent;
import net.canarymod.api.entity.living.humanoid.Player;

/**
 * Base class for any player event.
 */
public class CancelablePlayerEvent extends ArenaEvent {
    private final Player player;

    /**
     * Creates a new PlayerEvent.
     *
     * @param player The player associated with the event.
     * @param arena  The arena associated with the event.
     */
    public CancelablePlayerEvent(Player player, Arena arena) {
        super(arena);
        this.player = player;
    }

    /**
     * Gets the player associated with the event.
     *
     * @return The player associated with the event.
     */
    public Player getPlayer() {
        return player;
    }
}
