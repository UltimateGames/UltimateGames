/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.message;

import me.ampayne2.ultimategames.api.config.ConfigType;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.message.Message;
import me.ampayne2.ultimategames.api.message.Messenger;
import me.ampayne2.ultimategames.api.message.RecipientHandler;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.core.UG;
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
    private Map<String, Map<String, String>> gameMessages = new HashMap<>();
    private Map<Class<?>, RecipientHandler> recipientHandlers = new HashMap<>();

    /**
     * Creates a new message manager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UMessenger(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        debug = ultimateGames.getConfig().getBoolean("debug", false);
        log = ultimateGames.getLogger();
        loadMessages();
    }

    /**
     * Loads the ultimate games messages.
     */
    public void loadMessages() {
        FileConfiguration messageConfig = ultimateGames.getConfigManager().getConfig(ConfigType.MESSAGE);
        for (Message message : UGMessage.class.getEnumConstants()) {
            messageConfig.addDefault(message.getPath(), message.getDefault());
        }
        messageConfig.options().copyDefaults(true);
        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.MESSAGE).saveConfig();
        for (Message message : UGMessage.class.getEnumConstants()) {
            message.setMessage(ChatColor.translateAlternateColorCodes('&', messageConfig.getString(message.getPath())));
        }
    }

    /**
     * Loads a game's messages.
     *
     * @param game The game whose messages to load.
     */
    public void loadGameMessages(Game game, Class<?> messages) {
        FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game);
        for (Message message : (Message[]) messages.getEnumConstants()) {
            gameConfig.addDefault("Messages." + message.getPath(), message.getDefault());
        }
        gameConfig.options().copyDefaults(true);
        ultimateGames.getConfigManager().getGameConfigAccessor(game).saveConfig();
        for (Message message : (Message[]) messages.getEnumConstants()) {
            message.setMessage(ChatColor.translateAlternateColorCodes('&', gameConfig.getString("Messages." + message.getPath())));
        }
    }

    @Override
    public Messenger registerRecipient(Class recipientClass, RecipientHandler recipientHandler) {
        recipientHandlers.put(recipientClass, recipientHandler);
        return this;
    }

    @Override
    public String getGamePrefix(Game game) {
        return String.format(UGMessage.GAME_PREFIX.getMessage(), game.getName());
    }

    @Override
    public boolean sendPlayerChatMessage(String playerName, String message) {
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Object recipient;
            ChatColor nameColor;
            if (ultimateGames.getTeamManager().isPlayerInTeam(playerName)) {
                Team team = ultimateGames.getTeamManager().getPlayerTeam(playerName);
                recipient = team;
                nameColor = team.getColor();
            } else {
                recipient = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                nameColor = ChatColor.WHITE;
            }
            return sendRawMessage(recipient, String.format(UGMessage.CHAT.getMessage(), nameColor + playerName, message));
        } else {
            return false;
        }
    }

    @Override
    public boolean sendMessage(Object recipient, Message message, String... replace) {
        return sendRawMessage(recipient, UGMessage.PREFIX + (replace == null ? message.getMessage() : String.format(message.getMessage(), (Object[]) replace)));
    }

    @Override
    public boolean sendGameMessage(Object recipient, Game game, Message message, String... replace) {
        return sendRawMessage(recipient, getGamePrefix(game) + (replace == null ? message.getMessage() : String.format(message.getMessage(), (Object[]) replace)));
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
