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
package me.ampayne2.ultimategames.events.players;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.events.arenas.ArenaEvent;
import org.bukkit.entity.Player;

/**
 * Called after a spectator joins an arena.
 */
public class SpectatorPostJoinEvent extends ArenaEvent {
    private final Player player;

    public SpectatorPostJoinEvent(Player player, Arena arena) {
        super(arena);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
