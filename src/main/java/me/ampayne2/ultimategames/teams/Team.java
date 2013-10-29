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
package me.ampayne2.ultimategames.teams;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.scoreboards.ArenaScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a team in an arena.
 */
public class Team {
    private final UltimateGames ultimateGames;
    private final String name;
    private final ChatColor color;
    private final Arena arena;
    private boolean friendlyFire;
    private List<String> players = new ArrayList<String>();
    private static final int MAX_NAME_LENGTH = 14;

    /**
     * Creates a new team.
     *
     * @param ultimateGames A reference to the ultimateGames instance.
     * @param name          The name of the team.
     * @param arena         The arena of the team.
     * @param color         The color of the team.
     * @param friendlyFire  If the team should have friendly fire enabled.
     */
    public Team(UltimateGames ultimateGames, String name, Arena arena, ChatColor color, boolean friendlyFire) {
        this.ultimateGames = ultimateGames;
        // Keeps the name from being longer than 16 (14 + color) and potentially crashing players in the arena
        this.name = name.substring(0, Math.min(name.length(), MAX_NAME_LENGTH));
        this.color = color;
        this.arena = arena;
        this.friendlyFire = friendlyFire;
    }

    /**
     * Gets the team's name.
     *
     * @return The team's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the team's arena.
     *
     * @return The team's arena.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Gets the team's color.
     *
     * @return The team's color.
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * Checks if the team has friendly fire enabled.
     *
     * @return True if the team has friendly fire enabled, else false.
     */
    public boolean hasFriendlyFire() {
        return friendlyFire;
    }

    /**
     * Turns the team's friendly fire on or off.
     *
     * @param enabled True for friendly fire, False for no friendly fire.
     */
    public void setFriendlyFire(boolean enabled) {
        friendlyFire = enabled;
    }

    /**
     * Checks if a team has a player.
     *
     * @param playerName The player's name.
     * @return True if the player is in the team, else false.
     */
    public boolean hasPlayer(String playerName) {
        return players.contains(playerName);
    }

    /**
     * Checks if a team has space for another player while staying even with other teams.
     *
     * @return True if the team has space, else false.
     */
    public boolean hasSpace() {
        return Math.floor(arena.getPlayers().size() / ultimateGames.getTeamManager().getTeamsOfArena(arena).size()) >= players.size();
    }

    /**
     * Gets the players in a team.
     *
     * @return The players in a team.
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Adds a player to the team.
     *
     * @param player The player to add to the team.
     * @return True if the player is added to the team, else false.
     */
    public boolean addPlayer(Player player) {
        String playerName = player.getName();
        if (!players.contains(playerName)) {
            players.add(playerName);
            setPlayerColorToTeamColor(player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a player from the team.
     *
     * @param playerName Name of the player to remove from the team.
     */
    public void removePlayer(String playerName) {
        if (players.contains(playerName)) {
            ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.resetPlayerColor(playerName);
            }
            players.remove(playerName);
        }
    }

    /**
     * Removes all players from the team.
     */
    public void removePlayers() {
        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
        if (scoreBoard != null) {
            for (String playerName : players) {
                scoreBoard.resetPlayerColor(playerName);
            }
        }
        players.clear();
    }

    /**
     * Sets a player's color to the team's color.
     *
     * @param player The player.
     */
    public void setPlayerColorToTeamColor(Player player) {
        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
        if (scoreBoard != null) {
            scoreBoard.setPlayerColor(player, color);
        }
    }
}
