package me.ampayne2.ultimategames.chests;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.block.Chest;

public class RandomChest extends UGChest {
    
    @SuppressWarnings("unused")
    private UltimateGames ultimateGames;

    public RandomChest(UltimateGames ultimateGames, Chest chest, Arena arena) {
        super(chest, arena);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void reset() {
        
    }

}
