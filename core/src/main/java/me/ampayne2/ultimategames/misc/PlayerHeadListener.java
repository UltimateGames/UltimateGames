/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.misc;

import me.ampayne2.ultimategames.UG;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.shininet.bukkit.playerheads.events.LivingEntityDropHeadEvent;

/**
 * Handles head dropping with the PlayerHeads plugin.<br>
 */
public class PlayerHeadListener implements Listener {
    private final UG ultimateGames;

    /**
     * Creates a new PlayerHeadListener.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public PlayerHeadListener(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Cancels the event if the entity is a player in an arena,<br>
     * or if the entity is not a player and its location is inside an arena.<br>
     * http://dev.bukkit.org/bukkit-plugins/player-heads/
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadDrop(LivingEntityDropHeadEvent event) {
        Entity entity = event.getEntity();
        if ((entity instanceof Player && ultimateGames.getPlayerManager().isPlayerInArena(((Player) entity).getName())) || ultimateGames.getArenaManager().getLocationArena(entity.getLocation()) != null) {
            event.setCancelled(true);
        }
    }
}
