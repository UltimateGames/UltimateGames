/*
 * This file is part of UltimateGames ENGINE.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames ENGINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames ENGINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames ENGINE.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.message;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.config.ConfigType;
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

public class UMessenger implements Messenger {
    private final UG ultimateGames;
    private final boolean debug;
    private final Logger log;
    private final String messagePrefix;
    private final String gamePrefix;
    private Map<String, String> messages = new HashMap<>();
    private Map<String, Map<String, String>> gameMessages = new HashMap<>();
    private Map<Class<?>, RecipientHandler> recipientHandlers = new HashMap<>();

    /**
     * Creates a new message manager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public UMessenger(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        debug = ultimateGames.getConfig().getBoolean("debug", false);
        log = ultimateGames.getLogger();
        loadMessages();
        messagePrefix = messages.containsKey("prefix") ? messages.get("prefix") : ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "UltimateGames" + ChatColor.DARK_GRAY + "]";
        gamePrefix = messages.containsKey("gameprefix") ? messages.get("gameprefix") : ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "%s" + ChatColor.DARK_GRAY + "]";
    }

    /**
     * Loads the ultimate games messages.
     */
    public void loadMessages() {
        messages.clear();
        FileConfiguration messageConfig = ultimateGames.getConfigManager().getConfig(ConfigType.MESSAGE);
        for (String key : messageConfig.getConfigurationSection("Messages").getKeys(true)) {
            messages.put(key, ChatColor.translateAlternateColorCodes('&', messageConfig.getString("Messages." + key)));
        }
    }

    /**
     * Loads a game's messages.
     *
     * @param game The game whose messages to load.
     */
    public void loadGameMessages(Game game) {
        FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game);
        if (gameConfig.isConfigurationSection("Messages")) {
            gameMessages.remove(game.getName());
            Map<String, String> messages = new HashMap<>();
            for (String key : gameConfig.getConfigurationSection("Messages").getKeys(true)) {
                messages.put(key, ChatColor.translateAlternateColorCodes('&', gameConfig.getString("Messages." + key)));
            }
            gameMessages.put(game.getName(), messages);
        }
    }

    @Override
    public Messenger registerRecipient(Class recipientClass, RecipientHandler recipientHandler) {
        recipientHandlers.put(recipientClass, recipientHandler);
        return this;
    }

    @Override
    public String getPrefix() {
        return messagePrefix;
    }

    @Override
    public String getGamePrefix(Game game) {
        return String.format(gamePrefix, game.getName());
    }

    @Override
    public String getMessage(String path) {
        return messages.containsKey(path) ? messages.get(path) : ChatColor.DARK_RED + "No configured message for " + path;
    }

    @Override
    public String getGameMessage(Game game, String path) {
        if (gameMessages.containsKey(game.getName())) {
            Map<String, String> messages = gameMessages.get(game.getName());
            if (messages.containsKey(path)) {
                return messages.get(path);
            }
        }
        return ChatColor.DARK_RED + "No configured message for " + path;
    }

    @Override
    public boolean sendMessage(Object recipient, String path, String... replace) {
        return sendRawMessage(recipient, messagePrefix + (replace == null ? getMessage(path) : String.format(getMessage(path), (Object[]) replace)));
    }

    @Override
    public boolean sendGameMessage(Object recipient, Game game, String path, String... replace) {
        return sendRawMessage(recipient, getGamePrefix(game) + (replace == null ? getGameMessage(game, path) : String.format(getGameMessage(game, path), (Object[]) replace)));
    }

    @Override
    public boolean sendRawMessage(Object recipient, String message) {
        if (recipient != null && message != null) {
            for (Class<?> recipientClass : recipientHandlers.keySet()) {
                if (recipientClass.isAssignableFrom(recipient.getClass())) {
                    recipientHandlers.get(recipientClass).sendMessage(recipient, message);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void log(Level level, String... messages) {
        for (String message : messages) {
            log.log(level, message);
        }
    }

    @Override
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

    @Override
    public void debug(String message) {
        if (debug) {
            log.log(Level.INFO, message);
        }
    }

    @Override
    public Logger getLogger() {
        return log;
    }
}
