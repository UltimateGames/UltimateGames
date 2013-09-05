package me.ampayne2.ultimategames.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;

public class TeamManager implements Manager {
    
    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private Map<Arena, List<Team>> teams = new HashMap<Arena, List<Team>>();
    
    public TeamManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }
    
    @Override
    public boolean load() {
        loaded = true;
        return true;
    }

    @Override
    public boolean reload() {
        Iterator<Entry<Arena, List<Team>>> it = teams.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Arena, List<Team>> entry = it.next();
            for (Team team : entry.getValue()) {
                removeTeam(team);
            }
        }
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        Iterator<Entry<Arena, List<Team>>> it = teams.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Arena, List<Team>> entry = it.next();
            for (Team team : entry.getValue()) {
                removeTeam(team);
            }
        }
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
    
    /**
     * Checks if a team of an arena with a certain name exists.
     * @param arena The arena.
     * @param name The name of the team.
     * @return True if the team exists, else null.
     */
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
    
    /**
     * Gets a team of an arena with a certain name.
     * @param arena The arena.
     * @param name The name of the team.
     * @return The team if it exists, else null.
     */
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
    
    /**
     * Gets the teams of an arena.
     * @param arena The arena.
     * @return The teams of the arena.
     */
    public List<Team> getTeamsOfArena(Arena arena) {
        return loaded && teams.containsKey(arena) ? teams.get(arena) : new ArrayList<Team>();
    }
    
    /**
     * Removes all the teams of an arena.
     * @param arena The arena.
     */
    public void removeTeamsOfArena(Arena arena) {
        if (loaded && teams.containsKey(arena)) {
            List<Team> arenaTeams = teams.get(arena);
            for (Team team : arenaTeams) {
                team.removePlayers();
            }
            teams.remove(arena);
        }
    }
    
    /**
     * Adds a team to the manager.
     * @param team The team to add.
     * @return True if the team was added successfully, else false.
     */
    public boolean addTeam(Team team) {
        Arena arena = team.getArena();
        List<Team> arenaTeams = loaded && teams.containsKey(arena) ? teams.get(arena) : new ArrayList<Team>();
        if (!arenaTeams.contains(team)) {
            arenaTeams.add(team);
            teams.put(arena, arenaTeams);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Removes a team from the manager.
     * @param team The team to remove.
     */
    public void removeTeam(Team team) {
        Arena arena = team.getArena();
        if (loaded && teams.containsKey(arena)) {
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
    
    /**
     * Checks if a player is in an arena team.
     * @param playerName The player to check.
     * @return True if the player is in a team, else false.
     */
    public boolean isPlayerInTeam(String playerName) {
        Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
        if (loaded && arena != null && teams.containsKey(arena)) {
            for (Team team : teams.get(arena)) {
                if (team.hasPlayer(playerName)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Gets a player's team.
     * @param playerName The player's name.
     * @return The player's team. Null if the player isn't in a team.
     */
    public Team getPlayerTeam(String playerName) {
        Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
        Team playerTeam = null;
        if (loaded && arena != null && teams.containsKey(arena)) {
            for (Team team : teams.get(arena)) {
                if (team.hasPlayer(playerName)) {
                    playerTeam = team;
                }
            }
        }
        return playerTeam;
    }
    
    /**
     * Sets a player's team. Removes them from their current team if they are in one.<br>
     * Only sets the player's team if its arena is the same as the one the player is currently in.
     * @param player The player.
     * @param team The team to add the player to.
     * @return True if the player is in an arena and the player's arena is the same as the team's arena, else false.
     */
    public boolean setPlayerTeam(Player player, Team team) {
        String playerName = player.getName();
        if (loaded && ultimateGames.getPlayerManager().isPlayerInArena(playerName) && ultimateGames.getPlayerManager().getArenaPlayer(playerName).equals(team.getArena())) {
            Team oldTeam = getPlayerTeam(playerName);
            if (oldTeam != null) {
                oldTeam.removePlayer(player);
                ultimateGames.getMessageManager().sendReplacedMessage(player, "teams.leave", oldTeam.getColor() + oldTeam.getName());
            }
            team.addPlayer(player);
            ultimateGames.getMessageManager().sendReplacedMessage(player, "teams.join", team.getColor() + team.getName());
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Removes a player from their team if they are in one.
     * @param player The player to remove from their team.
     */
    public void removePlayerFromTeam(Player player) {
        String playerName = player.getName();
        if (loaded) {
            Team team = getPlayerTeam(playerName);
            if (team != null) {
                team.removePlayer(player);
            }
        }
    }
    
    /**
     * Evenly sorts players in an arena into the arena's teams.<br>
     * Kicks players from the arena to stop uneven teams.<br>
     * Players who haven't joined a team yet are kicked before players who have.
     * @param arena The arena to sort the players of.
     */
    public void sortPlayersIntoTeams(Arena arena) {
        if (!loaded) {
            return;
        }
        List<Team> teams = getTeamsOfArena(arena);
        List<String> players = arena.getPlayers();
        List<String> playersInTeams = new ArrayList<String>();
        List<String> playersNotInTeams = arena.getPlayers();
        for (Team team : teams) {
            for (String playerName : team.getPlayers()) {
                playersInTeams.add(playerName);
                playersNotInTeams.remove(playerName);
            }
        }
        int teamAmount = teams.size();
        int playerAmount = players.size();
        int playersInTeamsAmount = playersInTeams.size();
        int playersNotInTeamsAmount = playersNotInTeams.size();
        if (teamAmount <= 0) {
            return;
        }
        
        // Make it possible for each team to have the same amount of players.
        Random generator = new Random();
        while (playerAmount % teamAmount != 0) {
            if (playersNotInTeamsAmount > 0) {
                String playerName = playersNotInTeams.get(playersNotInTeamsAmount - 1);
                Player player = Bukkit.getPlayerExact(playerName);
                ultimateGames.getPlayerManager().removePlayerFromArena(player, false);
                ultimateGames.getMessageManager().sendMessage(player, "arenas.kick");
                playersNotInTeams.remove(playerName);
                playersNotInTeamsAmount = playersNotInTeams.size();
            } else {
                String playerName = playersInTeams.get(playersInTeamsAmount - 1);
                Player player = Bukkit.getPlayerExact(playerName);
                getPlayerTeam(playerName).removePlayer(player);
                ultimateGames.getPlayerManager().removePlayerFromArena(player, false);
                ultimateGames.getMessageManager().sendMessage(player, "arenas.kick");
                playersInTeams.remove(playerName);
                playersInTeamsAmount = playersInTeams.size();
            }
            players = arena.getPlayers();
            playerAmount = players.size();
        }
        
        // Kick the last players to join each team from their team if the team is too full.
        for (Team team : teams) {
            List<String> teamPlayers = team.getPlayers();
            while ((playerAmount / teamAmount) < teamPlayers.size()) {
                String playerName = teamPlayers.get(teamPlayers.size() - 1);
                Player player = Bukkit.getPlayerExact(playerName);
                team.removePlayer(player);
                ultimateGames.getMessageManager().sendReplacedMessage(player, "teams.kick", team.getColor() + team.getName());
                playersNotInTeams.add(playerName);
            }
        }
        
        // Add the players not in teams yet to the teams that need players.
        for (Team team : teams) {
            while ((playerAmount / teamAmount) > team.getPlayers().size()) {
                String playerName = playersNotInTeams.get(generator.nextInt(playersNotInTeams.size()));
                Player player = Bukkit.getPlayerExact(playerName);
                team.addPlayer(player);
                ultimateGames.getMessageManager().sendReplacedMessage(player, "teams.join", team.getColor() + team.getName());
            }
        }
    }

}
