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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;


/**
 * Used to access a YamlConfiguration file.
 */
public class ConfigAccessor {
    private final UltimateGames ultimateGames;
    private final ConfigType configType;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    /**
     * Creates a new ConfigAccessor.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param configType    The ConfigType of the configuration file.
     * @param parent        The parent file.
     */
    public ConfigAccessor(UltimateGames ultimateGames, ConfigType configType, File parent) {
        this.ultimateGames = ultimateGames;
        this.configType = configType;
        this.configFile = new File(parent, configType.getFileName());
    }

    /**
     * Creates a new ConfigAccessor.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param fileName      The file name of the configuration file.
     * @param parent        The parent file.
     */
    public ConfigAccessor(UltimateGames ultimateGames, String fileName, File parent) {
        this.ultimateGames = ultimateGames;
        this.configType = null;
        this.configFile = new File(parent, fileName);
    }

    /**
     * Reloads the configuration file from disk.
     */
    public ConfigAccessor reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = ultimateGames.getResource(configFile.getName());
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
        return this;
    }

    /**
     * Gets the config.
     *
     * @return The FileConfiguration.
     */
    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            reloadConfig();
        }
        return fileConfiguration;
    }

    /**
     * Saves the config to disk.
     */
    public ConfigAccessor saveConfig() {
        if (fileConfiguration != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException e) {
                ultimateGames.getMessenger().log(Level.SEVERE, "Could not save config to " + configFile);
                ultimateGames.getMessenger().debug(e);
            }
        }
        return this;
    }

    /**
     * Generates the default config if it hasn't already been generated.
     */
    public ConfigAccessor saveDefaultConfig() {
        if (!configFile.exists()) {
            ultimateGames.saveResource(configType.getFileName(), false);
        }
        return this;
    }

    /**
     * Gets the ConfigType.
     *
     * @return The ConfigAccessor's ConfigType.
     */
    public ConfigType getConfigType() {
        return configType;
    }
}