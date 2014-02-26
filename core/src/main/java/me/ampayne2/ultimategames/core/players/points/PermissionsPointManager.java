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
package me.ampayne2.ultimategames.core.players.points;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.players.points.PointManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * The default Ultimate Games point manager.<br>
 * Uses permissions to check if a player has a perk and does not store points added.
 */
public class PermissionsPointManager implements PointManager {

    @Override
    public void addPoint(Game game, String playerName, String valueName, int amount) {
    }

    @Override
    public boolean hasPerk(Game game, String playerName, String valueName) {
        Player player = Bukkit.getPlayerExact(playerName);
        return player != null && player.hasPermission("UltimateGames." + game.getName() + "." + valueName);
    }

    @Override
    public boolean hasPerk(Game game, String playerName, String valueName, boolean defaultValue) {
        return hasPerk(game, playerName, valueName) || defaultValue;
    }
}
