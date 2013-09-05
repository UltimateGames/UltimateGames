package me.ampayne2.ultimategames.teams;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.scoreboards.ArenaScoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * A class that represents a team in an arena.
 */
public class Team {
    
    private UltimateGames ultimateGames;
    private ChatColor color;
    private String name;
    private Arena arena;
    private boolean friendlyFire;
    private List<String> players = new ArrayList<String>();
    
    /**
     * Creates a new team.
     * @param ultimateGames A reference to the ultimateGames instance.
     * @param arena The arena of the team.
     * @param color The color of the team.
     * @param name The name of the team.
     */
    public Team(UltimateGames ultimateGames, Arena arena, ChatColor color, String name, boolean friendlyFire) {
        this.ultimateGames = ultimateGames;
        this.arena = arena;
        this.color = color;
        // Keeps the name from being longer than 16 (14 + color) and potentially crashing players in the arena
        this.name = name.substring(0, Math.min(name.length(), 14));
        this.friendlyFire = friendlyFire;
    }
    
    /**
     * Gets the team's arena.
     * @return The team's arena.
     */
    public Arena getArena() {
        return arena;
    }
    
    /**
     * Gets the team's color.
     * @return The team's color.
     */
    public ChatColor getColor() {
        return color;
    }
    
    /**
     * Gets the team's name.
     * @return The team's name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Checks if the team has friendly fire enabled.
     * @return True if the team has friendly fire enabled, else false.
     */
    public boolean hasFriendlyFire() {
        return friendlyFire;
    }
    
    /**
     * Turns the team's friendly fire on or off.
     * @param enabled True for friendly fire, False for no friendly fire.
     */
    public void setFriendlyFire(boolean enabled) {
        friendlyFire = enabled;
    }
    
    /**
     * Checks if a team has a player.
     * @param playerName The player's name.
     * @return True if the player is in the team, else false.
     */
    public boolean hasPlayer(String playerName) {
        return players.contains(playerName);
    }
    
    /**
     * Checks if a team has space for another player while staying even with other teams.
     * @return True if the team has space, else false.
     */
    public boolean hasSpace() {
        return Math.floor(arena.getPlayers().size() / ultimateGames.getTeamManager().getTeamsOfArena(arena).size()) > players.size();
    }
    
    /**
     * Gets the players in a team.
     * @return The players in a team.
     */
    public List<String> getPlayers() {
        return players;
    }
    
    /**
     * Adds a player to the team.
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
     * @param player The player to remove from the team.
     */
    public void removePlayer(Player player) {
        String playerName = player.getName();
        if (players.contains(playerName)) {
            players.remove(playerName);
            ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.resetPlayerColor(player);
            }
        }
    }
    
    /**
     * Removes all players from the team.
     */
    public void removePlayers() {
        for (String playerName : players) {
            ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.resetPlayerColor(Bukkit.getPlayerExact(playerName));
            }
        }
        players.clear();
    }
    
    /**
     * Sets a player's color to the team's color.
     * @param player The player.
     */
    public void setPlayerColorToTeamColor(Player player) {
        ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
        if (scoreBoard != null) {
            scoreBoard.setPlayerColor(player, color);
        }
    }

}
