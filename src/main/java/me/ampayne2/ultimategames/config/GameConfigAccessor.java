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

import java.io.File;

/**
 * A ConfigAccessor used to access a game config.
 */
public class GameConfigAccessor extends ConfigAccessor {
    private static final String EXTENSION = ".yml";
    private static final String GAME_FOLDER = "/Games";

    /**
     * Creates a new GameConfigAccessor.
     *
     * @param ultimateGames The UltimateGames instance.
     * @param gameName      The name of the game.
     */
    public GameConfigAccessor(UltimateGames ultimateGames, String gameName) {
        super(ultimateGames, gameName + EXTENSION, new File(ultimateGames.getDataFolder() + GAME_FOLDER));
    }

    @Override
    public ConfigAccessor saveDefaultConfig() {
        return this;
    }
}
