package me.ampayne2.UltimateGames;

import java.util.HashMap;
import java.util.Iterator;
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

	public Message(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		FileConfiguration messageConfig = ultimateGames.getConfigManager().getMessageConfig().getConfig();
		for (String key : messageConfig.getConfigurationSection("messages").getKeys(false)) {
			messages.put(key, messageConfig.getString("messages."+key));
		}
	}

	/**
	 * Logs one or more messages to the console
	 * 
	 * @param level the level to log the message at
	 * @param messages the message(s) to log
	 */
	public void log(Level level, String... messages) {
		for (String message : messages) {
			Bukkit.getLogger().log(level, "[UltimateGames] " + message);
		}
	}

	/**
	 * Sends one or more messages to a specific player
	 * 
	 * @param playername name of the player to send the message(s) to
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void send(String playername, String... messagetypes) {
		Player player = Bukkit.getPlayer(playername);
		String prefix = messages.get("prefix");
		if (player == null) {
			return;
		}
		if (prefix == null) {
			prefix = "&8[&bUltimateGames&8]";
		}
		for (String messagetype : messagetypes) {
			String message = messages.get(messagetype);
			if (message == null) {
				player.sendMessage(prefix + ChatColor.DARK_RED + "  No configured message for " + messagetype);
			}
			message = prefix.concat(" " + message);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}

	/**
	 * Sends one or more messages to a specific player, replacing certain strings
	 * 
	 * @param playername name of the player to send the message(s) to
	 * @param replace hashmap that replaces all keys in a message with a value
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void send(String playername, HashMap<String, String> replace, String... messagetypes) {
		Player player = Bukkit.getPlayer(playername);
		String prefix = messages.get("prefix");
		if (player == null) {
			return;
		}
		if (prefix == null) {
			prefix = "&8[&bUltimateGames&8]";
		}
		for (String messagetype : messagetypes) {
			String message = messages.get(messagetype);
			if (message == null) {
				player.sendMessage(prefix + ChatColor.DARK_RED + "  No configured message for " + messagetype);
			}
			Iterator<Entry<String, String>> it = replace.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> str = it.next();
				message = message.replace(str.getKey(), str.getValue());
			}
			message = prefix.concat(" " + message);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}

	/**
	 * Broadcasts one or more messages to the server
	 * 
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void broadcast(String... messagetypes) {
		String prefix = messages.get("prefix");
		if (prefix == null) {
			prefix = "&8[&bUltimateGames&8]";
		}
		for (String messagetype : messagetypes) {
			String message = messages.get(messagetype);
			if (message == null) {
				Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&4" + "  No configured message for " + messagetype));
			}
			message = prefix.concat(" " + message);
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}

	/**
	 * Broadcasts one or more messages to the server, replacing certain strings
	 * 
	 * @param replace hashmap that replaces all keys in a message with a value
	 * @param messagetypes the path(s) to the message(s), without "messages."
	 */
	public void broadcast(HashMap<String, String> replace, String... messagetypes) {
		String prefix = messages.get("prefix");
		if (prefix == null) {
			prefix = "&8[&bUltimateGames&8]";
		}
		for (String messagetype : messagetypes) {
			String message = messages.get(messagetype);
			if (message == null) {
				Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&4" + "  No configured message for " + messagetype));
			}
			Iterator<Entry<String, String>> it = replace.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> str = it.next();
				message = message.replace(str.getKey(), str.getValue());
			}
			message = prefix.concat(" " + message);
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}

	/**
	 * Decides whether or not to print the stack trace of an exception
	 * 
	 * @param e the exception to debug
	 */
	public void debug(Exception e) {
		if (ultimateGames.getConfig().getBoolean("debug")) {
			e.printStackTrace();
		}
	}
}
