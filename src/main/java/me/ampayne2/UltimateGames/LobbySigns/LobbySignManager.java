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
package me.ampayne2.UltimateGames.LobbySigns;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Games.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings("unchecked")
public class LobbySignManager {

	private UltimateGames ultimateGames;
	private ArrayList<LobbySign> lobbySigns;

	public LobbySignManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		loadLobbySigns();
	}

	public boolean isLobbySign(Sign sign) {
		if (getLobbySign(sign) == null) {
			return false;
		} else {
			return true;
		}
	}

	public LobbySign getLobbySign(Sign sign) {
		for (LobbySign ssign : lobbySigns) {
			if (sign.equals(ssign.getSign())) {
				return ssign;
			}
		}
		return null;
	}

	public ArrayList<LobbySign> getLobbySignsOfArena(Arena arena) {
		ArrayList<LobbySign> arenaSigns = new ArrayList<LobbySign>();
		for (LobbySign lobbySign : lobbySigns) {
			if (arena.equals(lobbySign.getArena())) {
				arenaSigns.add(lobbySign);
			}
		}
		if (arenaSigns.isEmpty()) {
			return null;
		} else {
			return arenaSigns;
		}
	}

	public ArrayList<LobbySign> getLobbySignsOfGame(Game game) {
		ArrayList<LobbySign> gameSigns = new ArrayList<LobbySign>();
		for (LobbySign lobbySign : lobbySigns) {
			if (game.equals(lobbySign.getArena().getGame())) {
				gameSigns.add(lobbySign);
			}
		}
		if (gameSigns.isEmpty()) {
			return null;
		} else {
			return gameSigns;
		}
	}

	public void updateLobbySignsOfArena(Arena arena) {
		ArrayList<LobbySign> arenaSigns = getLobbySignsOfArena(arena);
		for (LobbySign lobbySign : arenaSigns) {
			lobbySign.update();
		}
	}

	public void updateLobbySignsOfGame(Game game) {
		ArrayList<LobbySign> gameSigns = getLobbySignsOfGame(game);
		for (LobbySign lobbySign : gameSigns) {
			lobbySign.update();
		}
	}

	public void addLobbySign(LobbySign lobbySign) {
		if (!lobbySigns.contains(lobbySign)) {
			lobbySigns.add(lobbySign);
		}
	}

	public LobbySign createLobbySign(Sign sign, Arena arena) {
		if (isLobbySign(sign)) {
			// already a lobby sign here
			return null;
		}
		// lobby sign created
		LobbySign lobbySign = new LobbySign(sign, arena);
		addLobbySign(lobbySign);

		FileConfiguration lobbySignConfig = ultimateGames.getConfigManager().getLobbySignConfig().getConfig();
		String arenaPath = "LobbySigns." + arena.getGame().getGameDescription().getName() + "." + arena.getName();
		List<String> signInfo = new ArrayList<String>();
		signInfo.add(0, sign.getWorld().getName());
		signInfo.add(1, Integer.toString(sign.getX()));
		signInfo.add(2, Integer.toString(sign.getY()));
		signInfo.add(3, Integer.toString(sign.getZ()));
		List<List<String>> lobbySigns;
		if (lobbySignConfig.contains(arenaPath)) {
			lobbySigns = (List<List<String>>) lobbySignConfig.getList(arenaPath);
			lobbySigns.add(signInfo);
		} else {
			lobbySigns = new ArrayList<List<String>>();
			lobbySigns.add(signInfo);
			lobbySignConfig.createSection(arenaPath);
		}
		lobbySignConfig.set(arenaPath, lobbySigns);
		ultimateGames.getConfigManager().getLobbySignConfig().saveConfig();

		return lobbySign;
	}

	public boolean removeLobbySign(Sign sign) {
		if (isLobbySign(sign)) {
			FileConfiguration lobbySignConfig = ultimateGames.getConfigManager().getLobbySignConfig().getConfig();
			String world = sign.getWorld().getName();
			Integer x = sign.getX();
			Integer y = sign.getY();
			Integer z = sign.getZ();
			LobbySign lobbySign = getLobbySign(sign);
			String gamePath = "LobbySigns." + lobbySign.getArena().getGame().getGameDescription().getName();
			String arenaPath = gamePath + "." + lobbySign.getArena().getName();
			if (lobbySignConfig.contains(arenaPath)) {
				List<List<String>> lobbySigns = (List<List<String>>) lobbySignConfig.getList(arenaPath);
				List<List<String>> newLobbySigns = new ArrayList<List<String>>(lobbySigns);
				for (List<String> signInfo : lobbySigns) {
					if (world.equals(signInfo.get(0)) && x.equals(Integer.parseInt(signInfo.get(1))) && y.equals(Integer.parseInt(signInfo.get(2))) && z.equals(Integer.parseInt(signInfo.get(3)))) {
						newLobbySigns.remove(signInfo);
					}
				}
				lobbySignConfig.set(arenaPath, newLobbySigns);
				if (lobbySignConfig.getList(arenaPath).isEmpty()) {
					lobbySignConfig.set(arenaPath, null);	
				}
				if (lobbySignConfig.getConfigurationSection(gamePath).getKeys(true).isEmpty()) {
					lobbySignConfig.set(gamePath, null);
				}
				ultimateGames.getConfigManager().getLobbySignConfig().saveConfig();
			}
			lobbySigns.remove(lobbySign);
			return true;
		} else {
			return false;
		}
	}
	
	public void loadLobbySigns() {
		lobbySigns = new ArrayList<LobbySign>();
		FileConfiguration lobbySignConfig = ultimateGames.getConfigManager().getLobbySignConfig().getConfig();
		if (lobbySignConfig.getConfigurationSection("LobbySigns") == null) {
			return;
		}
		for (String gameKey : lobbySignConfig.getConfigurationSection("LobbySigns").getKeys(false)) {
			if (ultimateGames.getGameManager().gameExists(gameKey)) {
				String gamePath = "LobbySigns." + gameKey;
				for (String arenaKey : lobbySignConfig.getConfigurationSection(gamePath).getKeys(false)) {
					if (ultimateGames.getArenaManager().arenaExists(arenaKey, gameKey)) {
						String arenaPath = gamePath + "." + arenaKey;
						List<List<String>> lobbySigns = (List<List<String>>) lobbySignConfig.getList(arenaPath);
						for (List<String> signInfo : lobbySigns) {
							String world = signInfo.get(0);
							String x = signInfo.get(1);
							String y = signInfo.get(2);
							String z = signInfo.get(3);
							if (world != null && x != null && y != null && z != null) {
								World signWorld;
								if ((signWorld = Bukkit.getWorld(world)) != null) {
									Block locBlock = new Location(signWorld, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)).getBlock();
									if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
										addLobbySign(new LobbySign((Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
