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
package me.ampayne2.ultimategames.arenas.spawnpoints;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * An arena spawnpoint for players.
 */
public class PlayerSpawnPoint extends SpawnPoint implements Listener {
    private final UltimateGames ultimateGames;
    private Boolean locked;
    private String playerName;

    /**
     * Creates a new PlayerSpawnPoint
     *
     * @param ultimateGames A reference to the ultimateGames instance.
     * @param arena         The arena of the spawnpoint.
     * @param location      The location of the spawnpoint.
     * @param locked        If the spawnpoint is locked.
     */
    public PlayerSpawnPoint(UltimateGames ultimateGames, Arena arena, Location location, Boolean locked) {
        super(arena, location);
        this.ultimateGames = ultimateGames;
        this.locked = locked;
    }

    /**
     * Checks if the spawnpoint is locked.
     *
     * @return True if the spawnpoint is locked, else false.
     */
    public Boolean locked() {
        return locked;
    }

    /**
     * Sets the locked state of a spawnpoint.
     *
     * @param enabled The state to set. If a player is locked in a spawnpoint when set to false, the player will be released.
     */
    public void lock(Boolean enabled) {
        this.locked = enabled;
        if (!enabled) {
            playerName = null;
        }
    }

    /**
     * Teleports a player to the spawnpoint.
     *
     * @param player The player to teleport.
     */
    public void teleportPlayer(Player player) {
        if (player != null) {
            player.teleport(location);
            if (locked) {
                this.playerName = player.getName();
            }
        } else {
            this.playerName = null;
        }

    }

    /**
     * Gets the player locked to a spawnpoint. Null if nobody is locked.
     *
     * @return The player locked to a spawnpoint.
     */
    public String getPlayer() {
        return playerName;
    }

    /**
     * Stops players from attempting to leave spawnpoints.
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (locked && playerName != null) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
                return;
            }
            Player player = event.getPlayer();
            if (player.getName().equals(playerName) && (Math.abs(to.getX() - location.getX()) >= 1 || Math.abs(to.getZ() - location.getZ()) >= 1)) {
                location.setPitch(event.getFrom().getPitch());
                location.setYaw(event.getFrom().getYaw());
                player.teleport(location);
                ultimateGames.getMessageManager().sendMessage(player, "spawnpoints.leave");
            }
        }
    }
}
