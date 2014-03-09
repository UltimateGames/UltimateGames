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
package me.ampayne2.ultimategames.api.games;

import java.util.List;

/**
 * Holds a GamePlugin and all of a game's information.
 */
public interface Game {

    /**
     * Checks to see if a game is enabled.
     *
     * @return True if the game is enabled.
     */
    boolean isEnabled();

    /**
     * Disables a game.
     */
    void disable();

    /**
     * Gets the GamePlugin of the game.
     * @return The game's GamePlugin.
     */
    GamePlugin getGamePlugin();

    /**
     * Gets the name of the game.
     *
     * @return The name.
     */
    String getName();

    /**
     * Gets the description of the game.
     *
     * @return The description.
     */
    String getDescription();

    /**
     * Gets the version string of the game.
     *
     * @return The version.
     */
    String getVersion();

    /**
     * Gets the author of the game.
     *
     * @return The author.
     */
    String getAuthor();

    /**
     * Gets the game's plugin dependencies.<br>
     * Works the same as bukkit's plugin dependency system.
     *
     * @return The game's plugin dependencies.
     */
    List<String> getDepend();

    /**
     * Gets the PlayerType of the game.
     *
     * @return The player type.
     */
    PlayerType getPlayerType();

    /**
     * Gets the instruction pages of the game.
     *
     * @return The instruction pages.
     */
    List<String> getInstructionPages();

    Class<?> getMessages();

    void setMessages(Class<?> messages);
}
