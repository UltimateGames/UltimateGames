/*
 * This file is part of UltimateGames.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.arenas;

import me.ampayne2.ultimategames.UltimateGames;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ArenaListener implements Listener {
    private UltimateGames ultimateGames;

    public ArenaListener(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Blocks block breaking in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null && !arena.getArenaSetting("allowBreaking")) {
            event.setCancelled(true);
            ultimateGames.getMessageManager().sendMessage(event.getPlayer().getName(), "protections.break");
            return;
        }
        if (ultimateGames.getPlayerManager().isPlayerInArena(event.getPlayer().getName())) {
            arena = ultimateGames.getPlayerManager().getPlayerArena(event.getPlayer().getName());
            if (arena != null && !arena.getArenaSetting("allowBreaking")) {
                event.setCancelled(true);
                ultimateGames.getMessageManager().sendMessage(event.getPlayer().getName(), "protections.break");
                return;
            }
        }
    }

    /**
     * Blocks block placing in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null && !arena.getArenaSetting("allowBuilding")) {
            event.setCancelled(true);
            ultimateGames.getMessageManager().sendMessage(event.getPlayer().getName(), "protections.place");
            return;
        }
        if (ultimateGames.getPlayerManager().isPlayerInArena(event.getPlayer().getName())) {
            arena = ultimateGames.getPlayerManager().getPlayerArena(event.getPlayer().getName());
            if (arena != null && !arena.getArenaSetting("allowBuilding")) {
                event.setCancelled(true);
                ultimateGames.getMessageManager().sendMessage(event.getPlayer().getName(), "protections.place");
            }
        }
    }

    /**
     * Blocks tnt breaking blocks in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTntExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getLocation());
            if (arena != null) {
                if (!arena.getArenaSetting("allowExplosionDamage") && !arena.getArenaSetting("allowExplosionBlockBreaking")) {
                    event.setCancelled(true);
                } else if (!arena.getArenaSetting("allowExplosionBlockBreaking")) {
                    event.blockList().clear();
                }
            }
        }
    }

    /**
     * Blocks tnt damaging entities in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTntDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof TNTPrimed && event.getEntity() instanceof Player) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null && !arena.getArenaSetting("allowExplosionDamage")) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Stops death messages if player is in an arena.
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            event.setDeathMessage("");
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerDeath(arena, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerRespawn(arena, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            String playerName = ((Player) event.getEntity()).getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                arena.getGame().getGamePlugin().onEntityDamage(arena, event);
            }
        } else {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null) {
                arena.getGame().getGamePlugin().onEntityDamage(arena, event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            String playerName = ((Player) event.getEntity()).getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                arena.getGame().getGamePlugin().onEntityDamageByEntity(arena, event);
            }
        } else {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null) {
                arena.getGame().getGamePlugin().onEntityDamageByEntity(arena, event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerInteract(arena, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            String playerName = ((Player) event.getEntity()).getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                arena.getGame().getGamePlugin().onPlayerFoodLevelChange(arena, event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPickup(PlayerPickupItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onItemPickup(arena, event);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onItemDrop(arena, event);
        }
    }
}
