package me.ampayne2.ultimategames.chests;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.block.Chest;

public class StaticChest extends UGChest {
    
    @SuppressWarnings("unused")
    private UltimateGames ultimateGames;
    @SuppressWarnings("unused")
    private String label;

    public StaticChest(UltimateGames ultimateGames, String label, Chest chest, Arena arena) {
        super(chest, arena);
        this.ultimateGames = ultimateGames;
        this.label = label;
    }

    @Override
    public void reset() {
        
    }

}
