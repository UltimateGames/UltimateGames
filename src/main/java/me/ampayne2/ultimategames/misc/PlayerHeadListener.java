package me.ampayne2.ultimategames.misc;

import me.ampayne2.ultimategames.UltimateGames;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shininet.bukkit.playerheads.events.LivingEntityDropHeadEvent;

public class PlayerHeadListener implements Listener {
    
    private UltimateGames ultimateGames;
    
    public PlayerHeadListener(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }
    
    /**
     * Handles head dropping with the PlayerHeads plugin.<br>
     * Cancels the event if the entity is a player in an arena,<br>
     * or if the entity is not a player and its location is inside an arena.<br>
     * http://dev.bukkit.org/bukkit-plugins/player-heads/
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadDrop(LivingEntityDropHeadEvent event) {
        Entity entity = event.getEntity();
        if ((entity instanceof Player && ultimateGames.getPlayerManager().isPlayerInArena(((Player) entity).getName()))
                || ultimateGames.getArenaManager().getLocationArena(entity.getLocation()) != null) {
            event.setCancelled(true);
        }
    }

}
