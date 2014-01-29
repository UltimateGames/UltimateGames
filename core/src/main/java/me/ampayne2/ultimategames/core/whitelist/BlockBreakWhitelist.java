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
import me.ampayne2.ultimategames.api.whitelist.Whitelist;
import me.ampayne2.ultimategames.core.UG;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * A whitelist of blocks that can be broken in a game.
 */
public class BlockBreakWhitelist implements Whitelist<Material> {
    private final UG ultimateGames;
    private final Map<Game, Set<Material>> materials = new HashMap<>();
    private final Map<Game, Boolean> useAsBlacklist = new HashMap<>();

    /**
     * Creates a new BlockBreakWhitelist.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public BlockBreakWhitelist(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        reload();
    }

    @Override
    public boolean isWhitelisted(Game game, Material material) {
        Validate.notNull(game, "Game cannot be null");
        Validate.notNull(material, "Material cannot be null");

        return materials.containsKey(game) && (useAsBlacklist.get(game) ^ materials.get(game).contains(material));
    }

    @Override
    public boolean isBlacklisted(Game game, Material material) {
        return !isWhitelisted(game, material);
    }

    @Override
    public void reload() {
        materials.clear();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game);
            if (gameConfig.contains("BlockBreakWhitelist")) {
                List<String> materialNames = gameConfig.getStringList("BlockBreakWhitelist");
                Set<Material> configMaterials = new HashSet<>();
                for (String materialName : materialNames) {
                    configMaterials.add(Material.valueOf(materialName));
                }
                materials.put(game, configMaterials);
                useAsBlacklist.put(game, gameConfig.getBoolean("DefaultSettings.Use-Whitelist-As-Blacklist", false));
            }
        }
    }
}
