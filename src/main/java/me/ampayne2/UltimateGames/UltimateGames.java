package me.ampayne2.UltimateGames;

import me.ampayne2.UltimateGames.Listeners.SignListener;

import org.bukkit.plugin.java.JavaPlugin;

public class UltimateGames extends JavaPlugin {
	private JavaPlugin plugin;
	private MessageConfig msgConfig;
	private Message msg;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	private LobbySignManager lobbySignManager;

	public void onEnable() {
		plugin = this;
		/*
		 * msgConfig = new MessageConfig(this, "MessageConfig.yml");
		 * msgConfig.saveMessageConfig(); msg = new Message(this);
		 */
		getConfig().options().copyDefaults(true);
		saveConfig();
		gameManager = new GameManager(this);
		arenaManager = new ArenaManager(this);
		lobbySignManager = new LobbySignManager(this);
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public Message getMessage() {
		return msg;
	}

	public MessageConfig getMessageConfig() {
		return msgConfig;
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
