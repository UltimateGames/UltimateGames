/*
 * This file is part of UltimateGames.
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames. If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.UltimateGames.Arenas;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;

import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Games.Game;

public class Arena {
	private String arenaName;
	private Game game;
	private ArrayList<String> players = new ArrayList<String>();
	private Integer maxPlayers;
	private ArenaStatus arenaStatus;
	private HashMap<String, Boolean> arenaSettings = new HashMap<String, Boolean>();
	private Location minLocation;
	private Location maxLocation;
	private World arenaWorld;

	public Arena(Game game, String arenaName, Integer maxPlayers, Boolean storeInventory, Boolean storeArmor, Boolean storeExp, Boolean storeEffects, Boolean storeGamemode, Boolean resetAfterMatch,
			Boolean allowExplosionDamage, Boolean allowExplosionBlockBreaking, Boolean allowBuilding, Boolean allowBreaking, Location corner1, Location corner2) {
		this.arenaName = arenaName;
		this.game = game;
		this.maxPlayers = maxPlayers;
		arenaSettings.put("storeInventory", storeInventory);
		arenaSettings.put("storeArmor", storeArmor);
		arenaSettings.put("storeExp", storeExp);
		arenaSettings.put("storeEffects", storeEffects);
		arenaSettings.put("storeGamemode", storeGamemode);
		arenaSettings.put("resetAfterMatch", resetAfterMatch);
		arenaSettings.put("allowExplosionDamage", allowExplosionDamage);
		arenaSettings.put("allowExplosionBlockBreaking", allowExplosionBlockBreaking);
		arenaSettings.put("allowBuilding", allowBuilding);
		arenaSettings.put("allowBreaking", allowBreaking);
		Integer minx;
		Integer miny;
		Integer minz;
		Integer maxx;
		Integer maxy;
		Integer maxz;
		if (corner1.getBlockX() < corner2.getBlockX()) {
			minx = corner1.getBlockX();
			maxx = corner2.getBlockX();
		} else {
			minx = corner2.getBlockX();
			maxx = corner1.getBlockX();
		}
		if (corner1.getBlockY() < corner2.getBlockY()) {
			miny = corner1.getBlockY();
			maxy = corner2.getBlockY();
		} else {
			miny = corner2.getBlockY();
			maxy = corner1.getBlockY();
		}
		if (corner1.getBlockZ() < corner2.getBlockZ()) {
			minz = corner1.getBlockZ();
			maxz = corner2.getBlockZ();
		} else {
			minz = corner2.getBlockZ();
			maxz = corner1.getBlockZ();
		}
		if (corner1.getWorld().equals(corner2.getWorld())) {
			arenaWorld = corner1.getWorld();
			minLocation = new Location(arenaWorld, minx, miny, minz);
			maxLocation = new Location(arenaWorld, maxx, maxy, maxz);
		}
	}

	public String getName() {
		return arenaName;
	}

	public Game getGame() {
		return game;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public Integer getMaxPlayers() {
		return maxPlayers;
	}

	public ArenaStatus getStatus() {
		return arenaStatus;
	}

	public Boolean getArenaSetting(String setting) {
		if (arenaSettings.containsKey(setting)) {
			return arenaSettings.get(setting);
		} else {
			return null;
		}
	}

	public Location getMinLocation() {
		return minLocation;
	}

	public Location getMaxLocation() {
		return maxLocation;
	}

	public World getWorld() {
		return arenaWorld;
	}

	public void setStatus(ArenaStatus status) {
		arenaStatus = status;
	}

	public void setArenaSetting(String setting, Boolean value) {
		if (arenaSettings.containsKey(setting)) {
			arenaSettings.put(setting, value);
		}
	}

	public Boolean isInArena(Location location) {
		if (location.getWorld().equals(minLocation.getWorld())) {
			if (location.getX() >= minLocation.getX() && location.getX() <= maxLocation.getX()) {
				if (location.getY() >= minLocation.getY() && location.getY() <= maxLocation.getY()) {
					if (location.getZ() >= minLocation.getZ() && location.getZ() <= maxLocation.getZ()) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
