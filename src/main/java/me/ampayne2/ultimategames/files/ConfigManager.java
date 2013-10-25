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
package me.ampayne2.ultimategames.files;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles custom configs.
 */
public class ConfigManager {
	private final UltimateGames ultimateGames;
	private final ConfigAccessor messageConfig;
	private final ConfigAccessor arenaConfig;
	private final ConfigAccessor lobbyConfig;
	private final ConfigAccessor ugSignConfig;
	private final ConfigAccessor ugChestConfig;
	private final Map<Game, GameConfigAccessor> gameConfigs;

	public ConfigManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		File dataFolder = ultimateGames.getDataFolder();
		messageConfig = new ConfigAccessor(ultimateGames, "Messages.yml", dataFolder);
		messageConfig.saveDefaultConfig();
		arenaConfig = new ConfigAccessor(ultimateGames, "Arenas.yml", dataFolder);
		arenaConfig.saveDefaultConfig();
		lobbyConfig = new ConfigAccessor(ultimateGames, "Lobbies.yml", dataFolder);
		lobbyConfig.saveDefaultConfig();
		ugSignConfig = new ConfigAccessor(ultimateGames, "Signs.yml", dataFolder);
		ugSignConfig.saveDefaultConfig();
		ugChestConfig = new ConfigAccessor(ultimateGames, "Chests.yml", dataFolder);
		ugChestConfig.saveDefaultConfig();
		gameConfigs = new HashMap<Game, GameConfigAccessor>();
	}

	/**
	 * Gets the Message Config.
	 *
	 * @return The Message Config.
	 */
	public ConfigAccessor getMessageConfig() {
		return messageConfig;
	}

	/**
	 * Gets the Arena Config.
	 *
	 * @return The Arena Config.
	 */
	public ConfigAccessor getArenaConfig() {
		return arenaConfig;
	}

	/**
	 * Gets the Lobby Config.
	 *
	 * @return The Lobby Config.
	 */
	public ConfigAccessor getLobbyConfig() {
		return lobbyConfig;
	}

	/**
	 * Gets the Ultimate Game Sign Config.
	 *
	 * @return The Ultimate Game Sign Config.
	 */
	public ConfigAccessor getUGSignConfig() {
		return ugSignConfig;
	}

	/**
	 * Gets the Ultimate Game Chest Config.
	 *
	 * @return The Ultimate Game Sign Config.
	 */
	public ConfigAccessor getUGChestConfig() {
		return ugChestConfig;
	}

	/**
	 * Gets the Game Configs.
	 *
	 * @return The Game Configs.
	 */
	public Map<Game, GameConfigAccessor> getGameConfigs() {
		return gameConfigs;
	}

	/**
	 * Gets a Game's Config.
	 *
	 * @return The Game's Config.
	 */
	public GameConfigAccessor getGameConfig(Game game) {
		if (!gameConfigs.containsKey(game)) {
			addGameConfig(game);
		}
		return gameConfigs.get(game);
	}

	/**
	 * Adds a Game's Config to the manager.
	 *
	 * @param game The Game whose config you want to add.
	 */
	public void addGameConfig(Game game) {
		if (!gameConfigs.containsKey(game)) {
			GameConfigAccessor config = new GameConfigAccessor(ultimateGames, game.getName());
			config.saveConfig();
			gameConfigs.put(game, config);
		}
	}
}
