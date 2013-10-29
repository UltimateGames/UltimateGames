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

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class BlockPlaceWhitelist implements Whitelist {
    private final UltimateGames ultimateGames;
    private final Map<Game, Set<Material>> blocks = new HashMap<Game, Set<Material>>();
    private final Map<Game, Boolean> useAsBlacklist = new HashMap<Game, Boolean>();

    /**
     * The Block Place Whitelist.
     *
     * @param ultimateGames The UltimateGames instance.
     */
    public BlockPlaceWhitelist(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    public void reload() {
        blocks.clear();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
            if (gameConfig.contains("BlockPlaceWhitelist")) {
                List<String> materialNames = gameConfig.getStringList("BlockPlaceWhitelist");
                Set<Material> materials = new HashSet<Material>();
                for (String materialName : materialNames) {
                    materials.add(Material.valueOf(materialName));
                }
                blocks.put(game, materials);
                useAsBlacklist.put(game, gameConfig.getBoolean("DefaultSettings.Use-Whitelist-As-Blacklist", false));
            }
        }
    }

    /**
     * Checks if a certain material can be placed in a certain game.
     *
     * @param game     The game.
     * @param material The material.
     * @return True if the game has a whitelist and the material is whitelisted, else false.
     */
    public boolean canPlaceMaterial(Game game, Material material) {
        return blocks.containsKey(game) && (useAsBlacklist.get(game) ^ blocks.get(game).contains(material));
    }
}
