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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Message implements Manager {
    
    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private Map<String, String> messages = new HashMap<String, String>();
    private Map<String, String> gameMessages = new HashMap<String, String>();

    public Message(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }
    
    @Override
    public boolean load() {
        return reload();
    }

    @Override
    public boolean reload() {
        loadMessages();
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    public void loadMessages() {
        if (!messages.isEmpty()) {
            messages.clear();
        }
        FileConfiguration messageConfig = ultimateGames.getConfigManager().getMessageConfig().getConfig();
        for (String key : messageConfig.getConfigurationSection("messages").getKeys(true)) {
            messages.put(key, messageConfig.getString("messages." + key));
        }
    }

    public void loadGameMessages() {
        if (!gameMessages.isEmpty()) {
            gameMessages.clear();
        }
        for (Game game : ultimateGames.getGameManager().getGames()) {
            FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
            if (gameConfig.isConfigurationSection("Messages")) {
                for (String key : gameConfig.getConfigurationSection("Messages").getKeys(true)) {
                    gameMessages.put(game.getName() + "." + key, gameConfig.getString("Messages." + key));
                }
            }
        }
    }

    /**
     * Gets the message prefix.
     * @return The message prefix.
     */
    public String getMessagePrefix() {
        String prefix = messages.get("prefix");
        if (prefix == null) {
            prefix = "&8[&bUltimateGames&8]";
        }
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    /**
     * Gets a message with translated color codes.
     * @param messageType Path to the message in the message config, without "Messages."
     * @return The message.
     */
    public String getMessage(String messageType) {
        String message = messages.get(messageType);
        if (message == null) {
            message = ChatColor.DARK_RED + "No configured message for " + messageType;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Gets a game message with translated color codes.
     * @param game The game.
     * @param messageType Path to the message in the game's config, without "Messages."
     * @return The message.
     */
    public String getGameMessage(Game game, String messageType) {
        String message = gameMessages.get(game.getName() + "." + messageType);
        if (message == null) {
            message = ChatColor.DARK_RED + "No configured message for " + messageType;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Sends a message to a specific player.
     * @param playerName Name of the player to send the message to.
     * @param messageType Path to the message, without "Messages."
     */
    public void sendMessage(Player player, String messageType) {
        if (loaded) {
            player.sendMessage(getMessagePrefix() + " " + getMessage(messageType));
        }
    }

    /**
     * Sends a message to a specific player, replacing certain strings.
     * @param playerName Name of the player to send the message to.
     * @param messageType Path to the message, without "Messages."
     * @param replace Strings to replace %s with.
     */
    public void sendReplacedMessage(Player player, String messageType, String... replace) {
        if (loaded) {
            player.sendMessage(getMessagePrefix() + " " + String.format(getMessage(messageType), (Object[]) replace));
        }
    }

    /**
     * Sends a game message to a specific player.
     * @param game The game.
     * @param playerName Name of the player to send the message to.
     * @param messageType Path to the message in the game's config, without "Messages."
     */
    public void sendGameMessage(Game game, Player player, String messageType) {
        if (loaded) {
            player.sendMessage(getMessagePrefix() + " " + getGameMessage(game, messageType));
        }
    }

    /**
     * Sends a message to a specific player, replacing certain strings.
     * @param game The game.
     * @param playerName Name of the player to send the message to.
     * @param messageType Path to the message, without "Messages."
     * @param replace Strings to replace %s with.
     */
    public void sendReplacedGameMessage(Game game, Player player, String messageType, String... replace) {
        if (loaded) {
            player.sendMessage(getMessagePrefix() + " " + String.format(getGameMessage(game, messageType), (Object[]) replace));
        }
    }

    /**
     * Broadcasts a message to the server.
     * @param messageType Path to the message, without "Messages."
     */
    public void broadcastMessage(String messageType) {
        if (loaded) {
            Bukkit.getServer().broadcastMessage(getMessagePrefix() + " " + getMessage(messageType));
        }
    }

    /**
     * Broadcasts a message to the server, replacing certain strings.
     * @param messageType Path to the message, without "Messages."
     * @param replace Strings to replace %s with.
     */
    public void broadcastReplacedMessage(String messageType, String... replace) {
        if (loaded) {
            Bukkit.getServer().broadcastMessage(getMessagePrefix() + " " + String.format(getMessage(messageType), (Object[]) replace));
        }
    }

    /**
     * Broadcasts a game's message to the server.
     * @param game The game.
     * @param messageType Path to the message, without "Messages."
     */
    public void broadcastGameMessage(Game game, String messageType) {
        if (loaded) {
            Bukkit.getServer().broadcastMessage(getMessagePrefix() + " " + getGameMessage(game, messageType));
        }
    }

    /**
     * Broadcasts a game's message to the server, replacing certain strings.
     * @param game The game.
     * @param messageType Path to the message, without "Messages."
     * @param replace Strings to replace %s with.
     */
    public void broadcastReplacedGameMessage(Game game, String messageType, String... replace) {
        if (loaded) {
            Bukkit.getServer().broadcastMessage(getMessagePrefix() + " " + String.format(getGameMessage(game, messageType), (Object[]) replace));
        }
    }

    /**
     * Broadcasts a message to an arena.
     * @param arena Arena to broadcast the message to.
     * @param messageType Path to the message, without "Messages."
     */
    public void broadcastMessageToArena(Arena arena, String messageType) {
        if (loaded) {
            List<String> players =  new ArrayList<String>();
            players.addAll(arena.getPlayers());
            players.addAll(arena.getSpectators());
            for (String playerName : players) {
                Player player = Bukkit.getPlayerExact(playerName);
                if (player == null) {
                    return;
                }
                player.sendMessage(getMessagePrefix() + " " + getMessage(messageType));
            }
        }
    }

    /**
     * Broadcasts a message to an arena, replacing certain strings.
     * @param arena Arena to broadcast the message to.
     * @param messageType Path to the message, without "Messages."
     * @param replace Strings to replace %s with.
     */
    public void broadcastReplacedMessageToArena(Arena arena, String messageType, String... replace) {
        if (loaded) {
            List<String> players =  new ArrayList<String>();
            players.addAll(arena.getPlayers());
            players.addAll(arena.getSpectators());
            for (String playerName : players) {
                Player player = Bukkit.getPlayerExact(playerName);
                if (player == null) {
                    return;
                }
                player.sendMessage(getMessagePrefix() + " " + String.format(getMessage(messageType), (Object[]) replace));
            }
        }
    }

    /**
     * Broadcasts a game's message to an arena.
     * @param game The game.
     * @param arena Arena to broadcast the message to.
     * @param messageType Path to the message, without "Messages."
     */
    public void broadcastGameMessageToArena(Game game, Arena arena, String messageType) {
        if (loaded) {
            List<String> players =  new ArrayList<String>();
            players.addAll(arena.getPlayers());
            players.addAll(arena.getSpectators());
            for (String playerName : players) {
                Player player = Bukkit.getPlayerExact(playerName);
                if (player == null) {
                    return;
                }
                player.sendMessage(getMessagePrefix() + " " + getGameMessage(game, messageType));
            }
        }
    }

    /**
     * Broadcasts a game's message to an arena, replacing certain strings.
     * @param game The game.
     * @param arena Arena to broadcast the message to.
     * @param messageType Path to the message, without "Messages."
     * @param replace Strings to replace %s with.
     */
    public void broadcastReplacedGameMessageToArena(Game game, Arena arena, String messageType, String... replace) {
        if (loaded) {
            List<String> players =  new ArrayList<String>();
            players.addAll(arena.getPlayers());
            players.addAll(arena.getSpectators());
            for (String playerName : players) {
                Player player = Bukkit.getPlayerExact(playerName);
                if (player == null) {
                    return;
                }
                player.sendMessage(getMessagePrefix() + " " + String.format(getGameMessage(game, messageType), (Object[]) replace));
            }
        }
    }

    /**
     * Logs one or more messages to the console.
     * @param level the level to log the message at.
     * @param messages the message(s) to log.
     */
    public void log(Level level, String... messages) {
        for (String message : messages) {
            Bukkit.getLogger().log(level, "[ultimategames] " + message);
        }
    }

    /**
     * Decides whether or not to print the stack trace of an exception.
     * @param e the exception to debug.
     */
    public void debug(Exception e) {
        if (ultimateGames.getConfig().getBoolean("debug")) {
            Logger log = Bukkit.getLogger();
            log.severe("");
            log.severe("Internal error!");
            log.severe("If this bug hasn't been reported please open a ticket at https://github.com/ampayne2/UltimateGames/issues");
            log.severe("Include the following into your bug report:");
            log.severe(" ======= SNIP HERE =======");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            for (String l: sw.toString().replace("\r", "").split("\n")) {
               	log.severe(l);
            }
            pw.close();
            try {
            	sw.close();
            } catch (IOException e1) {
                ultimateGames.getLogger().log(Level.SEVERE, "An error occured in debugging an exception", e);
            }
            log.severe(" ======= SNIP HERE =======");
            log.severe("");
        }
    }
    
    /**
     * Decides whether or not to print a debug message.
     * @param message the message to debug.
     */
    public void debug(String message) {
        if (ultimateGames.getConfig().getBoolean("debug")) {
            Logger log = Bukkit.getLogger();
            log.log(Level.INFO, message);
        }
    }
}
