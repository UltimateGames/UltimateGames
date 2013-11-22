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
package me.ampayne2.ultimategames.message;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Message {
    private final UltimateGames ultimateGames;
    private final boolean debug;
    private final String messagePrefix;
    private final Logger log;
    private Map<String, String> messages = new HashMap<String, String>();
    private Map<String, Map<String, String>> gameMessages = new HashMap<String, Map<String, String>>();
    private Map<Class<?>, MessageRecipient> recipients = new HashMap<Class<?>, MessageRecipient>();

    public Message(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        debug = ultimateGames.getConfig().getBoolean("debug");
        String prefix = messages.get("prefix");
        messagePrefix = ChatColor.translateAlternateColorCodes('&', prefix == null ? "&8[&bUltimateGames&8] " : prefix);
        log = ultimateGames.getLogger();
        loadMessages();
    }

    public void loadMessages() {
        FileConfiguration messageConfig = ultimateGames.getConfigManager().getMessageConfig().getConfig();
        for (String key : messageConfig.getConfigurationSection("Messages").getKeys(true)) {
            messages.put(key, ChatColor.translateAlternateColorCodes('&', messageConfig.getString("Messages." + key)));
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
                messages.put(key, ChatColor.translateAlternateColorCodes('&', gameConfig.getString("Messages." + key)));
            }
            gameMessages.put(game.getName(), messages);
        }
    }

    public Message registerRecipient(Class recipientClass, MessageRecipient recipient) {
        recipients.put(recipientClass, recipient);
        return this;
    }

    /**
     * Gets the message prefix.
     *
     * @return The message prefix.
     */
    public String getPrefix() {
        return messagePrefix;
    }

    /**
     * Gets a message with translated color codes.
     *
     * @param path Path to the message in the message config, without "Messages."
     * @return The message.
     */
    public String getMessage(String path) {
        String message = messages.get(path);
        if (message == null) {
            message = ChatColor.DARK_RED + "No configured message for " + path;
        }
        return message;
    }

    /**
     * Gets a game message with translated color codes.
     *
     * @param game The game.
     * @param path Path to the message in the game's config, without "Messages."
     * @return The message.
     */
    public String getGameMessage(Game game, String path) {
        Map<String, String> messages = gameMessages.get(game.getName());
        String message = messages == null ? null : messages.get(path);
        if (message == null) {
            message = ChatColor.DARK_RED + "No configured message for " + path;
        }
        return message;
    }

    /**
     * Sends a message to a recipient.
     *
     * @param recipient The recipient of the message; Either CommandSender, Team, Arena, or Server.
     * @param path      The path to the message.
     * @param replace   Strings to replace any occurences of %s in the message with.
     * @return True if the message was sent, else false.
     */
    public boolean sendMessage(Object recipient, String path, String... replace) {
        return sendRawMessage(recipient, messagePrefix + (replace == null ? getMessage(path) : String.format(getMessage(path), (Object[]) replace)));
    }

    /**
     * Sends a game message to a recipient.
     *
     * @param recipient The recipient of the message; Either CommandSender, Team, Arena, or Server.
     * @param game      The game.
     * @param path      The path to the message.
     * @param replace   Strings to replace any occurences of %s in the message with.
     * @return True if the message was sent, else false.
     */
    public boolean sendGameMessage(Object recipient, Game game, String path, String... replace) {
        return sendRawMessage(recipient, messagePrefix + (replace == null ? getGameMessage(game, path) : String.format(getGameMessage(game, path), (Object[]) replace)));
    }

    /**
     * Sends a raw message to a recipient.
     *
     * @param recipient The recipient of the message. Type of recipient must be registered.
     * @param message   The message.
     * @return True if the message was sent, else false.
     */
    public boolean sendRawMessage(Object recipient, String message) {
        if (recipient != null && message != null) {
            for (Class<?> recipientClass : recipients.keySet()) {
                if (recipientClass.isAssignableFrom(recipient.getClass())) {
                    recipients.get(recipientClass).sendMessage(recipient, message);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Logs one or more messages to the console.
     *
     * @param level    the level to log the message at.
     * @param messages the message(s) to log.
     */
    public void log(Level level, String... messages) {
        for (String message : messages) {
            log.log(level, message);
        }
    }

    /**
     * Decides whether or not to print the stack trace of an exception.
     *
     * @param e the exception to debug.
     */
    public void debug(Exception e) {
        if (debug) {
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
        if (debug) {
            log.log(Level.INFO, message);
        }
    }

    /**
     * Gets the logger.
     *
     * @return The logger.
     */
    public Logger getLogger() {
        return log;
    }
}
