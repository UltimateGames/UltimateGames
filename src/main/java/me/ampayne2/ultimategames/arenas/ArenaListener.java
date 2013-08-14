/*
 * This file is part of UltimateGames.
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames. If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.arenas;

import java.util.List;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
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
     * Blocks block placing in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            if (arena.getStatus() == ArenaStatus.RUNNING && ultimateGames.getWhitelistManager().getBlockPlaceWhitelist().canPlaceMaterial(arena.getGame(), event.getBlock().getType())) {
                arena.getGame().getGamePlugin().onBlockPlace(arena, event);
                ultimateGames.getLogManager().logBlockChange(arena, event.getBlockReplacedState().getType(), event.getBlockReplacedState().getRawData(), event.getBlockReplacedState().getLocation());
            } else {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Blocks block breaking in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            if (arena.getStatus() == ArenaStatus.RUNNING && ultimateGames.getWhitelistManager().getBlockBreakWhitelist().canBreakMaterial(arena.getGame(), event.getBlock().getType())) {
                Block block = event.getBlock();
                if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                    event.setCancelled(true);
                } else if (block.getRelative(BlockFace.UP).getType() == Material.SIGN_POST) {
                    event.setCancelled(true);
                } else if (block.getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN) {
                    event.setCancelled(true);
                } else if (block.getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN) {
                    event.setCancelled(true);
                } else if (block.getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN) {
                    event.setCancelled(true);
                } else if (block.getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN) {
                    event.setCancelled(true);
                } else {
                    arena.getGame().getGamePlugin().onBlockBreak(arena, event);
                    if ((block.getType() == Material.WOODEN_DOOR || block.getType() == Material.IRON_DOOR_BLOCK) && (block.getData() & 0x8) == 0x8) {
                        ultimateGames.getLogManager().logBlockChange(arena, block.getType(), (byte) (block.getData() & ~0x8), block.getRelative(BlockFace.DOWN).getLocation());
                    } else {
                        ultimateGames.getLogManager().logBlockChange(arena, block.getType(), block.getData(), block.getLocation());
                    }
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Blocks tnt breaking blocks in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof TNTPrimed || event.getEntity() instanceof Creeper || event.getEntity() instanceof Fireball) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null) {
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    if (!arena.getArenaSetting("allowExplosionDamage") && !arena.getArenaSetting("allowExplosionBlockBreaking")) {
                        event.setCancelled(true);
                    } else if (!arena.getArenaSetting("allowExplosionBlockBreaking")) {
                        event.blockList().clear();
                    } else {
                        List<Block> canBeBroken = ultimateGames.getWhitelistManager().getBlockBreakWhitelist().blocksWhitelisted(arena.getGame(), event.blockList());
                        event.blockList().clear();
                        event.blockList().addAll(canBeBroken);
                        for (Block block : canBeBroken) {
                            ultimateGames.getLogManager().logBlockChange(arena, block.getType(), block.getData(), block.getLocation());
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Blocks tnt damaging entities in arenas when not allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof TNTPrimed && event.getEntity() instanceof Player) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null && !arena.getArenaSetting("allowExplosionDamage")) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Blocks piston extension in arenas.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            event.setCancelled(true);
        }
    }

    /**
     * Blocks piston retraction in arenas.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            event.setCancelled(true);
        }
    }

    /**
     * Logs block physics events in arenas.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            if (arena.getStatus() == ArenaStatus.RUNNING) {
                Block block = event.getBlock();
                ultimateGames.getLogManager().logBlockChange(arena, block.getType(), block.getData(), block.getLocation());
            } else if (arena.getStatus() == ArenaStatus.RESETTING) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Stops death messages if player is in an arena.
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            event.setDeathMessage("");
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerDeath(arena, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerRespawn(arena, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            String playerName = ((Player) event.getEntity()).getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                arena.getGame().getGamePlugin().onPlayerFoodLevelChange(arena, event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onItemPickup(arena, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onItemDrop(arena, event);
        }
    }
}
