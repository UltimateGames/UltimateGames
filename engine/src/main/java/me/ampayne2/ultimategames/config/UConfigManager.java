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
package me.ampayne2.ultimategames.config;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles custom configs.
 */
public class UConfigManager implements ConfigManager {
    private final UG ultimateGames;
    private final Map<ConfigType, ConfigAccessor> configs = new HashMap<>();
    private final Map<Game, UGameConfigAccessor> gameConfigs = new HashMap<>();

    /**
     * Creates a new ConfigManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public UConfigManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        File dataFolder = ultimateGames.getDataFolder();

        ultimateGames.saveDefaultConfig();
        for (ConfigType configType : ConfigType.class.getEnumConstants()) {
            addConfigAccessor(new UConfigAccessor(ultimateGames, configType, dataFolder).saveDefaultConfig());
        }
    }

    /**
     * Adds a ConfigAccessor to the config manager.
     *
     * @param configAccessor The ConfigAccessor.
     */
    public void addConfigAccessor(ConfigAccessor configAccessor) {
        configs.put(configAccessor.getConfigType(), configAccessor);
    }

    @Override
    public ConfigAccessor getConfigAccessor(ConfigType configType) {
        return configs.get(configType);
    }

    @Override
    public FileConfiguration getConfig(ConfigType configType) {
        return configs.get(configType).getConfig();
    }

    /**
     * Adds a Game's Config to the manager.
     *
     * @param game The Game whose config you want to add.
     */
    public void addGameConfig(Game game) {
        if (!gameConfigs.containsKey(game)) {
            UGameConfigAccessor config = new UGameConfigAccessor(ultimateGames, game.getName());
            config.saveConfig();
            gameConfigs.put(game, config);
        }
    }

    @Override
    public UGameConfigAccessor getGameConfigAccessor(Game game) {
        if (!gameConfigs.containsKey(game)) {
            addGameConfig(game);
        }
        return gameConfigs.get(game);
    }

    @Override
    public FileConfiguration getGameConfig(Game game) {
        return getGameConfigAccessor(game).getConfig();
    }
}
