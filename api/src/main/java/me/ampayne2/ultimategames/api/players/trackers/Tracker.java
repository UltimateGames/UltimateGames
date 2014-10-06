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
package me.ampayne2.ultimategames.api.players.trackers;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.DisconnectionHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;
import net.visualillusionsent.utils.TaskManager;

import java.util.concurrent.TimeUnit;

/**
 * The base class for a tracker.
 */
public abstract class Tracker implements Runnable, PluginListener {
    private final UltimateGames ultimateGames;
    private final Player player;
    private final Arena arena;

    /**
     * Creates a new Tracker.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param player        The tracking player.
     * @param arena         The tracking player's arena.
     */
    public Tracker(UltimateGames ultimateGames, Player player, Arena arena) {
        this.ultimateGames = ultimateGames;
        this.player = player;
        this.arena = arena;
        TaskManager.scheduleContinuedTask(this,0,5 * 20, TimeUnit.MILLISECONDS);
    }

    /**
     * Gets the tracking player.
     *
     * @return The tracking player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the tracking player's arena.
     *
     * @return The tracking player's arena.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Stops the tracker from tracking.
     */
    public void stop() {
        TaskManager.removeTask(this);
    }

    /**
     * Updates the tracker.
     */
    public abstract void run();

    /**
     * Stops the tracker when the tracking player disconnects.
     */
    @HookHandler(priority = Priority.PASSIVE, ignoreCanceled = true)
    public void onPlayerQuit(DisconnectionHook event) {
        if (event.getPlayer().equals(player)) {
            stop();
        }
    }
}
