package me.ampayne2.ultimategames.chests;

import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

public abstract class UGChest {
    
    private Chest chest;
    private Arena arena;

    /**
     * Creates a new chest
     * @param chest Chest to be turned into UGChest.
     * @param arena Arena of the chest.
     */
    public UGChest(Chest chest, Arena arena) {
        this.chest = chest;
        this.arena = arena;
    }
    
    /**
     * Resets the contents of a UG Chest.
     */
    public abstract void reset();

    /**
     * Gets the UGChest's Chest.
     * @return The UGChest's Chest.
     */
    public Chest getChest() {
        return chest;
    }
    
    /**
     * Gets the UGChest's Inventory.
     * @return The UGChest's Inventory.
     */
    public Inventory getInventory() {
        return chest.getInventory();
    }

    /**
     * Gets the UGChest's Arena.
     * @return arena The UGChest's Arena.
     */
    public Arena getArena() {
        return arena;
    }

}
