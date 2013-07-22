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
package me.ampayne2.UltimateGames.Players;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpawnPoint implements Listener{

	private UltimateGames ultimateGames;
	private Arena arena;
	private Location location;
	private Boolean locked;
	private String playerName;
	
	public SpawnPoint(UltimateGames ultimateGames, Arena arena, Location location, Boolean locked) {
		this.arena = arena;
		this.location = location;
		this.locked = locked;
		this.ultimateGames = ultimateGames;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Boolean locked() {
		return locked;
	}
	
	public void lock(Boolean enabled) {
		this.locked = enabled;
		if (enabled == false) {
			this.playerName = null;
		}
	}
	
	public void teleportPlayer(String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		player.teleport(location);
		this.playerName = playerName;
	}
	
	public String getPlayer() {
		return playerName;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (locked && playerName != null && event.getPlayer().getName().equals(playerName)) {
			if (Math.abs(event.getTo().getX() - location.getX()) >= 1 || Math.abs(event.getTo().getZ() - location.getZ()) >= 1) {
				Location location = new Location(this.location.getWorld(), this.location.getX(), this.location.getY(), this.location.getZ());
				location.setPitch(event.getFrom().getPitch());
				location.setYaw(event.getFrom().getYaw());
				Bukkit.getPlayer(playerName).teleport(location);
				ultimateGames.getMessageManager().sendMessage(event.getPlayer().getName(), "spawnpoints.leave");
			}
		}
	}
	
}