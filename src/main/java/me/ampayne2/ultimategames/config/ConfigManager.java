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
package me.ampayne2.ultimategames.config;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles custom configs.
 */
public class ConfigManager {
    private final UltimateGames ultimateGames;
    private final Map<ConfigType, ConfigAccessor> configs = new HashMap<ConfigType, ConfigAccessor>();
    private final Map<Game, GameConfigAccessor> gameConfigs = new HashMap<Game, GameConfigAccessor>();

    /**
     * Creates a new ConfigManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     */
    public ConfigManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        File dataFolder = ultimateGames.getDataFolder();

        ultimateGames.saveDefaultConfig();
        for (ConfigType configType : ConfigType.class.getEnumConstants()) {
            addConfigAccessor(new ConfigAccessor(ultimateGames, configType, dataFolder).saveDefaultConfig());
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

    /**
     * Gets a certain ConfigAccessor.
     *
     * @param configType The type of the config.
     * @return The config accessor.
     */
    public ConfigAccessor getConfigAccessor(ConfigType configType) {
        return configs.get(configType);
    }

    /**
     * Gets a certain FileConfiguration.
     *
     * @param configType The type of the config.
     * @return The config.
     */
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
            GameConfigAccessor config = new GameConfigAccessor(ultimateGames, game.getName());
            config.saveConfig();
            gameConfigs.put(game, config);
        }
    }

    /**
     * Gets a Game's Config.
     *
     * @return The Game's Config.
     */
    public GameConfigAccessor getGameConfigAccessor(Game game) {
        if (!gameConfigs.containsKey(game)) {
            addGameConfig(game);
        }
        return gameConfigs.get(game);
    }

    /**
     * Gets a game's FileConfiguration
     *
     * @param game The game.
     * @return The config.
     */
    public FileConfiguration getGameConfig(Game game) {
        return getGameConfigAccessor(game).getConfig();
    }
}
