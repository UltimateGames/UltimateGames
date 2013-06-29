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
