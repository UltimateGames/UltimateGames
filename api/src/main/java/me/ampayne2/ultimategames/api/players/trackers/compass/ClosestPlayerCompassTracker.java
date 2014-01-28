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
import me.ampayne2.ultimategames.api.utils.UGUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.HashSet;

/**
 * A tracker that tracks the closest player from a collection of players with the compass.
 */
public class ClosestPlayerCompassTracker extends PlayerCompassTracker {
    private Collection<Player> targetPlayers;

    /**
     * Creates a new ClosestPlayerCompassTracker.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param player        The tracking player.
     * @param targetPlayers The initial target players.
     * @param arena         The tracking player's arena.
     */
    public ClosestPlayerCompassTracker(UltimateGames ultimateGames, Player player, Collection<Player> targetPlayers, Arena arena) {
        super(ultimateGames, player, arena);
        this.targetPlayers = targetPlayers;
    }

    /**
     * Creates a new ClosestPlayerCompassTracker with no initial target players.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param player        The tracking player.
     * @param arena         The tracking player's arena.
     */
    public ClosestPlayerCompassTracker(UltimateGames ultimateGames, Player player, Arena arena) {
        this(ultimateGames, player, new HashSet<Player>(), arena);
    }

    @Override
    public Location getTarget() {
        setTargetPlayer(UGUtils.getNearestPlayer(getPlayer(), targetPlayers));
        return super.getTarget();
    }

    /**
     * Sets the target players of the tracker.
     *
     * @param targetPlayers The target players.
     */
    public void setTargetPlayers(Collection<Player> targetPlayers) {
        this.targetPlayers = targetPlayers;
    }

    /**
     * Gets the target players of the tracker.
     *
     * @return The target players.
     */
    public Collection<Player> getTargetPlayers() {
        return targetPlayers;
    }

    /**
     * Removes a player from the collection of tracked players when the player disconnects.
     */
    @Override
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTargetPlayerQuit(PlayerQuitEvent event) {
        if (targetPlayers.contains(event.getPlayer())) {
            targetPlayers.remove(event.getPlayer());
        }
    }
}
