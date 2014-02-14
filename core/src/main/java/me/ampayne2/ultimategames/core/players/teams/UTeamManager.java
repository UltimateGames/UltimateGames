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

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.events.arenas.ArenaOpenEvent;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.api.players.teams.TeamManager;
import me.ampayne2.ultimategames.api.utils.IconMenu;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class UTeamManager implements TeamManager, Listener {
    private final UG ultimateGames;
    private Map<Arena, List<Team>> teams = new HashMap<>();
    private Map<Arena, IconMenu> teamSelectors = new HashMap<>();
    private static final Random RANDOM = new Random();

    /**
     * Creates a new TeamManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UTeamManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;

        Bukkit.getServer().getPluginManager().registerEvents(this, ultimateGames.getPlugin());
    }

    @Override
    public boolean teamExists(Arena arena, String name) {
        if (teams.containsKey(arena)) {
            for (Team team : teams.get(arena)) {
                if (name.equalsIgnoreCase(team.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Team getTeam(Arena arena, String name) {
        if (teams.containsKey(arena)) {
            for (Team team : teams.get(arena)) {
                if (name.equalsIgnoreCase(team.getName())) {
                    return team;
                }
            }
        }
        return null;
    }

    @Override
    public List<Team> getTeamsOfArena(Arena arena) {
        return teams.containsKey(arena) ? teams.get(arena) : new ArrayList<Team>();
    }

    @Override
    public void removeTeamsOfArena(Arena arena) {
        if (teams.containsKey(arena)) {
            List<Team> arenaTeams = teams.get(arena);
            for (Team team : arenaTeams) {
                team.removePlayers();
            }
            teams.remove(arena);
        }
    }

    @Override
    public Team createTeam(UltimateGames ultimateGames, String name, Arena arena, ChatColor color, boolean friendlyFire, boolean canSeeFriendlyInvisibles) {
        Team team = new UTeam((UG) ultimateGames, name, arena, color, friendlyFire, canSeeFriendlyInvisibles);
        List<Team> arenaTeams = teams.containsKey(arena) ? teams.get(arena) : new ArrayList<Team>();
        if (!arenaTeams.contains(team)) {
            arenaTeams.add(team);
            teams.put(arena, arenaTeams);
            return team;
        } else {
            return null;
        }
    }

    @Override
    public void removeTeam(Team team) {
        Arena arena = team.getArena();
        if (teams.containsKey(arena)) {
            List<Team> arenaTeams = teams.get(arena);
            if (arenaTeams.contains(team)) {
                team.removePlayers();
                arenaTeams.remove(team);
                if (arenaTeams.isEmpty()) {
                    teams.remove(arena);
                } else {
                    teams.put(arena, arenaTeams);
                }
            }
        }
    }

    @Override
    public boolean isPlayerInTeam(String playerName) {
        Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
        if (arena != null && teams.containsKey(arena)) {
            for (Team team : teams.get(arena)) {
                if (team.hasPlayer(playerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Team getPlayerTeam(String playerName) {
        Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
        Team playerTeam = null;
        if (arena != null && teams.containsKey(arena)) {
            for (Team team : teams.get(arena)) {
                if (team.hasPlayer(playerName)) {
                    playerTeam = team;
                }
            }
        }
        return playerTeam;
    }

    @Override
    public boolean setPlayerTeam(Player player, Team team) {
        String playerName = player.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName) && ultimateGames.getPlayerManager().getArenaPlayer(playerName).getArena().equals(team.getArena())) {
            Team oldTeam = getPlayerTeam(playerName);
            if (oldTeam != null) {
                oldTeam.removePlayer(player);
                ultimateGames.getMessenger().sendMessage(player, "teams.leave", oldTeam.getColor() + oldTeam.getName());
            }
            ((UTeam) team).addPlayer(player);
            ultimateGames.getMessenger().sendMessage(player, "teams.join", team.getColor() + team.getName());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removePlayerFromTeam(Player player) {
        Team team = getPlayerTeam(player.getName());
        if (team != null) {
            team.removePlayer(player);
        }
    }

    @Override
    public void sortPlayersIntoTeams(Arena arena) {
        List<Team> arenaTeams = getTeamsOfArena(arena);
        List<String> players = arena.getPlayers();
        List<String> playersInTeams = new ArrayList<>();
        List<String> playersNotInTeams = new ArrayList<>(players);

        // Populate the players in teams and players not in teams lists
        for (Team team : arenaTeams) {
            for (String playerName : team.getPlayers()) {
                playersInTeams.add(playerName);
                playersNotInTeams.remove(playerName);
            }
        }

        // Don't sort if there are no teams
        int teamAmount = arenaTeams.size();
        if (teamAmount <= 0 || players.size() < teamAmount) {
            return;
        }

        // Kick the last player(s) to join from the game if the teams cannot be balanced
        while (players.size() % teamAmount != 0) {
            Player playerToKick = Bukkit.getPlayerExact(playersNotInTeams.size() > 0 ? playersNotInTeams.get(playersNotInTeams.size() - 1) : playersInTeams.get(playersInTeams.size() - 1));
            ultimateGames.getPlayerManager().removePlayerFromArena(playerToKick, false);
            ultimateGames.getMessenger().sendMessage(playerToKick, "arenas.kick");
            playersInTeams.remove(playerToKick.getName());
            playersNotInTeams.remove(playerToKick.getName());
            players = arena.getPlayers();
        }

        // Kick the last player(s) to join from their team if the teams cannot be balanced
        for (Team team : arenaTeams) {
            List<String> teamPlayers = team.getPlayers();
            while ((players.size() / teamAmount) < teamPlayers.size()) {
                String playerName = teamPlayers.get(teamPlayers.size() - 1);
                Player player = Bukkit.getPlayerExact(playerName);
                team.removePlayer(player);
                ultimateGames.getMessenger().sendMessage(player, "teams.kick", team.getColor() + team.getName());
                playersNotInTeams.add(playerName);
                teamPlayers = team.getPlayers();
            }
        }

        // Add the players not in teams yet to the teams that need players.
        for (Team team : arenaTeams) {
            while ((players.size() / teamAmount) > team.getPlayers().size()) {
                Player player = Bukkit.getPlayerExact(playersNotInTeams.get(RANDOM.nextInt(playersNotInTeams.size())));
                ((UTeam) team).addPlayer(player);
                ultimateGames.getMessenger().sendMessage(player, "teams.join", team.getColor() + team.getName());
                playersNotInTeams.remove(player.getName());
            }
        }
    }

    @Override
    public IconMenu getTeamSelector(final Arena arena) {
        if (teamSelectors.containsKey(arena)) {
            return teamSelectors.get(arena);
        }

        final List<Team> teams = getTeamsOfArena(arena);
        IconMenu menu = new IconMenu(arena.getGame().getName() + " Teams", ((int) Math.ceil(teams.size() / 9.0)) * 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                TeamManager teamManager = ultimateGames.getTeamManager();
                Team team = teamManager.getTeam(arena, event.getName());
                if (team.hasSpace()) {
                    teamManager.setPlayerTeam(event.getPlayer(), team);
                } else {
                    ultimateGames.getMessenger().sendMessage(event.getPlayer(), "teams.full", team.getName());
                }
            }
        }, ultimateGames);
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            menu.setOption(i, team.getTeamIcon(), ChatColor.BOLD.toString() + ChatColor.AQUA + team.getName());
        }
        return menu;
    }

    @EventHandler
    public void onArenaOpen(ArenaOpenEvent event) {
        Arena arena = event.getArena();
        if (teamSelectors.containsKey(arena)) {
            teamSelectors.get(arena).destroy();
        }
        teamSelectors.put(arena, getTeamSelector(arena));
    }
}
