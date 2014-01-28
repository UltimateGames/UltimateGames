/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.players.points;

import me.ampayne2.ultimategames.games.Game;

/**
 * The ultimate games point manager unless replaced.
 */
public class NullPointManager implements PointManager {

    @Override
    public void addPoint(Game game, String playerName, String valueName, int amount) {
    }

    @Override
    public boolean hasPerk(Game game, String playerName, String valueName) {
        return false;
    }

    @Override
    public boolean hasPerk(Game game, String playerName, String valueName, boolean defaultValue) {
        return defaultValue;
    }
}
