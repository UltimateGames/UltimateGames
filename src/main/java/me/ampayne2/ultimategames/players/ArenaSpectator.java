package me.ampayne2.ultimategames.players;

import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArenaSpectator {
    
    private String playerName;
    private Arena arena;
    
    /**
     * Represents a player spectating an arena.
     * @param playerName The player's name.
     * @param arena The arena.
     */
    public ArenaSpectator(String playerName, Arena arena) {
        this.playerName = playerName;
        this.arena = arena;
    }
    
    /**
     * Gets the player.
     * @return The player.
     */
    public Player getPlayer() {
        return Bukkit.getPlayerExact(playerName);
    }
    
    /**
     * Gets a player's name.
     * @return The player's name.
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Gets the player's arena.
     * @return The player's arena.
     */
    public Arena getArena() {
        return arena;
    }

}
