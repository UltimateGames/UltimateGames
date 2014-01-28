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
package me.ampayne2.ultimategames.api.players.classes;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.utils.IconMenu;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Manages player classes for games.
 */
public interface GameClassManager {

    /**
     * Checks if a GameClass is registered.
     *
     * @param game      The game.
     * @param gameClass The GameClass.
     * @return True if the GameClass is registered, else false.
     */
    boolean isRegistered(Game game, GameClass gameClass);

    /**
     * Gets a GameClass of a game.
     *
     * @param game The game.
     * @param name The name of the GameClass.
     * @return The GameClass, null if none by the name exist.
     */
    GameClass getGameClass(Game game, String name);

    /**
     * Gets the GameClasses of a game.
     *
     * @param game The game.
     * @return The GameClasses of a game.
     */
    List<GameClass> getGameClasses(Game game);

    /**
     * Registers a GameClass.
     *
     * @param gameClass The GameClass.
     * @return True if the GameClass was registered successfully, else false.
     */
    GameClassManager registerGameClass(GameClass gameClass);

    /**
     * Unregisters a GameClass.
     *
     * @param gameClass The GameClass.
     */
    GameClassManager unregisterGameClass(GameClass gameClass);

    /**
     * Unregisters all of a game's GameClasses.
     *
     * @param game The Game.
     */
    GameClassManager unregisterGameClasses(Game game);

    /**
     * Gets a player's GameClass.
     *
     * @param game       The game of the player.
     * @param playerName The player's name.
     * @return The GameClass, null if the player isn't in one.
     */
    GameClass getPlayerClass(Game game, String playerName);

    /**
     * Gets the class selector of a game.
     *
     * @param game   The class selector of a game.
     * @param player The player of the class selector.
     * @return The player's class selector of a game.
     */
    IconMenu getClassSelector(Game game, final Player player);
}
