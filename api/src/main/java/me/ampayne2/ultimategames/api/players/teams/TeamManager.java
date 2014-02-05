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
package me.ampayne2.ultimategames.api.players.teams;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.utils.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Manages teams in arenas.
 */
public interface TeamManager {

    /**
     * Checks if a team of an arena with a certain name exists.
     *
     * @param arena The arena.
     * @param name  The name of the team.
     * @return True if the team exists, else null.
     */
    boolean teamExists(Arena arena, String name);

    /**
     * Gets a team of an arena with a certain name.
     *
     * @param arena The arena.
     * @param name  The name of the team.
     * @return The team if it exists, else null.
     */
    Team getTeam(Arena arena, String name);

    /**
     * Gets the teams of an arena.
     *
     * @param arena The arena.
     * @return The teams of the arena.
     */
    List<Team> getTeamsOfArena(Arena arena);

    /**
     * Removes all the teams of an arena.
     *
     * @param arena The arena.
     */
    void removeTeamsOfArena(Arena arena);

    /**
     * Creates a team and adds it to the manager.
     *
     * @param name                     The name of the team.
     * @param arena                    The arena.
     * @param color                    The team color.
     * @param friendlyFire             If the team should have friendly fire.
     * @param canSeeFriendlyInvisibles If the team can see friendly invisibles.
     * @return The team.
     */
    Team createTeam(UltimateGames ultimateGames, String name, Arena arena, ChatColor color, boolean friendlyFire, boolean canSeeFriendlyInvisibles);

    /**
     * Removes a team from the manager.
     *
     * @param team The team to remove.
     */
    void removeTeam(Team team);

    /**
     * Checks if a player is in an arena team.
     *
     * @param playerName The player to check.
     * @return True if the player is in a team, else false.
     */
    boolean isPlayerInTeam(String playerName);

    /**
     * Gets a player's team.
     *
     * @param playerName The player's name.
     * @return The player's team. Null if the player isn't in a team.
     */
    Team getPlayerTeam(String playerName);

    /**
     * Sets a player's team. Removes them from their current team if they are in one.<br>
     * Only sets the player's team if its arena is the same as the one the player is currently in.
     *
     * @param player The player.
     * @param team   The team to add the player to.
     * @return True if the player is in an arena and the player's arena is the same as the team's arena, else false.
     */
    boolean setPlayerTeam(Player player, Team team);

    /**
     * Removes a player from their team if they are in one.
     *
     * @param player The player to remove from their team.
     */
    void removePlayerFromTeam(Player player);

    /**
     * Evenly sorts players in an arena into the arena's teams.<br>
     * Kicks players from the arena to stop uneven teams.<br>
     * Players who haven't joined a team yet are kicked before players who have.
     * TODO: Make the player's unlocked classes/tiers influence the sorting.
     *
     * @param arena The arena to sort the players of.
     */
    void sortPlayersIntoTeams(Arena arena);

    /**
     * Gets the team selector of an arena.
     *
     * @param arena The arena.
     * @return The arena's team selector.
     */
    IconMenu getTeamSelector(Arena arena);
}
