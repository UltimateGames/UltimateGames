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
package me.ampayne2.ultimategames.signs;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.SignType;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
public class UGSignManager {
	private final UltimateGames ultimateGames;
	private final Map<SignType, List<UGSign>> ugSigns = new HashMap<SignType, List<UGSign>>();

	public UGSignManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		loadUGSigns();
	}

	/**
	 * Checks to see if a sign is an Ultimate Game sign.<br>
	 * Faster than isUGSign(Sign sign);
	 *
	 * @param sign The sign to check.
	 *
	 * @return If the sign is an Ultimate Game sign.
	 */
	public boolean isUGSign(Sign sign, SignType signType) {
		for (UGSign ugSign : ugSigns.get(signType)) {
			if (sign.equals(ugSign.getSign())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if a sign is an Ultimate Game sign.
	 *
	 * @param sign The sign to check.
	 *
	 * @return If the sign is an Ultimate Game sign.
	 */
	public boolean isUGSign(Sign sign) {
		for (Entry<SignType, List<UGSign>> entry : ugSigns.entrySet()) {
			for (UGSign ugSign : entry.getValue()) {
				if (sign.equals(ugSign.getSign())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gets the redstone output sign at a location.
	 *
	 * @param location The location to check.
	 *
	 * @return The redstone output sign. Null if doesn't exist at location.
	 */
	public RedstoneOutputSign getRedstoneOutputSign(Location location) {
		for (UGSign ugSign : ugSigns.get(SignType.REDSTONE_OUTPUT)) {
			if (ugSign.getSign().getLocation().equals(location)) {
				return (RedstoneOutputSign) ugSign;
			}
		}
		return null;
	}

	/**
	 * Gets the UGsign of a sign.<br>
	 * Faster than getUGSign(Sign sign);
	 *
	 * @param sign The sign.
	 * @param signType The sign's type.
	 *
	 * @return The UG sign.
	 */
	public UGSign getUGSign(Sign sign, SignType signType) {
		for (UGSign ugSign : ugSigns.get(signType)) {
			if (sign.equals(ugSign.getSign())) {
				return ugSign;
			}
		}
		return null;
	}

	/**
	 * Gets the UGsign of a sign.
	 *
	 * @param sign The sign.
	 *
	 * @return The UG sign.
	 */
	public UGSign getUGSign(Sign sign) {
		for (Entry<SignType, List<UGSign>> entry : ugSigns.entrySet()) {
			for (UGSign ugSign : entry.getValue()) {
				if (sign.equals(ugSign.getSign())) {
					return ugSign;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the Ultimate Game signs of an arena.<br>
	 * Faster than getUGSignsOfArena(Arena arena);
	 *
	 * @param arena The arena.
	 * @param signType The SignType of the signs to get.
	 *
	 * @return The Ultimate Game Signs.
	 */
	public List<UGSign> getUGSignsOfArena(Arena arena, SignType signType) {
		List<UGSign> signs = new ArrayList<UGSign>();
		for (UGSign ugSign : ugSigns.get(signType)) {
			if (ugSign.getArena().equals(arena)) {
				signs.add(ugSign);
			}
		}
		return signs;
	}

	/**
	 * Gets the Ultimate Game signs of an arena.<br>
	 * Faster than getUGSignsOfArena(Arena arena);
	 *
	 * @param arena The arena.
	 *
	 * @return The Ultimate Game Signs.
	 */
	public List<UGSign> getUGSignsOfArena(Arena arena) {
		List<UGSign> signs = new ArrayList<UGSign>();
		for (Entry<SignType, List<UGSign>> entry : ugSigns.entrySet()) {
			for (UGSign ugSign : entry.getValue()) {
				if (ugSign.getArena().equals(arena)) {
					signs.add(ugSign);
				}
			}
		}
		return signs;
	}

	/**
	 * Gets the Ultimate Game signs of a game.<br>
	 * Faster than getUGSignsOfGame(Game game);
	 *
	 * @param game The game.
	 * @param signType The SignType of the signs to get.
	 *
	 * @return The Ultimate Game Signs.
	 */
	public List<UGSign> getUGSignsOfGame(Game game, SignType signType) {
		List<UGSign> signs = new ArrayList<UGSign>();
		for (UGSign ugSign : ugSigns.get(signType)) {
			if (ugSign.getArena().getGame().equals(game)) {
				signs.add(ugSign);
			}
		}
		return signs;
	}

	/**
	 * Gets the Ultimate Game signs of a game.
	 *
	 * @param game The game.
	 *
	 * @return The Ultimate Game Signs.
	 */
	public List<UGSign> getUGSignsOfGame(Game game) {
		List<UGSign> signs = new ArrayList<UGSign>();
		for (Entry<SignType, List<UGSign>> entry : ugSigns.entrySet()) {
			for (UGSign ugSign : entry.getValue()) {
				if (ugSign.getArena().getGame().equals(game)) {
					signs.add(ugSign);
				}
			}
		}
		return signs;
	}

	/**
	 * Updates the Ultimate Game signs of an arena.<br>
	 * Faster than updateUGSignsOfArena(Arena arena);
	 *
	 * @param arena The arena.
	 * @param signType The SignType of the signs to update.
	 */
	public void updateUGSignsOfArena(Arena arena, SignType signType) {
		for (UGSign ugSign : getUGSignsOfArena(arena, signType)) {
			ugSign.update();
		}
	}

	/**
	 * Updates the Ultimate Game signs of an arena.
	 *
	 * @param arena The arena.
	 */
	public void updateUGSignsOfArena(Arena arena) {
		for (UGSign ugSign : getUGSignsOfArena(arena)) {
			ugSign.update();
		}
	}

	/**
	 * Updates the Ultimate Game signs of a game.<br>
	 * Faster than updateUGSignsOfGame(Game game);
	 *
	 * @param game The game.
	 * @param signType The SignType of the signs to update.
	 */
	public void updateUGSignsOfGame(Game game, SignType signType) {
		for (UGSign ugSign : getUGSignsOfGame(game, signType)) {
			ugSign.update();
		}
	}

	/**
	 * Updates the Ultimate Game signs of a game.
	 *
	 * @param game The game.
	 */
	public void updateUGSignsOfGame(Game game) {
		for (UGSign ugSign : getUGSignsOfGame(game)) {
			ugSign.update();
		}
	}

	public void addUGSign(UGSign ugSign, SignType signType) {
		ugSigns.get(signType).add(ugSign);
	}

	/**
	 * Creates a UG Sign and adds it to the manager.
	 *
	 * @param label    The sign's label.
	 * @param sign     The physical sign.
	 * @param arena    The arena the sign is in.
	 * @param signType The sign's type.
	 *
	 * @return The UG Sign created.
	 */
	public UGSign createUGSign(String label, Sign sign, Arena arena, SignType signType) {
		FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
		String signPath = signType.toString() + "." + arena.getGame().getName() + "." + arena.getName();
		List<String> signInfo = new ArrayList<String>();
		signInfo.add(0, sign.getWorld().getName());
		signInfo.add(1, Integer.toString(sign.getX()));
		signInfo.add(2, Integer.toString(sign.getY()));
		signInfo.add(3, Integer.toString(sign.getZ()));
		if (signType.hasLabel()) {
			signInfo.add(4, label);
		}
		List<List<String>> ugSigns;
		if (ugSignConfig.contains(signPath)) {
			ugSigns = (List<List<String>>) ugSignConfig.getList(signPath);
			ugSigns.add(signInfo);
		} else {
			ugSigns = new ArrayList<List<String>>();
			ugSigns.add(signInfo);
			ugSignConfig.createSection(signPath);
		}
		ugSignConfig.set(signPath, ugSigns);
		ultimateGames.getConfigManager().getUGSignConfig().saveConfig();
		UGSign ugSign = null;
		switch (signType) {
			case LOBBY:
				ugSign = new LobbySign(ultimateGames, sign, arena);
				break;
			case CLICK_INPUT:
				ugSign = new ClickInputSign(label, sign, arena);
				break;
			case REDSTONE_INPUT:
				ugSign = new RedstoneInputSign(label, sign, arena);
				break;
			case TEXT_OUTPUT:
				ugSign = new TextOutputSign(label, sign, arena);
				break;
			case REDSTONE_OUTPUT:
				ugSign = new RedstoneOutputSign(label, sign, arena);
				break;
		}
		addUGSign(ugSign, signType);
		return ugSign;
	}

	/**
	 * Removes a UG Sign from the manager and config.
	 *
	 * @param sign The sign of the UG Sign to remove.
	 */
	public void removeUGSign(Sign sign) {
		UGSign ugSign = getUGSign(sign);
		if (ugSign != null) {
			FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
			String world = sign.getWorld().getName();
			Integer x = sign.getX();
			Integer y = sign.getY();
			Integer z = sign.getZ();
			SignType signType = ugSign.getSignType();
			String gamePath = signType.toString() + "." + ugSign.getArena().getGame().getName();
			String arenaPath = gamePath + "." + ugSign.getArena().getName();
			if (ugSignConfig.contains(arenaPath)) {
				List<List<String>> ugSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
				List<List<String>> newUGSigns = new ArrayList<List<String>>(ugSigns);
				for (List<String> signInfo : ugSigns) {
					if (world.equals(signInfo.get(0)) && x.equals(Integer.parseInt(signInfo.get(1))) && y.equals(Integer.parseInt(signInfo.get(2))) && z.equals(Integer.parseInt(signInfo.get(3)))) {
						newUGSigns.remove(signInfo);
					}
				}
				ugSignConfig.set(arenaPath, newUGSigns);
				if (ugSignConfig.getList(arenaPath).isEmpty()) {
					ugSignConfig.set(arenaPath, null);
				}
				if (ugSignConfig.getConfigurationSection(gamePath).getKeys(true).isEmpty()) {
					ugSignConfig.set(gamePath, null);
				}
				ultimateGames.getConfigManager().getUGSignConfig().saveConfig();
			}
			ugSigns.get(signType).remove(ugSign);
		}
	}

	/**
	 * Loads all of the Ultimate Game signs.
	 */
	public void loadUGSigns() {
		ugSigns.clear();
		FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
		for (SignType signType : EnumSet.allOf(SignType.class)) {
			ugSigns.put(signType, new ArrayList<UGSign>());
			String signTypeName = signType.toString();
			if (ugSignConfig.getConfigurationSection(signTypeName) != null) {
				for (String gameKey : ugSignConfig.getConfigurationSection(signTypeName).getKeys(false)) {
					if (ultimateGames.getGameManager().gameExists(gameKey)) {
						String gamePath = signTypeName + "." + gameKey;
						for (String arenaKey : ugSignConfig.getConfigurationSection(gamePath).getKeys(false)) {
							if (ultimateGames.getArenaManager().arenaExists(arenaKey, gameKey)) {
								String arenaPath = gamePath + "." + arenaKey;
								List<List<String>> ugSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
								for (List<String> signInfo : ugSigns) {
									World world = Bukkit.getWorld(signInfo.get(0));
									Integer x = Integer.parseInt(signInfo.get(1));
									Integer y = Integer.parseInt(signInfo.get(2));
									Integer z = Integer.parseInt(signInfo.get(3));
									Block locBlock = new Location(world, x, y, z).getBlock();
									if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
										Arena arena = ultimateGames.getArenaManager().getArena(arenaKey, gameKey);
										UGSign ugSign = null;
										switch (signType) {
											case LOBBY:
												ugSign = new LobbySign(ultimateGames, (Sign) locBlock.getState(), arena);
												break;
											case CLICK_INPUT:
												ugSign = new ClickInputSign(signInfo.get(4), (Sign) locBlock.getState(), arena);
												break;
											case REDSTONE_INPUT:
												ugSign = new RedstoneInputSign(signInfo.get(4), (Sign) locBlock.getState(), arena);
												break;
											case TEXT_OUTPUT:
												ugSign = new TextOutputSign(signInfo.get(4), (Sign) locBlock.getState(), arena);
												break;
											case REDSTONE_OUTPUT:
												ugSign = new RedstoneOutputSign(signInfo.get(4), (Sign) locBlock.getState(), arena);
												break;
										}
										addUGSign(ugSign, signType);
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
