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
package me.ampayne2.ultimategames.items;

import me.ampayne2.ultimategames.games.Game;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages special game items.
 */
public class GameItemManager {
    private final Map<Game, Set<GameItem>> gameItems = new HashMap<Game, Set<GameItem>>();

    /**
     * Checks if an ItemStack is registered.
     *
     * @param game The game.
     * @param item The ItemStack.
     * @return True if the ItemStack is registered, else false.
     */
    public boolean isRegistered(Game game, ItemStack item) {
        if (gameItems.containsKey(game)) {
            for (GameItem gameItem : gameItems.get(game)) {
                if (gameItem.getItem().equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a GameItem is registered.
     *
     * @param game The game.
     * @param item The GameItem.
     * @return True if the GameItem is registered, else false.
     */
    public boolean isRegistered(Game game, GameItem item) {
        return gameItems.containsKey(game) && gameItems.get(game).contains(item);
    }

    /**
     * Gets a GameItem of a game.
     *
     * @param game The game.
     * @param item The ItemStack of the GameItem.
     * @return The GameItem, null if none of the ItemStack exists.
     */
    public GameItem getGameItem(Game game, ItemStack item) {
        if (gameItems.containsKey(game)) {
            for (GameItem gameItem : gameItems.get(game)) {
                if (gameItem.getItem().equals(item)) {
                    return gameItem;
                }
            }
        }
        return null;
    }

    /**
     * Gets the GameItems of a game.
     *
     * @param game The game.
     * @return The GameItems of a game.
     */
    public Set<GameItem> getGameItems(Game game) {
        return gameItems.containsKey(game) ? gameItems.get(game) : new HashSet<GameItem>();
    }

    /**
     * Registers a GameItem.
     *
     * @param game     The Game.
     * @param gameItem The GameItem.
     * @return True if the GameItem was registered successfully, else false.
     */
    public boolean registerGameItem(Game game, GameItem gameItem) {
        if (gameItems.containsKey(game)) {
            Set<GameItem> items = gameItems.get(game);
            for (GameItem item : items) {
                if (gameItem.getItem().equals(item.getItem())) {
                    return false;
                }
            }
            items.add(gameItem);
        } else {
            Set<GameItem> items = new HashSet<GameItem>();
            items.add(gameItem);
            gameItems.put(game, items);
        }
        return true;
    }

    /**
     * Unregisters a GameItem.
     *
     * @param game The Game.
     * @param item The GameItem.
     */
    public void unregisterGameItem(Game game, GameItem item) {
        if (gameItems.containsKey(game)) {
            gameItems.get(game).remove(item);
        }
    }

    /**
     * Unregisters all of a game's GameItems.
     *
     * @param game The Game.
     */
    public void unregisterGameItems(Game game) {
        gameItems.remove(game);
    }
}
