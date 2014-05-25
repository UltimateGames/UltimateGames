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
package me.ampayne2.ultimategames.api.players.points;

import me.ampayne2.ultimategames.api.games.Game;

/**
 * The Point Manager. Needs a hook to do something.
 */
public interface PointManager {

    /**
     * Adds a point to a player.
     *
     * @param game       The game of the point.
     * @param playerName The name of the player.
     * @param valueName  The name of the type of point.
     * @param amount     The amount of points.
     */
    void addPoint(Game game, String playerName, String valueName, int amount);

    /**
     * Checks if a player has a certain perk.
     *
     * @param game       The game of the perk.
     * @param playerName The name of the player.
     * @param valueName  The name of the perk.
     * @return True if the player has the perk, else false.
     */
    boolean hasPerk(Game game, String playerName, String valueName);

    /**
     * Checks if a player has a certain perk.
     *
     * @param game         The game of the perk.
     * @param playerName   The name of the player.
     * @param valueName    The name of the perk.
     * @param defaultValue The default perk state.
     * @return True if the player has the perk, else the default value.
     */
    boolean hasPerk(Game game, String playerName, String valueName, boolean defaultValue);

    /**
     * Checks if a player has a certain perk selected.
     *
     * @param game       The game of the perk.
     * @param playerName The name of the player.
     * @param valueName  The name of the perk.
     * @return True if the player has the perk selected, else false.
     */
    boolean hasPerkSelected(Game game, String playerName, String valueName);

    /**
     * Checks if a player has a certain perk selected.
     *
     * @param game         The game of the perk.
     * @param playerName   The name of the player.
     * @param valueName    The name of the perk.
     * @param defaultValue The default perk state.
     * @return True if the player has the perk selected, else the default value.
     */
    boolean hasPerkSelected(Game game, String playerName, String valueName, boolean defaultValue);
}
