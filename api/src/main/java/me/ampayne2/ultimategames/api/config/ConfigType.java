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

import me.ampayne2.ultimategames.api.UltimateGames;

/**
 * An enum of ultimate games configs.
 */
public enum ConfigType {
    MESSAGE("Messages.yml"),
    ARENA("Arenas.yml"),
    LOBBY("Lobbies.yml"),
    SIGN("Signs.yml"),
    CHEST("Chests.yml");

    private final String fileName;

    private ConfigType(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the ConfigType's file name.
     *
     * @return The file name of the ConfigType.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the {@link me.ampayne2.ultimategames.api.config.ConfigAccessor} of the ConfigType.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @return The {@link me.ampayne2.ultimategames.api.config.ConfigAccessor}
     */
    public ConfigAccessor getConfigAccessor(UltimateGames ultimateGames) {
        return ultimateGames.getConfigManager().getConfigAccessor(this);
    }
}
