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
package me.ampayne2.ultimategames;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Message {
	private final UltimateGames ultimateGames;
	private Map<String, String> messages = new HashMap<String, String>();
	private Map<String, Map<String, String>> gameMessages = new HashMap<String, Map<String, String>>();

	public Message(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		FileConfiguration messageConfig = ultimateGames.getConfigManager().getMessageConfig().getConfig();
		for (String key : messageConfig.getConfigurationSection("Messages").getKeys(true)) {
			messages.put(key, messageConfig.getString("Messages." + key));
		}
	}

	/**
	 * Loads a game's messages.
	 *
	 * @param game The game.
	 */
	public void loadGameMessages(Game game) {
		FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
		if (gameConfig.isConfigurationSection("Messages")) {
			gameMessages.remove(game.getName());
			Map<String, String> messages = new HashMap<String, String>();
			for (String key : gameConfig.getConfigurationSection("Messages").getKeys(true)) {
				messages.put(key, gameConfig.getString("Messages." + key));
			}
			gameMessages.put(game.getName(), messages);
		}
	}

	/**
	 * Gets the message prefix.
	 *
	 * @return The message prefix.
	 */
	public String getMessagePrefix() {
		String prefix = messages.get("prefix");
		if (prefix == null) {
			prefix = "&8[&bUltimateGames&8] ";
		}
		return ChatColor.translateAlternateColorCodes('&', prefix);
	}

	/**
	 * Gets a message with translated color codes.
	 *
	 * @param path Path to the message in the message config, without "Messages."
	 *
	 * @return The message.
	 */
	public String getMessage(String path) {
		String message = messages.get(path);
		if (message == null) {
			message = ChatColor.DARK_RED + "No configured message for " + path;
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Gets a game message with translated color codes.
	 *
	 * @param game The game.
	 * @param path Path to the message in the game's config, without "Messages."
	 *
	 * @return The message.
	 */
	public String getGameMessage(Game game, String path) {
		Map<String, String> messages = gameMessages.get(game.getName());
		String message = messages == null ? null : messages.get(path);
		if (message == null) {
			message = ChatColor.DARK_RED + "No configured message for " + path;
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	/**
	 * Sends a message to a recipient.
	 *
	 * @param recipient The recipient of the message; Either Player, Team, Arena, or Server.
	 * @param path      The path to the message.
	 * @param replace   Strings to replace any occurences of %s in the message with.
	 *
	 * @return True if the message was sent, else false.
	 */
	public boolean sendMessage(Object recipient, String path, String... replace) {
		return sendRawMessage(recipient, getMessagePrefix() + (replace == null ? getMessage(path) : String.format(getMessage(path), (Object[]) replace)));
	}

	/**
	 * Sends a game message to a recipient.
	 *
	 * @param recipient The recipient of the message; Either Player, Team, Arena, or Server.
	 * @param game      The game.
	 * @param path      The path to the message.
	 * @param replace   Strings to replace any occurences of %s in the message with.
	 *
	 * @return True if the message was sent, else false.
	 */
	public boolean sendGameMessage(Object recipient, Game game, String path, String... replace) {
		return sendRawMessage(recipient, getMessagePrefix() + (replace == null ? getGameMessage(game, path) : String.format(getGameMessage(game, path), (Object[]) replace)));
	}

	/**
	 * Sends a raw message to a recipient.
	 *
	 * @param recipient The recipient of the message; Either Player, Team, Arena, or Server.
	 * @param message   The message.
	 *
	 * @return True if the message was sent, else false.
	 */
	public boolean sendRawMessage(Object recipient, String message) {
		if (recipient instanceof CommandSender) {
			((CommandSender) recipient).sendMessage(message);
		} else if (recipient instanceof Team) {
			for (String playerName : ((Team) recipient).getPlayers()) {
				Player player = Bukkit.getPlayerExact(playerName);
				if (player != null) {
					player.sendMessage(message);
				}
			}
		} else if (recipient instanceof Arena) {
			Arena arena = (Arena) recipient;
			List<String> players = new ArrayList<String>();
			players.addAll(arena.getPlayers());
			players.addAll(arena.getSpectators());
			for (String playerName : players) {
				Player player = Bukkit.getPlayerExact(playerName);
				if (player != null) {
					player.sendMessage(message);
				}
			}
		} else if (recipient instanceof Server) {
			((Server) recipient).broadcastMessage(message);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Logs one or more messages to the console.
	 *
	 * @param level    the level to log the message at.
	 * @param messages the message(s) to log.
	 */
	public void log(Level level, String... messages) {
		for (String message : messages) {
			ultimateGames.getLogger().log(level, message);
		}
	}

	/**
	 * Decides whether or not to print the stack trace of an exception.
	 *
	 * @param e the exception to debug.
	 */
	public void debug(Exception e) {
		if (ultimateGames.getConfig().getBoolean("debug")) {
			Logger log = ultimateGames.getLogger();
			log.severe("");
			log.severe("Internal error!");
			log.severe("If this bug hasn't been reported please open a ticket at https://github.com/ampayne2/UltimateGames/issues");
			log.severe("Include the following into your bug report:");
			log.severe(" ======= SNIP HERE =======");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			for (String l : sw.toString().replace("\r", "").split("\n")) {
				log.severe(l);
			}
			pw.close();
			try {
				sw.close();
			} catch (IOException e1) {
				log.log(Level.SEVERE, "An error occured in debugging an exception", e);
			}
			log.severe(" ======= SNIP HERE =======");
			log.severe("");
		}
	}

	/**
	 * Decides whether or not to print a debug message.
	 *
	 * @param message the message to debug.
	 */
	public void debug(String message) {
		if (ultimateGames.getConfig().getBoolean("debug")) {
			ultimateGames.getLogger().log(Level.INFO, message);
		}
	}
}
