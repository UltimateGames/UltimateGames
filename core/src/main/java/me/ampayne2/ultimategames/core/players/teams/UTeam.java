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
package me.ampayne2.ultimategames.core.players.teams;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.scoreboards.Scoreboard;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a team in an arena.<br>
 * Must be registered with the {@link me.ampayne2.ultimategames.api.players.teams.TeamManager}.
 */
public class UTeam implements Team {
    private final UG ultimateGames;
    private final String name;
    private final ChatColor color;
    private final Arena arena;
    private boolean friendlyFire;
    private boolean canSeeFriendlyInvisibles;
    @SuppressWarnings("deprecation")
    private ItemStack icon = new ItemStack(397, 1, (short) 3);
    private List<String> players = new ArrayList<>();
    private static final int MAX_NAME_LENGTH = 14;

    /**
     * Creates a new team.
     *
     * @param ultimateGames            The {@link me.ampayne2.ultimategames.core.UG} instance.
     * @param name                     The name of the team.
     * @param arena                    The arena of the team.
     * @param color                    The color of the team.
     * @param friendlyFire             If the team should have friendly fire enabled.
     * @param canSeeFriendlyInvisibles If the team can see friendly invisibles.
     */
    public UTeam(UG ultimateGames, String name, Arena arena, ChatColor color, boolean friendlyFire, boolean canSeeFriendlyInvisibles) {
        this.ultimateGames = ultimateGames;
        // Keeps the name from being longer than 16 (14 + color) and potentially crashing players in the arena
        this.name = name.substring(0, Math.min(name.length(), MAX_NAME_LENGTH));
        this.color = color;
        this.arena = arena;
        this.friendlyFire = friendlyFire;
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    @Override
    public ChatColor getColor() {
        return color;
    }

    @Override
    public String getColoredName() {
        return color + name;
    }

    @Override
    public boolean hasFriendlyFire() {
        return friendlyFire;
    }

    @Override
    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        return canSeeFriendlyInvisibles;
    }

    @Override
    public void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles) {
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
    }

    @Override
    public boolean hasPlayer(String playerName) {
        return players.contains(playerName);
    }

    @Override
    public boolean hasSpace() {
        return Math.floor(arena.getPlayers().size() / ultimateGames.getTeamManager().getTeamsOfArena(arena).size()) > players.size();
    }

    @Override
    public List<String> getPlayers() {
        return new ArrayList<>(players);
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
            Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.addPlayer(player, this);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removePlayer(Player player) {
        String playerName = player.getName();
        if (players.contains(playerName)) {
            Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.resetPlayerColor(player);
            }
            players.remove(playerName);
        }
    }

    @Override
    public void removePlayers() {
        Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
        if (scoreBoard != null) {
            for (String playerName : players) {
                scoreBoard.resetPlayerColor(Bukkit.getPlayerExact(playerName));
            }
        }
        players.clear();
    }

    @Override
    public ItemStack getTeamIcon() {
        return icon;
    }

    @Override
    public void setTeamIcon(ItemStack icon) {
        ItemStack newIcon = icon.clone();
        newIcon.setAmount(1);
        this.icon = newIcon;
    }
}
