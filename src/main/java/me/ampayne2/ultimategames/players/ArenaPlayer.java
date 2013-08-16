package me.ampayne2.ultimategames.players;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ampayne2.ultimategames.arenas.Arena;

public class ArenaPlayer {
    
    private String playerName;
    private Arena arena;
    
    public ArenaPlayer(String playerName, Arena arena) {
        this.playerName = playerName;
        this.arena = arena;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayerExact(playerName);
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public Arena getArena() {
        return arena;
    }

}
