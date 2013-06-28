package me.ampayne2.UltimateGames;

import me.ampayne2.UltimateGames.Arenas.ArenaManager;
import me.ampayne2.UltimateGames.Files.ConfigManager;
import me.ampayne2.UltimateGames.Games.GameManager;
import me.ampayne2.UltimateGames.Listeners.SignListener;
import me.ampayne2.UltimateGames.LobbySigns.LobbySignManager;

import org.bukkit.plugin.java.JavaPlugin;

public class UltimateGames extends JavaPlugin {
	private JavaPlugin plugin;
	private ConfigManager configManager;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	private LobbySignManager lobbySignManager;
	private Message messageManager;

	public void onEnable() {
		plugin = this;
		getConfig().options().copyDefaults(true);
		saveConfig();
		configManager = new ConfigManager(this);
		messageManager = new Message(this);
		gameManager = new GameManager(this);
		arenaManager = new ArenaManager(this);
		lobbySignManager = new LobbySignManager(this);
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}
	
	public ConfigManager getConfigManager() {
		return configManager;
	}

	public Message getMessageManager() {
		return messageManager;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public ArenaManager getArenaManager() {
		return arenaManager;
	}
	
	public LobbySignManager getLobbySignManager() {
		return lobbySignManager;
	}
}
