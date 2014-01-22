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
package me.ampayne2.ultimategames.arenas.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collection;

/**
 * A wrapper for the bukkit scoreboard.<br>
 * Create using the {@link me.ampayne2.ultimategames.arenas.scoreboards.ScoreboardManager}.
 */
public class ArenaScoreboard {
    private final Scoreboard scoreboard;
    private final String name;
    private static final String GHOST_TEAM_NAME = "ultimateghosts";

    /**
     * Creates an ArenaScoreboard.
     *
     * @param name The name of the ArenaScoreboard.
     */
    public ArenaScoreboard(String name) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewObjective(name, "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
        this.name = name;
    }

    /**
     * Gets the name of an ArenaScoreboard.
     *
     * @return The name of the ArenaScoreboard.
     */
    public String getName() {
        return name;
    }

    /**
     * Lets a player see an ArenaScoreboard.
     *
     * @param player The player.
     */
    public void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

    /**
     * Lets multiple players see an ArenaScoreboard.
     *
     * @param players The players.
     */
    public void addPlayers(Collection<Player> players) {
        for (Player player : players) {
            player.setScoreboard(scoreboard);
        }
    }

    /**
     * Hides an ArenaScoreboard from a player.
     *
     * @param player The player.
     */
    public void removePlayer(Player player) {
        resetPlayerColor(player.getName());
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /**
     * Hides an ArenaScoreboard from multiple players.
     *
     * @param players The players.
     */
    public void removePlayers(Collection<Player> players) {
        for (Player player : players) {
            removePlayer(player);
        }
    }

    /**
     * Sets a player's name to a certain color.
     *
     * @param player    The player.
     * @param chatColor The color.
     */
    public void setPlayerColor(Player player, ChatColor chatColor) {
        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) {
            team.unregister();
        }
        team = scoreboard.registerNewTeam(player.getName());
        team.setPrefix(chatColor + "");
        team.addPlayer(player);
    }

    /**
     * Resets a player's name's color.
     *
     * @param playerName The player.
     */
    public void resetPlayerColor(String playerName) {
        Team team = scoreboard.getTeam(playerName);
        if (team != null) {
            team.unregister();
        }
    }

    /**
     * Adds a player to the ghost team. Players must still receive an invisibility effect to be seen as a ghost.<br>
     * Only players in the ghost team will see each other as ghosts.<br>
     * RESETS PLAYER COLOR.
     *
     * @param player The player to add to the ghost team.
     */
    public void makePlayerGhost(Player player) {
        resetPlayerColor(player.getName());
        Team team = scoreboard.getTeam(GHOST_TEAM_NAME);
        if (team == null ) {
            team = scoreboard.registerNewTeam(GHOST_TEAM_NAME);
            team.setCanSeeFriendlyInvisibles(true);
        }
        team.addPlayer(player);
    }

    /**
     * Resets an ArenaScoreboard.
     */
    public void reset() {
        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }
        scoreboard.getObjective(name).unregister();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    /**
     * Sets the visibility of an ArenaScoreboard.
     *
     * @param visible Whether or not the ArenaScoreboard should be visible.
     */
    public void setVisible(Boolean visible) {
        if (visible) {
            scoreboard.getObjective(name).setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    /**
     * Gets the score of a scoreboard element.
     *
     * @param name Name of the element.
     * @return The score.
     */
    public int getScore(String name) {
        return scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name)).getScore();
    }

    /**
     * Sets the score of a scoreboard element.<br>
     * If setting the score to 0 and the score is already 0, the score is briefly changed to 1 so the 0 will appear.
     *
     * @param name Name of the element.
     */
    public void setScore(String name, int score) {
        Score scoreboardScore = scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name));
        if (score == 0 && scoreboardScore.getScore() == 0) {
            scoreboardScore.setScore(1);
        }
        scoreboardScore.setScore(score);
    }

    /**
     * Resets the score of a scoreboard element.
     *
     * @param name Name of the element.
     */
    public void resetScore(String name) {
        scoreboard.resetScores(Bukkit.getOfflinePlayer(name));
    }

    /**
     * Gets the score of a team.
     *
     * @param team The team.
     * @return The team's score.
     */
    public int getScore(me.ampayne2.ultimategames.players.teams.Team team) {
        return getScore(team.getColor() + "Team " + team.getName());
    }

    /**
     * Sets the score of a team.
     *
     * @param team  The scoreboard.
     * @param score The new score.
     */
    public void setScore(me.ampayne2.ultimategames.players.teams.Team team, int score) {
        setScore(team.getColor() + "Team " + team.getName(), score);
    }

    /**
     * Resets the score of a team.
     *
     * @param team The team.
     */
    public void resetScore(me.ampayne2.ultimategames.players.teams.Team team) {
        resetScore(team.getColor() + "Team " + team.getName());
    }
}
