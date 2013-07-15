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
package me.ampayne2.UltimateGames.Arenas;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Enums.PlayerType;
import me.ampayne2.UltimateGames.Games.Game;

public class Arena {
	private UltimateGames ultimateGames;
	private String arenaName;
	private Game game;
	private ArrayList<String> players = new ArrayList<String>();
	private Integer maxPlayers;
	private ArenaStatus arenaStatus;
	private HashMap<String, Boolean> arenaSettings = new HashMap<String, Boolean>();
	private Location minLocation;
	private Location maxLocation;
	private World arenaWorld;

	public Arena(UltimateGames ultimateGames, Game game, String arenaName, Location corner1, Location corner2) {
		this.ultimateGames = ultimateGames;
		this.arenaName = arenaName;
		this.game = game;
		arenaStatus = ArenaStatus.ARENA_STOPPED;
		FileConfiguration gamesConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
		FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
		String arenaPath = "Arenas."+game.getGameDescription().getName()+"."+arenaName;
		//Get all arena information. Tries to get from arena config, if doesn't exist there then gets from default game settings, if doesn't exist there then is set specifically to true/false
		Boolean storeInventory = arenaConfig.getBoolean(arenaPath+".Players.Store-Inventory", gamesConfig.getBoolean("DefaultSettings.Store-Inventory", true));
		Boolean storeArmor = arenaConfig.getBoolean(arenaPath+".Players.Store-Armor", gamesConfig.getBoolean("DefaultSettings.Store-Armor", true));
		Boolean storeExp = arenaConfig.getBoolean(arenaPath+".Players.Store-Exp", gamesConfig.getBoolean("DefaultSettings.Store-Exp", true));
		Boolean storeEffects = arenaConfig.getBoolean(arenaPath+".Players.Store-Effects", gamesConfig.getBoolean("DefaultSettings.Store-Effects", true));
		Boolean storeGamemode = arenaConfig.getBoolean(arenaPath+".Players.Store-Gamemode", gamesConfig.getBoolean("DefaultSettings.Store-Gamemode", true));
		Boolean resetAfterMatch = arenaConfig.getBoolean(arenaPath+".Reset-After-Match", gamesConfig.getBoolean("DefaultSettings.Reset-After-Match", true));
		Boolean allowExplosionDamage = arenaConfig.getBoolean(arenaPath+".Allow-Explosion-Damage", gamesConfig.getBoolean("DefaultSettings.Allow-Explosion-Damage", true));
		Boolean allowExplosionBlockBreaking = arenaConfig.getBoolean(arenaPath+".Allow-Explosion-Block-Breaking", gamesConfig.getBoolean("DefaultSettings.Allow-Explosion-Block-Breaking", true));
		Boolean allowBuilding = arenaConfig.getBoolean(arenaPath+".Allow-Building", gamesConfig.getBoolean("DefaultSettings.Allow-Building", true));
		Boolean allowBreaking = arenaConfig.getBoolean(arenaPath+".Allow-Breaking", gamesConfig.getBoolean("DefaultSettings.Allow-Breaking", true));
		//Put all the arena information into the arenaSettings hashmap
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
		if (game.getGameDescription().getPlayerType() == PlayerType.SINGLE_PLAYER) {
			maxPlayers = 1;
		} else if (game.getGameDescription().getPlayerType() == PlayerType.TWO_PLAYER) {
			maxPlayers = 2;
		} else if (game.getGameDescription().getPlayerType() == PlayerType.CONFIGUREABLE) {
			maxPlayers = gamesConfig.getInt("DefaultSettings.MaxPlayers", 8);
		}
		//takes the 2 corners and turns them into minLocation and maxLocation
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
		
		//create the arena in the config if it doesn't exist
		if (arenaConfig.getConfigurationSection(arenaPath) == null) {
			arenaConfig.set(arenaPath + ".Status", "ARENA_STOPPED");
			arenaConfig.set(arenaPath + ".MaxPlayers", maxPlayers);
			arenaConfig.set(arenaPath + ".Players.Store-Inventory", storeInventory);
			arenaConfig.set(arenaPath + ".Players.Store-Armor", storeArmor);
			arenaConfig.set(arenaPath + ".Players.Store-Exp", storeExp);
			arenaConfig.set(arenaPath + ".Players.Store-Effects", storeEffects);
			arenaConfig.set(arenaPath + ".Players.Store-Gamemode", storeGamemode);
			arenaConfig.set(arenaPath + ".Reset-After-Match", resetAfterMatch);
			arenaConfig.set(arenaPath + ".Allow-Explosion-Damage", allowExplosionDamage);
			arenaConfig.set(arenaPath + ".Allow-Explosion-Block-Breaking", allowExplosionBlockBreaking);
			arenaConfig.set(arenaPath + ".Allow-Building", allowBuilding);
			arenaConfig.set(arenaPath + ".Allow-Breaking", allowBreaking);
			arenaConfig.set(arenaPath + ".Arena-Location.world", arenaWorld.getName());
			arenaConfig.set(arenaPath + ".Arena-Location.minx", minLocation.getBlockX());
			arenaConfig.set(arenaPath + ".Arena-Location.miny", minLocation.getBlockY());
			arenaConfig.set(arenaPath + ".Arena-Location.minz", minLocation.getBlockZ());
			arenaConfig.set(arenaPath + ".Arena-Location.maxx", maxLocation.getBlockX());
			arenaConfig.set(arenaPath + ".Arena-Location.maxy", maxLocation.getBlockY());
			arenaConfig.set(arenaPath + ".Arena-Location.maxz", maxLocation.getBlockZ());
		}
		ultimateGames.getConfigManager().getGameConfig(game).saveConfig();
		ultimateGames.getConfigManager().getArenaConfig().saveConfig();
	}

	public String getName() {
		return arenaName;
	}

	public Game getGame() {
		return game;
	}
	
	public boolean addPlayer(String playerName) {
		if(game.getGameDescription().getPlayerType() != PlayerType.INFINITE && players.size() >= maxPlayers) {
			// makes sure arena has room
			setStatus(ArenaStatus.STARTING);
			return false;
		} else if(players.contains(playerName)) {
			// player already in arena
			return false;
		} else {
			// player joined successfully
			players.add(playerName);
			if(game.getGameDescription().getPlayerType() != PlayerType.INFINITE && players.size() == maxPlayers) {
				setStatus(ArenaStatus.STARTING);
			}
			return true;
		}
	}
	
	public boolean removePlayer(String playerName) {
		if (players.contains(playerName)) {
			players.remove(playerName);
			ultimateGames.getLobbySignManager().updateLobbySignsOfArena(this);
			return true;
		} else {
			return false;
		}
	}
	
	public void removePlayers() {
		if (!players.isEmpty()) {
			players.clear();
			ultimateGames.getLobbySignManager().updateLobbySignsOfArena(this);
		}
	}
	
	public boolean hasPlayer(String playerName) {
		if(!players.isEmpty() && players.contains(playerName)) {
			return true;
		} else {
			return false;
		}
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

	public Boolean locationIsInArena(Location location) {
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
