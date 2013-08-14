package me.ampayne2.ultimategames.arenas;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockChange {
    
    public Arena arena;
    public Material material;
    public byte data;
    public Location location;
    
    public BlockChange(Arena arena, Material material, byte data, Location location) {
        this.arena = arena;
        this.material = material;
        this.data = data;
        this.location = location;
    }

}
