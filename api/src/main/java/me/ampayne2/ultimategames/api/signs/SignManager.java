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
package me.ampayne2.ultimategames.api.signs;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.games.Game;
import org.bukkit.Location;

import java.util.List;

/**
 * Manages UltimateGames signs.
 */
public interface SignManager {

    /**
     * Checks to see if a sign is an Ultimate Game sign.<br>
     * Faster than isUGSign(Sign sign);
     *
     * @param sign The sign to check.
     * @return If the sign is an Ultimate Game sign.
     */
    boolean isSign(org.bukkit.block.Sign sign, SignType signType);

    /**
     * Checks to see if a sign is an Ultimate Game sign.
     *
     * @param sign The sign to check.
     * @return If the sign is an Ultimate Game sign.
     */
    boolean isSign(org.bukkit.block.Sign sign);

    /**
     * Gets the redstone output sign at a location.
     *
     * @param location The location to check.
     * @return The redstone output sign. Null if doesn't exist at location.
     */
    Sign getRedstoneOutputSign(Location location);

    /**
     * Gets the UGsign of a sign.<br>
     * Faster than getUGSign(Sign sign);
     *
     * @param sign     The sign.
     * @param signType The sign's type.
     * @return The UG sign.
     */
    Sign getSign(org.bukkit.block.Sign sign, SignType signType);

    /**
     * Gets the UGsign of a sign.
     *
     * @param sign The sign.
     * @return The UG sign.
     */
    Sign getSign(org.bukkit.block.Sign sign);

    /**
     * Gets the Ultimate Game signs of an arena.<br>
     * Faster than getUGSignsOfArena(Arena arena);
     *
     * @param arena    The arena.
     * @param signType The SignType of the signs to get.
     * @return The Ultimate Game Signs.
     */
    List<Sign> getSignsOfArena(Arena arena, SignType signType);

    /**
     * Gets the Ultimate Game signs of an arena.<br>
     * Faster than getUGSignsOfArena(Arena arena);
     *
     * @param arena The arena.
     * @return The Ultimate Game Signs.
     */
    List<Sign> getSignsOfArena(Arena arena);

    /**
     * Gets the Ultimate Game signs of a game.<br>
     * Faster than getUGSignsOfGame(Game game);
     *
     * @param game     The game.
     * @param signType The SignType of the signs to get.
     * @return The Ultimate Game Signs.
     */
    List<Sign> getSignsOfGame(Game game, SignType signType);

    /**
     * Gets the Ultimate Game signs of a game.
     *
     * @param game The game.
     * @return The Ultimate Game Signs.
     */
    List<Sign> getSignsOfGame(Game game);

    /**
     * Updates the Ultimate Game signs of an arena.<br>
     * Faster than updateUGSignsOfArena(Arena arena);
     *
     * @param arena    The arena.
     * @param signType The SignType of the signs to update.
     */
    void updateSignsOfArena(Arena arena, SignType signType);

    /**
     * Updates the Ultimate Game signs of an arena.
     *
     * @param arena The arena.
     */
    void updateSignsOfArena(Arena arena);

    /**
     * Updates the Ultimate Game signs of a game.<br>
     * Faster than updateUGSignsOfGame(Game game);
     *
     * @param game     The game.
     * @param signType The SignType of the signs to update.
     */
    void updateSignsOfGame(Game game, SignType signType);

    /**
     * Updates the Ultimate Game signs of a game.
     *
     * @param game The game.
     */
    void updateSignsOfGame(Game game);
}
