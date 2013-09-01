package me.ampayne2.ultimategames.events;

import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaStopEvent extends Event {
    
    private static final HandlerList handlers = new HandlerList();
    private Arena arena;

    public ArenaStopEvent(Arena arena) {
        this.arena = arena;
    }
    
    public Arena getArena() {
        return arena;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
