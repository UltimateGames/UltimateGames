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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Message {
	private UltimateGames ultimateGames;
	private Map<String, String> messages = new HashMap<String, String>();
	private Map<String, String> gameMessages = new HashMap<String, String>();

	public Message(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		loadMessages();
	}
	
	public void loadMessages() {
		if (!messages.isEmpty()) {
			messages.clear();
		}
		FileConfiguration messageConfig = ultimateGames.getConfigManager().getMessageConfig().getConfig();
		for (String key : messageConfig.getConfigurationSection("messages").getKeys(true)) {
			messages.put(key, messageConfig.getString("messages."+key));
		}
	}
	
	public void loadGameMessages() {
		gameMessages.put("ClickingGame.join", "&3%Player% joined the game. %Amount%");
		gameMessages.put("ClickingGame.countdown", "&4Game starting in %Time% seconds!");
	    gameMessages.put("ClickingGame.start", "&4Start clicking! You have 30 seconds!");
		gameMessages.put("ClickingGame.score", "&3%Name% clicked %Clicks% times!");
		/*
		if (!gameMessages.isEmpty()) {
			gameMessages.clear();
		}
		for (Game game : ultimateGames.getGameManager().getGames()) {
			FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
			for (String key : gameConfig.getConfigurationSection("messages").getKeys(true)) {
				gameMessages.put(game.getGameDescription().getName()+"."+key, gameConfig.getString("messages."+key));
			}
		}
		*/
	}

	/**
	 * Gets the message, adds the prefix, and translates the color codes.
	 * 
	 * @param messagetype Path to the message in the message config, without "Messages."
	 * @return The message.
	 */
	public String getMessage(String messagetype, String gameName) {
		String prefix = messages.get("prefix");
		if (prefix == null) {
			prefix = "&8[&bUltimateGames&8]";
		}
		String message;
		if (gameName == null) {
			message = messages.get(messagetype);
		} else {
			message = gameMessages.get(gameName+"."+messagetype);
		}
		if (message == null) {
			message = ChatColor.DARK_RED + "  No configured message for " + messagetype;
		}
		message = prefix+"  "+message;
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	/**
	 * Gets the message, and translates the color codes.
	 * 
	 * @param messagetype Path to the message in the message config, without "Messages."
	 * @return The message.
	 */
	public String getMessageWithoutPrefix(String messagetype, String gameName) {
		String message;
		if (gameName == null) {
			message = messages.get(messagetype);
		} else {
			message = gameMessages.get(gameName+"."+messagetype);
		}
		if (message == null) {
			message = ChatColor.DARK_RED + "  No configured message for " + messagetype;
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	/**
	 * Gets the message, replaces the keys, adds the prefix, and translates the color codes.
	 * 
	 * @param replace hashmap that replaces all keys in a message with a value.
	 * @param messagetype Path to the message in the message config, without "Messages."
	 * @return The message.
	 */
	public String getReplacedMessage(HashMap<String, String> replace, String messagetype, String gameName) {
		String prefix = messages.get("prefix");
		if (prefix == null) {
			prefix = "&8[&bUltimateGames&8]";
		}
		String message;
		if (gameName == null) {
			message = messages.get(messagetype);
		} else {
			message = gameMessages.get(gameName+"."+messagetype);
		}
		if (message == null) {
			message = ChatColor.DARK_RED + "  No configured message for " + messagetype;
		}
		for(Entry<String, String> it : replace.entrySet()) {
			message = message.replace(it.getKey(), it.getValue());
		}
		message = prefix+"  "+message;
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	/**
	 * Gets the message, replaces the keys, and translates the color codes.
	 * 
	 * @param replace hashmap that replaces all keys in a message with a value.
	 * @param messagetype Path to the message in the message config, without "Messages."
	 * @return The message.
	 */
	public String getReplacedMessageWithoutPrefix(HashMap<String, String> replace, String messagetype, String gameName) {
		String message;
		if (gameName == null) {
			message = messages.get(messagetype);
		} else {
			message = gameMessages.get(gameName+"."+messagetype);
		}
		if (message == null) {
			message = ChatColor.DARK_RED + "  No configured message for " + messagetype;
		}
		for(Entry<String, String> it : replace.entrySet()) {
			message = message.replace(it.getKey(), it.getValue());
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	/**
	 * Logs one or more messages to the console.
	 * 
	 * @param level the level to log the message at.
	 * @param messages the message(s) to log.
	 */
	public void log(Level level, String... messages) {
		for (String message : messages) {
			Bukkit.getLogger().log(level, "[UltimateGames] " + message);
		}
	}

	/**
	 * Sends one or more messages to a specific player.
	 * 
	 * @param playername name of the player to send the message(s) to.
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void send(String playername, String... messagetypes) {
		Player player = Bukkit.getPlayer(playername);
		if (player == null) {
			return;
		}
		for (String messagetype : messagetypes) {
			player.sendMessage(getMessage(messagetype, null));
		}
	}

	/**
	 * Sends one or more messages to a specific player, replacing certain strings.
	 * 
	 * @param playername name of the player to send the message(s) to.
	 * @param replace hashmap that replaces all keys in a message with a value.
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void send(String playername, HashMap<String, String> replace, String... messagetypes) {
		Player player = Bukkit.getPlayer(playername);
		if (player == null) {
			return;
		}
		for (String messagetype : messagetypes) {
			player.sendMessage(getReplacedMessage(replace, messagetype, null));
		}
	}
	
	/**
	 * Sends one or more game messages to a specific player.
	 * 
	 * @param gameName name of the game to get the message(s) from.
	 * @param playerName name of the player to send the message(s) to.
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void send(String gameName, String playerName, String... messagetypes) {
		Player player = Bukkit.getPlayer(playerName);
		if (player == null) {
			return;
		}
		for (String messagetype : messagetypes) {
			player.sendMessage(getMessage(messagetype, gameName));
		}
	}
	
	/**
	 * Sends one or more game messages to a specific player, replacing certain strings.
	 * 
	 * @param gameName name of the game to get the message(s) from.
	 * @param playername name of the player to send the message(s) to.
	 * @param replace hashmap that replaces all keys in a message with a value.
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void send(String gameName, String playerName, HashMap<String, String> replace, String... messagetypes) {
		Player player = Bukkit.getPlayer(playerName);
		if (player == null) {
			return;
		}
		for (String messagetype : messagetypes) {
			player.sendMessage(getReplacedMessage(replace, messagetype, gameName));
		}
	}

	/**
	 * Broadcasts one or more messages to the server.
	 * 
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void broadcast(String... messagetypes) {
		for (String messagetype : messagetypes) {
			Bukkit.getServer().broadcastMessage(getMessage(messagetype, null));
		}
	}

	/**
	 * Broadcasts one or more messages to the server, replacing certain strings.
	 * 
	 * @param replace hashmap that replaces all keys in a message with a value.
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void broadcast(HashMap<String, String> replace, String... messagetypes) {
		for (String messagetype : messagetypes) {
			Bukkit.getServer().broadcastMessage(getReplacedMessage(replace, messagetype, null));
		}
	}
	
	/**
	 * Broadcasts one or more messages to the players in an arena.
	 * 
	 * @param arenaName Name of the arena.
	 * @param gameName Name of the game.
	 * @param messagetypes the path(s) to the message(s), without "message."
	 */
	public void broadcast(String arenaName, String gameName, String... messagetypes) {
		ArrayList<String> playerNames = ultimateGames.getArenaManager().getArena(arenaName, gameName).getPlayers();
		for (String messagetype : messagetypes) {
			for (String playerName : playerNames) {
				Player player = Bukkit.getPlayer(playerName);
				if (player != null) {
					player.sendMessage(getMessage(messagetype, gameName));
				}
			}
		}
	}
	
	/**
	 * Broadcasts one or more messages to the players in an arena.
	 * 
	 * @param arenaName Name of the arena.
	 * @param gameName Name of the game.
	 * @param replace hashmap that replaces all keys in a message with a value.
	 * @param messagetypes the path(s) to the message(s), without "message."
	 */
	public void broadcast(String arenaName, String gameName, HashMap<String, String> replace, String... messagetypes) {
		ArrayList<String> playerNames = ultimateGames.getArenaManager().getArena(arenaName, gameName).getPlayers();
		for (String messagetype: messagetypes) {
			for (String playerName : playerNames) {
				Player player = Bukkit.getPlayer(playerName);
				if (player != null) {
					player.sendMessage(getReplacedMessage(replace, messagetype, gameName));
				}
			}
		}
	}

	/**
	 * Decides whether or not to print the stack trace of an exception.
	 * 
	 * @param e the exception to debug.
	 */
	public void debug(Exception e) {
		if (ultimateGames.getConfig().getBoolean("debug")) {
			e.printStackTrace();
		}
	}
}
