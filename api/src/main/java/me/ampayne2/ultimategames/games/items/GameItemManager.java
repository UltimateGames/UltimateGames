/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.games.items;

import me.ampayne2.ultimategames.games.Game;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * Manages GameItems.
 */
public interface GameItemManager {

    /**
     * Checks if an ItemStack is registered.
     *
     * @param game The game.
     * @param item The ItemStack.
     * @return True if the ItemStack is registered, else false.
     */
    boolean isRegistered(Game game, ItemStack item);

    /**
     * Checks if a GameItem is registered.
     *
     * @param game The game.
     * @param item The GameItem.
     * @return True if the GameItem is registered, else false.
     */
    boolean isRegistered(Game game, GameItem item);

    /**
     * Gets a GameItem of a game.
     *
     * @param game The game.
     * @param item The ItemStack of the GameItem.
     * @return The GameItem, null if none of the ItemStack exists.
     */
    GameItem getGameItem(Game game, ItemStack item);

    /**
     * Gets the GameItems of a game.
     *
     * @param game The game.
     * @return The GameItems of a game.
     */
    Set<GameItem> getGameItems(Game game);

    /**
     * Registers a GameItem.
     *
     * @param game     The Game.
     * @param gameItem The GameItem.
     * @return True if the GameItem was registered successfully, else false.
     */
    GameItemManager registerGameItem(Game game, GameItem gameItem);

    /**
     * Unregisters a GameItem.
     *
     * @param game The Game.
     * @param item The GameItem.
     */
    GameItemManager unregisterGameItem(Game game, GameItem item);

    /**
     * Unregisters all of a game's GameItems.
     *
     * @param game The Game.
     */
    GameItemManager unregisterGameItems(Game game);
}
