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
package me.ampayne2.ultimategames.core.arenas.scoreboards;

import me.ampayne2.ultimategames.api.arenas.scoreboards.Scoreboard;
import me.ampayne2.ultimategames.api.players.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class UScoreboard implements Scoreboard {
    private final String name;
    private final org.bukkit.scoreboard.Scoreboard scoreboard;
    private static final String GHOST_TEAM_NAME = "ghosts";

    /**
     * Creates an ArenaScoreboard.
     *
     * @param name The name of the ArenaScoreboard.
     */
    public UScoreboard(String name) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewObjective(name, "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboard.registerNewTeam(GHOST_TEAM_NAME).setCanSeeFriendlyInvisibles(true);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public boolean hasPlayer(Player player) {
        return player.getScoreboard().equals(scoreboard);
    }

    @Override
    public void addPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

    @Override
    public void addPlayer(Player player, Team team) {
        player.setScoreboard(scoreboard);
        org.bukkit.scoreboard.Team scoreboardTeam = scoreboard.getTeam(team.getName());
        if (scoreboardTeam == null) {
            scoreboardTeam = scoreboard.registerNewTeam(team.getName());
            scoreboardTeam.setPrefix(team.getColor() + "");
            if (team.canSeeFriendlyInvisibles()) {
                scoreboardTeam.setCanSeeFriendlyInvisibles(true);
            }
        }
        scoreboardTeam.addPlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        if (hasPlayer(player)) {
            org.bukkit.scoreboard.Team team = scoreboard.getPlayerTeam(player);
            if (team != null) {
                team.removePlayer(player);
                if (team.getName().equals(player.getName())) {
                    team.unregister();
                }
            }
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    @Override
    public void setPlayerColor(Player player, ChatColor chatColor) {
        if (hasPlayer(player)) {
            String playerName = player.getName();
            org.bukkit.scoreboard.Team team = scoreboard.getPlayerTeam(player);
            if (team == null) {
                team = scoreboard.registerNewTeam(playerName);
            }
            if (team.getName().equals(playerName)) {
                team.setPrefix(chatColor + "");
                team.addPlayer(player);
            }
        }
    }

    @Override
    public void resetPlayerColor(Player player) {
        if (hasPlayer(player)) {
            org.bukkit.scoreboard.Team team = scoreboard.getPlayerTeam(player);
            if (team != null) {
                team.removePlayer(player);
            }
        }
    }

    /**
     * Adds a player to the ghost team. Players must still receive an invisibility effect to be seen as a ghost.<br>
     * Only players in the ghost team will see each other as ghosts.<br>
     * RESETS PLAYER COLOR. Only use for spectators
     *
     * @param player The player to add to the ghost team.
     */
    public void makePlayerGhost(Player player) {
        if (hasPlayer(player)) {
            scoreboard.getTeam(GHOST_TEAM_NAME).addPlayer(player);
        }
    }

    @Override
    public void reset() {
        for (org.bukkit.scoreboard.Team team : scoreboard.getTeams()) {
            team.unregister();
        }
        scoreboard.getObjective(name).unregister();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreboard.registerNewTeam(GHOST_TEAM_NAME).setCanSeeFriendlyInvisibles(true);
    }

    @Override
    public void setVisible(Boolean visible) {
        if (visible) {
            scoreboard.getObjective(name).setDisplaySlot(DisplaySlot.SIDEBAR);
        } else {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        }
    }

    @Override
    public int getScore(String name) {
        return scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name)).getScore();
    }

    @Override
    public void setScore(String name, int score) {
        Score scoreboardScore = scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name));
        if (score == 0 && scoreboardScore.getScore() == 0) {
            scoreboardScore.setScore(1);
        }
        scoreboardScore.setScore(score);
    }

    @Override
    public void resetScore(String name) {
        scoreboard.resetScores(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public int getScore(Team team) {
        return getScore(team.getColor() + "Team " + team.getName());
    }

    @Override
    public void setScore(Team team, int score) {
        setScore(team.getColor() + "Team " + team.getName(), score);
    }

    @Override
    public void resetScore(Team team) {
        resetScore(team.getColor() + "Team " + team.getName());
    }
}
