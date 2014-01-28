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
package me.ampayne2.ultimategames.whitelist;

import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Set;

/**
 * The block break whitelist.
 */
public interface BlockBreakWhitelist extends Whitelist {

    /**
     * Checks if a certain material can be broken in a certain game.
     *
     * @param game     The game.
     * @param material The material.
     * @return True if the game has a whitelist and the material is whitelisted, else false.
     */
    boolean canBreakMaterial(Game game, Material material);

    /**
     * Checks if a certain block can be broken in a certain game.
     *
     * @param game  The game.
     * @param block The block.
     * @return True if the game has a whitelist and the block's material is whitelisted, else false.
     */
    boolean canBreakMaterial(Game game, Block block);

    /**
     * Gets the materials that can be broken in a certain game out of a list of materials.
     *
     * @param game      The game.
     * @param materials The materials.
     * @return The materials that can be broken.
     */
    Set<Material> materialsWhitelisted(Game game, Set<Material> materials);

    /**
     * Gets the blocks that can be broken in a certain game out of a list of blocks.
     *
     * @param game      The game.
     * @param materials The materials.
     * @return The blocks that can be broken.
     */
    Set<Block> blocksWhitelisted(Game game, Set<Block> materials);
}
