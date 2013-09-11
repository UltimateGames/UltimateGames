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
package me.ampayne2.ultimategames.whitelist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

public class BlockBreakWhitelist extends Whitelist {

    private UltimateGames ultimateGames;
    private Map<Game, Set<Material>> blocks;
    private Map<Game, Boolean> useAsBlacklist = new HashMap<Game, Boolean>();

    /**
     * The Block Break Whitelist.
     * @param ultimateGames The UltimateGames instance.
     */
    public BlockBreakWhitelist(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    public void reload() {
        blocks = new HashMap<Game, Set<Material>>();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
            if (gameConfig.contains("BlockBreakWhitelist")) {
                List<String> materialNames = gameConfig.getStringList("BlockBreakWhitelist");
                Set<Material> materials = new HashSet<Material>();
                for (String materialName : materialNames) {
                    materials.add(Material.valueOf(materialName));
                }
                blocks.put(game, materials);
                Boolean blacklist = gameConfig.getBoolean("DefaultSettings.Use-Whitelist-As-Blacklist", false);
                useAsBlacklist.put(game, blacklist);
            }
        }
    }

    /**
     * Checks if a certain material can be broken in a certain game.
     * @param game The game.
     * @param material The material.
     * @return True if the game has a whitelist and the material is whitelisted, else false.
     */
    public boolean canBreakMaterial(Game game, Material material) {
        return blocks.containsKey(game) && (useAsBlacklist.get(game) ^ blocks.get(game).contains(material));
    }

    /**
     * Checks if a certain block can be broken in a certain game.
     * @param game The game.
     * @param block The block.
     * @return True if the game has a whitelist and the block's material is whitelisted, else false.
     */
    public boolean canBreakMaterial(Game game, Block block) {
        return blocks.containsKey(game) && ((!useAsBlacklist.get(game) && blocks.get(game).contains(block.getType())) || (useAsBlacklist.get(game) && !blocks.get(game).contains(block.getType())));
    }

    /**
     * Gets the materials that can be broken in a certain game out of a list of materials.
     * @param game The game.
     * @param materials The materials.
     * @return The materials that can be broken.
     */
    public Set<Material> materialsWhitelisted(Game game, Set<Material> materials) {
        Set<Material> whitelistedMaterials = new HashSet<Material>();
        if (blocks.containsKey(game)) {
            Set<Material> whitelistedTypes = blocks.get(game);
            for (Material material : materials) {
                if ((!useAsBlacklist.get(game) && whitelistedTypes.contains(material)) || (useAsBlacklist.get(game) && !whitelistedTypes.contains(material))) {
                    whitelistedMaterials.add(material);
                }
            }
        }
        return whitelistedMaterials;
    }

    /**
     * Gets the blocks that can be broken in a certain game out of a list of blocks.
     * @param game The game.
     * @param materials The materials.
     * @return The blocks that can be broken.
     */
    public Set<Block> blocksWhitelisted(Game game, Set<Block> materials) {
        Set<Block> whitelistedBlocks = new HashSet<Block>();
        if (blocks.containsKey(game)) {
            Set<Material> whitelistedTypes = blocks.get(game);
            for (Block block : materials) {
                if ((!useAsBlacklist.get(game) && whitelistedTypes.contains(block.getType())) || (useAsBlacklist.get(game) && !whitelistedTypes.contains(block.getType()))) {
                    whitelistedBlocks.add(block);
                }
            }
        }
        return whitelistedBlocks;
    }

}
