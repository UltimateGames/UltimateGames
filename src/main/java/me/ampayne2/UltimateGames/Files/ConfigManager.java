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
package me.ampayne2.UltimateGames.Files;

import java.io.File;
import java.util.HashMap;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Games.Game;

public class ConfigManager {

	private UltimateGames ultimateGames;
	private ConfigAccessor messageConfig;
	private ConfigAccessor gamesConfig;
	private ConfigAccessor arenaConfig;
	private ConfigAccessor lobbyConfig;
	private ConfigAccessor lobbySignConfig;
	private HashMap<Game, ConfigAccessor> gameConfigs;
	
	public ConfigManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		messageConfig = new ConfigAccessor(ultimateGames.getPlugin(), "MessageConfig.yml", ultimateGames.getPlugin().getDataFolder());
		messageConfig.saveDefaultConfig();
		gamesConfig = new ConfigAccessor(ultimateGames.getPlugin(), "Games.yml", ultimateGames.getPlugin().getDataFolder());
		gamesConfig.saveDefaultConfig();
		arenaConfig = new ConfigAccessor(ultimateGames.getPlugin(), "Arenas.yml", ultimateGames.getPlugin().getDataFolder());
		arenaConfig.saveDefaultConfig();
		lobbyConfig = new ConfigAccessor(ultimateGames.getPlugin(), "Lobbies.yml", ultimateGames.getPlugin().getDataFolder());
		lobbyConfig.saveDefaultConfig();
		lobbySignConfig = new ConfigAccessor(ultimateGames.getPlugin(), "LobbySigns.yml", ultimateGames.getPlugin().getDataFolder());
		lobbySignConfig.saveDefaultConfig();
		gameConfigs = new HashMap<Game, ConfigAccessor>();
	}
	
	public ConfigAccessor getMessageConfig() {
		return messageConfig;
	}
	
	public ConfigAccessor getGamesConfig() {
		return gamesConfig;
	}
	
	public ConfigAccessor getArenaConfig() {
		return arenaConfig;
	}
	
	public ConfigAccessor getLobbyConfig() {
		return lobbyConfig;
	}
	
	public ConfigAccessor getLobbySignConfig() {
		return lobbySignConfig;
	}
	
	public ConfigAccessor getGameConfig(Game game) {
		if (!gameConfigs.containsKey(game)) {
			addGameConfig(game);
		}
		return gameConfigs.get(game);
	}
	
	public void addGameConfig(Game game) {
		if (!gameConfigs.containsKey(game)) {
			ConfigAccessor config = new ConfigAccessor(ultimateGames.getPlugin(), game.getGameDescription().getName() + ".yml", new File(ultimateGames.getPlugin().getDataFolder() + "/Games"));
			config.saveConfig();
			gameConfigs.put(game, config);
		}
	}
}
