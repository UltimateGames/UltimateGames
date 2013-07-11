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
package me.ampayne2.UltimateGames;

import java.net.URLClassLoader;

import me.ampayne2.UltimateGames.Arenas.ArenaManager;
import me.ampayne2.UltimateGames.Command.CommandController;
import me.ampayne2.UltimateGames.Files.ConfigManager;
import me.ampayne2.UltimateGames.Games.GameManager;
import me.ampayne2.UltimateGames.Games.Plugin.PluginClassLoader;
import me.ampayne2.UltimateGames.Listeners.ArenaListener;
import me.ampayne2.UltimateGames.Listeners.SignListener;
import me.ampayne2.UltimateGames.LobbySigns.LobbySignManager;
import me.ampayne2.UltimateGames.Players.QueueManager;
import me.ampayne2.UltimateGames.Utils.Utils;

import org.bukkit.plugin.java.JavaPlugin;

public class UltimateGames extends JavaPlugin {
	private JavaPlugin plugin;
	private ConfigManager configManager;
	private GameManager gameManager;
	private ArenaManager arenaManager;
	private LobbySignManager lobbySignManager;
	private QueueManager queueManager;
	private Message messageManager;
	private PluginClassLoader pluginClassLoader;
	private Utils utils;

	public void onEnable() {
		plugin = this;
		getConfig().options().copyDefaults(true);
		saveConfig();
		configManager = new ConfigManager(this);
		messageManager = new Message(this);
		pluginClassLoader = new PluginClassLoader((URLClassLoader) this.getClassLoader());
		gameManager = new GameManager(this);
		queueManager = new QueueManager(this);
		arenaManager = new ArenaManager(this);
		lobbySignManager = new LobbySignManager(this);
		utils = new Utils();
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
		getServer().getPluginManager().registerEvents(new ArenaListener(this), this);
		getCommand("ultimategames").setExecutor(new CommandController(this));
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
	
	public QueueManager getQueueManager() {
		return queueManager;
	}
	
	public PluginClassLoader getPluginClassLoader() {
		return pluginClassLoader;
	}
	
	public Utils getUtils() {
		return utils;
	}
}
