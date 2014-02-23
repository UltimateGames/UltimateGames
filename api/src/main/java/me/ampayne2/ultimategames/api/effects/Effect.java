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
package me.ampayne2.ultimategames.api.effects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents any type of effect in UG.
 */
public interface Effect {

    /**
     * Plays the effect at a location.
     *
     * @param location The location.
     * @return The Effect.
     */
    Effect play(Location location);

    /**
     * Plays the effect to a player at a location.
     *
     * @param player   The player.
     * @param location The location.
     * @return The Effect.
     */
    Effect play(Player player, Location location);
}
