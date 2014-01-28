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
package me.ampayne2.ultimategames.core.whitelist;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.whitelist.BlockPlaceWhitelist;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class UBlockPlaceWhitelist implements BlockPlaceWhitelist {
    private final UG ultimateGames;
    private final Map<Game, Set<Material>> blocks = new HashMap<>();
    private final Map<Game, Boolean> useAsBlacklist = new HashMap<>();

    /**
     * Creates a new BlockPlaceWhitelist.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UBlockPlaceWhitelist(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void reload() {
        blocks.clear();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game);
            if (gameConfig.contains("BlockPlaceWhitelist")) {
                List<String> materialNames = gameConfig.getStringList("BlockPlaceWhitelist");
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
    public boolean canPlaceMaterial(Game game, Material material) {
        return blocks.containsKey(game) && (useAsBlacklist.get(game) ^ blocks.get(game).contains(material));
    }
}
