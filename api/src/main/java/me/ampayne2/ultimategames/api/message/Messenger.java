/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.api.message;

import me.ampayne2.ultimategames.api.games.Game;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages UltimateGames message sending, logging, and debugging.
 */
public interface Messenger {

    /**
     * Registers a recipient with a RecipientHandler.
     *
     * @param recipientClass   The recipient's class.
     * @param recipientHandler The RecipientHandler.
     * @return The Messenger instance.
     */
    Messenger registerRecipient(Class recipientClass, RecipientHandler recipientHandler);

    /**
     * Gets a game's message prefix.
     *
     * @param game The game.
     * @return The game's message prefix.
     */
    String getGamePrefix(Game game);

    /**
     * Sends a message from the player to the arena the player is in.
     *
     * @param sender  The name of the player.
     * @param message The message.
     * @return True if the player is in an arena and the message was sent, else false.
     */
    boolean sendPlayerChatMessage(String sender, String message);

    /**
     * Sends a message to a recipient.
     *
     * @param recipient The recipient of the message.
     * @param message   The message.
     * @param replace   Strings to replace any occurences of %s in the message with.
     * @return True if the message was sent, else false.
     */
    boolean sendMessage(Object recipient, Message message, String... replace);

    /**
     * Sends a game message to a recipient.
     *
     * @param recipient The recipient of the message; Either CommandSender, Team, Arena, or Server.
     * @param game      The game.
     * @param message   The message.
     * @param replace   Strings to replace any occurences of %s in the message with.
     * @return True if the message was sent, else false.
     */
    boolean sendGameMessage(Object recipient, Game game, Message message, String... replace);

    /**
     * Sends a raw message to a recipient.
     *
     * @param recipient The recipient of the message. Type of recipient must be registered.
     * @param message   The message.
     * @return True if the message was sent, else false.
     */
    boolean sendRawMessage(Object recipient, String message);

    /**
     * Logs one or more messages to the console.
     *
     * @param level    the level to log the message at.
     * @param messages the message(s) to log.
     */
    void log(Level level, String... messages);

    /**
     * Decides whether or not to print the stack trace of an exception.
     *
     * @param e the exception to debug.
     */
    void debug(Exception e);

    /**
     * Decides whether or not to print a debug message.
     *
     * @param message the message to debug.
     */
    void debug(String message);

    /**
     * Gets the logger.
     *
     * @return The logger.
     */
    Logger getLogger();
}
