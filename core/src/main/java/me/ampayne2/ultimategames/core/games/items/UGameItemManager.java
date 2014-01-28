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
package me.ampayne2.ultimategames.core.games.items;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.items.GameItem;
import me.ampayne2.ultimategames.api.games.items.GameItemManager;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages game items.
 */
public class UGameItemManager implements GameItemManager {
    private final Map<Game, Set<GameItem>> gameItems = new HashMap<>();

    /**
     * Checks if an ItemStack is registered.
     *
     * @param game The game.
     * @param item The ItemStack.
     * @return True if the ItemStack is registered, else false.
     */
    @Override
    public boolean isRegistered(Game game, ItemStack item) {
        if (gameItems.containsKey(game)) {
            for (GameItem gameItem : gameItems.get(game)) {
                if (gameItem.getItem().getType().equals(item.getType()) && gameItem.getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
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
    @Override
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
    @Override
    public GameItem getGameItem(Game game, ItemStack item) {
        if (gameItems.containsKey(game)) {
            for (GameItem gameItem : gameItems.get(game)) {
                if (gameItem.getItem().getType().equals(item.getType()) && gameItem.getItem().getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
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
    @Override
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
    @Override
    public UGameItemManager registerGameItem(Game game, GameItem gameItem) {
        if (gameItems.containsKey(game)) {
            Set<GameItem> items = gameItems.get(game);
            for (GameItem item : items) {
                if (gameItem.equals(item)) {
                    return this;
                }
            }
            items.add(gameItem);
        } else {
            Set<GameItem> items = new HashSet<>();
            items.add(gameItem);
            gameItems.put(game, items);
        }
        return this;
    }

    /**
     * Unregisters a GameItem.
     *
     * @param game The Game.
     * @param item The GameItem.
     */
    @Override
    public UGameItemManager unregisterGameItem(Game game, GameItem item) {
        if (gameItems.containsKey(game)) {
            gameItems.get(game).remove(item);
        }
        return this;
    }

    /**
     * Unregisters all of a game's GameItems.
     *
     * @param game The Game.
     */
    @Override
    public UGameItemManager unregisterGameItems(Game game) {
        gameItems.remove(game);
        return this;
    }
}
