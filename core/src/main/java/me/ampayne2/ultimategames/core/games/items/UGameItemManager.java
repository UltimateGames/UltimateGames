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

public class UGameItemManager implements GameItemManager {
    private final Map<Game, Set<GameItem>> gameItems = new HashMap<>();

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

    @Override
    public boolean isRegistered(Game game, GameItem item) {
        return gameItems.containsKey(game) && gameItems.get(game).contains(item);
    }

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

    @Override
    public Set<GameItem> getGameItems(Game game) {
        return gameItems.containsKey(game) ? gameItems.get(game) : new HashSet<GameItem>();
    }

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

    @Override
    public UGameItemManager unregisterGameItem(Game game, GameItem item) {
        if (gameItems.containsKey(game)) {
            gameItems.get(game).remove(item);
        }
        return this;
    }

    @Override
    public UGameItemManager unregisterGameItems(Game game) {
        gameItems.remove(game);
        return this;
    }
}
