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
package me.ampayne2.ultimategames.api.players.trackers.compass;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.players.trackers.Tracker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * A tracker that tracks a location with a compass.
 */
public class CompassTracker extends Tracker {
    private Location target;

    /**
     * Creates a new CompassTracker.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param player        The tracking player.
     * @param target        The initial target location.
     * @param arena         The tracking player's arena.
     */
    public CompassTracker(UltimateGames ultimateGames, Player player, Location target, Arena arena) {
        super(ultimateGames, player, arena);
        this.target = target;
    }

    /**
     * Creates a new CompassTracker with the initial target at the player's location.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param player        The tracking player.
     * @param arena         The tracking player's arena.
     */
    public CompassTracker(UltimateGames ultimateGames, Player player, Arena arena) {
        this(ultimateGames, player, player.getLocation(), arena);
    }

    @Override
    public void run() {
        Location location = getTarget();
        if (location != null) {
            getPlayer().setCompassTarget(location);
        }
    }

    /**
     * Sets the target of the tracker.
     *
     * @param target The target.
     */
    public void setTarget(Location target) {
        this.target = target;
    }

    /**
     * Gets the target of the tracker.
     *
     * @return The target.
     */
    public Location getTarget() {
        return target;
    }
}
