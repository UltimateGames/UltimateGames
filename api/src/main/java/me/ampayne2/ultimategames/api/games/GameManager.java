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

import java.util.Set;

/**
 * Manages all of the UG games.
 */
public interface GameManager {

    /**
     * Gets the games loaded.
     *
     * @return The games loaded.
     */
    Set<Game> getGames();

    /**
     * Checks if a game is loaded.
     *
     * @param gameName The name of the game.
     * @return True if the game is loaded, else false.
     */
    boolean gameExists(String gameName);

    /**
     * Checks if a game is loaded.
     *
     * @param game The game.
     * @return True if the game is loaded, else false.
     */
    boolean gameExists(Game game);

    /**
     * Gets the game of a certain name.
     *
     * @param gameName The name of the game.
     * @return The game of the certain name.
     */
    Game getGame(String gameName);
}
