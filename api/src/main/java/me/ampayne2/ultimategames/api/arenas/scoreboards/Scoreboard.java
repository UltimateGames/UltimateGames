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
package me.ampayne2.ultimategames.api.arenas.scoreboards;

import me.ampayne2.ultimategames.api.players.teams.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * A wrapper for bukkit scoreboards.
 */
public interface Scoreboard {

    /**
     * Gets the name of the ArenaScoreboard.
     *
     * @return The name of the ArenaScoreboard.
     */
    String getName();

    /**
     * Gets the bukkit scoreboard of the ArenaScoreboard.
     *
     * @return The bukkit scoreboard.
     */
    org.bukkit.scoreboard.Scoreboard getScoreboard();

    /**
     * Checks if the ArenaScoreboard has a player.
     *
     * @param player The player.
     * @return True if the ArenaScoreboard has the player, else false.
     */
    boolean hasPlayer(Player player);

    /**
     * Adds a player to the ArenaScoreboard.
     *
     * @param player The player.
     */
    void addPlayer(Player player);

    /**
     * Adds a player to the ArenaScoreboard and to a Team's scoreboard team.<br>
     * Also sets the player's name to the Team's color.
     *
     * @param player The player.
     * @param team   The team.
     */
    void addPlayer(Player player, Team team);

    /**
     * Removes a player from the ArenaScoreboard.
     *
     * @param player The player.
     */
    void removePlayer(Player player);

    /**
     * Sets a player's name to a certain color.<br>
     * Doesn't work for players in a UG team's scoreboard team, only for players with their own scoreboard team.
     *
     * @param player    The player.
     * @param chatColor The color.
     */
    void setPlayerColor(Player player, ChatColor chatColor);

    /**
     * Resets a player's name's color by unregistering the player's scoreboard team.<br>
     * Doesn't work for players in a UG team's scoreboard team, only for players with their own scoreboard team.
     *
     * @param player The player.
     */
    void resetPlayerColor(Player player);

    /**
     * Resets an ArenaScoreboard.
     */
    void reset();

    /**
     * Sets the visibility of an ArenaScoreboard.
     *
     * @param visible Whether or not the ArenaScoreboard should be visible.
     */
    void setVisible(Boolean visible);

    /**
     * Gets the score of a scoreboard element.
     *
     * @param name Name of the element.
     * @return The score.
     */
    int getScore(String name);

    /**
     * Sets the score of a scoreboard element.<br>
     * If setting the score to 0 and the score is already 0, the score is briefly changed to 1 so the 0 will appear.
     *
     * @param name Name of the element.
     */
    void setScore(String name, int score);

    /**
     * Resets the score of a scoreboard element.
     *
     * @param name Name of the element.
     */
    void resetScore(String name);

    /**
     * Gets the score of a team.
     *
     * @param team The team.
     * @return The team's score.
     */
    int getScore(Team team);

    /**
     * Sets the score of a team.
     *
     * @param team  The scoreboard.
     * @param score The new score.
     */
    void setScore(Team team, int score);

    /**
     * Resets the score of a team.
     *
     * @param team The team.
     */
    void resetScore(Team team);
}
