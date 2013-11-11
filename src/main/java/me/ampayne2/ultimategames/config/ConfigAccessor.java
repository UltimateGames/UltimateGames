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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ConfigAccessor {
    private final String fileName;
    private final JavaPlugin plugin;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    public ConfigAccessor(JavaPlugin plugin, String fileName, File parent) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.configFile = new File(parent, fileName);
    }

    public void reloadConfig() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            fileConfiguration.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (fileConfiguration == null) {
            this.reloadConfig();
        }
        return fileConfiguration;
    }

    public void saveConfig() {
        if (fileConfiguration != null && configFile != null) {
            try {
                getConfig().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }

    public static String saveInventory(Inventory inventory) {
        YamlConfiguration config = new YamlConfiguration();

        // Save every element in the list
        saveInventory(inventory, config);
        return config.saveToString();
    }

    public static void saveInventory(Inventory inventory, ConfigurationSection destination) {
        // Save every element in the list
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            // Don't store NULL entries
            if (item != null) {
                destination.set(Integer.toString(i), item);
            }
        }
    }

    public static ItemStack[] loadInventory(String data) throws InvalidConfigurationException {
        YamlConfiguration config = new YamlConfiguration();

        // Load the string
        config.loadFromString(data);
        return loadInventory(config);
    }

    public static ItemStack[] loadInventory(ConfigurationSection source) throws InvalidConfigurationException {
        List<ItemStack> stacks = new ArrayList<ItemStack>();

        try {
            // Try to parse this inventory
            for (String key : source.getKeys(false)) {
                int number = Integer.parseInt(key);

                // Size should always be bigger
                while (stacks.size() <= number) {
                    stacks.add(null);
                }

                stacks.set(number, (ItemStack) source.get(key));
            }
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Expected a number.", e);
        }

        // Return result
        return stacks.toArray(new ItemStack[0]);
    }
}