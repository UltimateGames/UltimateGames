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
package me.ampayne2.ultimategames.core.games.blocks;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.blocks.GameBlock;
import me.ampayne2.ultimategames.api.games.blocks.GameBlockManager;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UGameBlockManager implements GameBlockManager {
    private final Map<Game, Set<GameBlock>> gameBlocks = new HashMap<>();

    @Override
    public boolean isRegistered(Game game, Material material) {
        if (gameBlocks.containsKey(game)) {
            for (GameBlock gameBlock : gameBlocks.get(game)) {
                if (gameBlock.getMaterial().equals(material)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isRegistered(Game game, GameBlock gameBlock) {
        return gameBlocks.containsKey(game) && gameBlocks.get(game).contains(gameBlock);
    }

    @Override
    public GameBlock getGameBlock(Game game, Material material) {
        if (gameBlocks.containsKey(game)) {
            for (GameBlock gameItem : gameBlocks.get(game)) {
                if (gameItem.getMaterial().equals(material)) {
                    return gameItem;
                }
            }
        }
        return null;
    }

    @Override
    public Set<GameBlock> getGameBlocks(Game game) {
        return gameBlocks.containsKey(game) ? gameBlocks.get(game) : new HashSet<GameBlock>();
    }

    @Override
    public UGameBlockManager registerGameBlock(Game game, GameBlock gameBlock) {
        if (gameBlocks.containsKey(game)) {
            Set<GameBlock> blocks = gameBlocks.get(game);
            for (GameBlock block : blocks) {
                if (gameBlock.equals(block)) {
                    return this;
                }
            }
            blocks.add(gameBlock);
        } else {
            Set<GameBlock> blocks = new HashSet<>();
            blocks.add(gameBlock);
            gameBlocks.put(game, blocks);
        }
        return this;
    }

    @Override
    public UGameBlockManager unregisterGameBlock(Game game, GameBlock gameBlock) {
        if (gameBlocks.containsKey(game)) {
            gameBlocks.get(game).remove(gameBlock);
        }
        return this;
    }

    @Override
    public UGameBlockManager unregisterGameBlocks(Game game) {
        gameBlocks.remove(game);
        return this;
    }
}
