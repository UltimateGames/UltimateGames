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

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Used to access a YamlConfiguration file.
 */
public interface ConfigAccessor {

    /**
     * Reloads the configuration file from disk.
     */
    ConfigAccessor reloadConfig();

    /**
     * Gets the config.
     *
     * @return The FileConfiguration.
     */
    FileConfiguration getConfig();

    /**
     * Saves the config to disk.
     */
    ConfigAccessor saveConfig();

    /**
     * Gets the ConfigType.
     *
     * @return The ConfigAccessor's ConfigType.
     */
    ConfigType getConfigType();
}
