/*
 * This file is part of UltimateGames ENGINE.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames ENGINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames ENGINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames ENGINE.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.whitelist;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class UBlockBreakWhitelist implements BlockBreakWhitelist {
    private final UltimateGames ultimateGames;
    private final Map<Game, Set<Material>> blocks = new HashMap<>();
    private final Map<Game, Boolean> useAsBlacklist = new HashMap<>();

    /**
     * Creates a new BlockBreakWhitelist.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     */
    public UBlockBreakWhitelist(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void reload() {
        blocks.clear();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game);
            if (gameConfig.contains("BlockBreakWhitelist")) {
                List<String> materialNames = gameConfig.getStringList("BlockBreakWhitelist");
                Set<Material> materials = new HashSet<>();
                for (String materialName : materialNames) {
                    materials.add(Material.valueOf(materialName));
                }
                blocks.put(game, materials);
                useAsBlacklist.put(game, gameConfig.getBoolean("DefaultSettings.Use-Whitelist-As-Blacklist", false));
            }
        }
    }

    @Override
    public boolean canBreakMaterial(Game game, Material material) {
        return blocks.containsKey(game) && (useAsBlacklist.get(game) ^ blocks.get(game).contains(material));
    }

    @Override
    public boolean canBreakMaterial(Game game, Block block) {
        return blocks.containsKey(game) && (useAsBlacklist.get(game) ^ blocks.get(game).contains(block.getType()));
    }

    @Override
    public Set<Material> materialsWhitelisted(Game game, Set<Material> materials) {
        Set<Material> whitelistedMaterials = new HashSet<>();
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

    @Override
    public Set<Block> blocksWhitelisted(Game game, Set<Block> materials) {
        Set<Block> whitelistedBlocks = new HashSet<>();
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
