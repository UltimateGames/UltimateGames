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
package me.ampayne2.ultimategames.api.games.blocks;

import me.ampayne2.ultimategames.api.games.Game;
import org.bukkit.Material;

import java.util.Set;

/**
 * Manages GameBlocks.
 */
public interface GameBlockManager {

    /**
     * Checks if an Material is registered.
     *
     * @param game     The game.
     * @param material The Material.
     * @return True if the Material is registered, else false.
     */
    boolean isRegistered(Game game, Material material);

    /**
     * Checks if a GameBlock is registered.
     *
     * @param game      The game.
     * @param gameBlock The GameBlock.
     * @return True if the GameBlock is registered, else false.
     */
    boolean isRegistered(Game game, GameBlock gameBlock);

    /**
     * Gets a GameBlock of a game.
     *
     * @param game     The game.
     * @param material The Material of the GameBlock.
     * @return The GameBlock, null if none of the Material exists.
     */
    GameBlock getGameBlock(Game game, Material material);

    /**
     * Gets the GameBlocks of a game.
     *
     * @param game The game.
     * @return The GameBlocks of a game.
     */
    Set<GameBlock> getGameBlocks(Game game);

    /**
     * Registers a GameBlock.
     *
     * @param game      The Game.
     * @param gameBlock The GameBlock.
     * @return True if the GameBlock was registered successfully, else false.
     */
    GameBlockManager registerGameBlock(Game game, GameBlock gameBlock);

    /**
     * Unregisters a GameBlock.
     *
     * @param game      The Game.
     * @param gameBlock The GameBlock.
     */
    GameBlockManager unregisterGameBlock(Game game, GameBlock gameBlock);

    /**
     * Unregisters all of a game's GameBlocks.
     *
     * @param game The Game.
     */
    GameBlockManager unregisterGameBlocks(Game game);
}
