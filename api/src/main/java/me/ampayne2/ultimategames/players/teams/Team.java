/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.players.teams;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * A team in an arena.
 */
public interface Team {

    /**
     * Gets the team's name.
     *
     * @return The team's name.
     */
    String getName();

    /**
     * Gets the team's arena.
     *
     * @return The team's arena.
     */
    Arena getArena();

    /**
     * Gets the team's color.
     *
     * @return The team's color.
     */
    ChatColor getColor();

    /**
     * Checks if the members of the team can attack each other.
     *
     * @return True if the team has friendly fire enabled, else false.
     */
    boolean hasFriendlyFire();

    /**
     * Sets if the members of the team can attack each other.
     *
     * @param friendlyFire If the team has friendly fire enabled.
     */
    void setFriendlyFire(boolean friendlyFire);

    /**
     * Checks if the members of the team can see friendly invisibles.
     *
     * @return True if the team can see friendly invisibles, else false.
     */
    boolean canSeeFriendlyInvisibles();

    /**
     * Sets if the members of the team can see friendly invisibles.
     *
     * @param canSeeFriendlyInvisibles If the team can see friendly invisibles.
     */
    void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles);

    /**
     * Checks if a team has a player.
     *
     * @param playerName The player's name.
     * @return True if the player is in the team, else false.
     */
    boolean hasPlayer(String playerName);

    /**
     * Checks if a team has space for another player while staying even with other teams.
     *
     * @return True if the team has space, else false.
     */
    boolean hasSpace();

    /**
     * Gets the players in a team.
     *
     * @return The players in a team.
     */
    List<String> getPlayers();

    /**
     * Removes a player from the team.
     *
     * @param player The player to remove from the team.
     */
    void removePlayer(Player player);

    /**
     * Removes all players from the team.
     */
    void removePlayers();
}
