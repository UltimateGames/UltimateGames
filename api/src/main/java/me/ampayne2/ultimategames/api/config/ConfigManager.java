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
package me.ampayne2.ultimategames.api.config;

import me.ampayne2.ultimategames.api.games.Game;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages the UltimateGames configs.
 */
public interface ConfigManager {

    /**
     * Gets a certain ConfigAccessor.
     *
     * @param configType The type of the config.
     * @return The ConfigAccessor.
     */
    ConfigAccessor getConfigAccessor(ConfigType configType);

    /**
     * Gets a certain FileConfiguration.
     *
     * @param configType The type of the config.
     * @return The FileConfiguration.
     */
    FileConfiguration getConfig(ConfigType configType);

    /**
     * Gets a Game's ConfigAccessor.
     *
     * @return The Game's ConfigAccessor.
     */
    ConfigAccessor getGameConfigAccessor(Game game);

    /**
     * Gets a game's FileConfiguration
     *
     * @param game The game.
     * @return The FileConfiguration.
     */
    FileConfiguration getGameConfig(Game game);
}
