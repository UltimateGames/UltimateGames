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

import java.util.HashSet;
import java.util.Set;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.players.PlayerManager;
import me.ampayne2.ultimategames.teams.Team;
import me.ampayne2.ultimategames.teams.TeamManager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
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
    private static final int DOOR_BIT = 0x8;

    public ArenaListener(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Blocks block placing inside an arena for players not in that arena,<br>
     * or if the block's material is not whitelisted for placing.<br>
     * Logs the block placement for resetting if the arena requires resets,<br>
     * and if the game's onBlockPlace method doesn't cancel the event.
     * 
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            String playerName = event.getPlayer().getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                if (ultimateGames.getPlayerManager().getArenaPlayer(playerName).isEditing()) {
                    return;
                } else if (arena.getStatus() == ArenaStatus.RUNNING && ultimateGames.getWhitelistManager().getBlockPlaceWhitelist().canPlaceMaterial(arena.getGame(), event.getBlock().getType())
                        && arena.resetAfterMatch()) {
                    arena.getGame().getGamePlugin().onBlockPlace(arena, event);
                    if (!event.isCancelled()) {
                        ultimateGames.getLogManager().logBlockChange(arena, event.getBlockReplacedState().getType(), event.getBlockReplacedState().getRawData(),
                                event.getBlockReplacedState().getLocation());
                    }
                } else {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Blocks block breaking inside an arena for players not in that arena,<br>
     * or if the block's material is not whitelisted for breaking.<br>
     * Logs the block break for resetting if the arena requires resets,<br>
     * and if the game's onBlockBreak method doesn't cancel the event.<br><br>
     * 
     * Stops blocks with signs attached from breaking.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            String playerName = event.getPlayer().getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                if (ultimateGames.getPlayerManager().getArenaPlayer(playerName).isEditing()) {
                    return;
                } else if (arena.getStatus() == ArenaStatus.RUNNING && arena.resetAfterMatch()
                        && ultimateGames.getWhitelistManager().getBlockBreakWhitelist().canBreakMaterial(arena.getGame(), event.getBlock().getType())) {
                    Block block = event.getBlock();
                    Block up = block.getRelative(BlockFace.UP);
                    Block east = block.getRelative(BlockFace.EAST);
                    Block north = block.getRelative(BlockFace.NORTH);
                    Block south = block.getRelative(BlockFace.SOUTH);
                    Block west = block.getRelative(BlockFace.WEST);
                    if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                        event.setCancelled(true);
                    } else if (up.getType() == Material.SIGN_POST) {
                        event.setCancelled(true);
                    } else if (east.getType() == Material.WALL_SIGN && ultimateGames.getUtils().isAttachedToBlock(east, block)) {
                        event.setCancelled(true);
                    } else if (north.getType() == Material.WALL_SIGN && ultimateGames.getUtils().isAttachedToBlock(north, block)) {
                        event.setCancelled(true);
                    } else if (south.getType() == Material.WALL_SIGN && ultimateGames.getUtils().isAttachedToBlock(south, block)) {
                        event.setCancelled(true);
                    } else if (west.getType() == Material.WALL_SIGN && ultimateGames.getUtils().isAttachedToBlock(west, block)) {
                        event.setCancelled(true);
                    } else {
                        arena.getGame().getGamePlugin().onBlockBreak(arena, event);
                        if (arena.resetAfterMatch()) {
                            if (!((block.getType() == Material.WOODEN_DOOR || block.getType() == Material.IRON_DOOR_BLOCK) && (block.getData() & DOOR_BIT) == DOOR_BIT)) {
                                ultimateGames.getLogManager().logBlockChange(arena, block.getType(), block.getData(), block.getLocation());
                            }
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles explosion block damage caused by TnT, Creepers, or Fireballs.<br>
     * If the arena allows explosion block breaking, each block is handled separately<br>
     * as if a player attempted to destroy it. If the arena doesn't allow explosion block breaking,<br>
     * no blocks are broken.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof TNTPrimed || entity instanceof Creeper || entity instanceof Fireball) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(entity.getLocation());
            if (arena != null) {
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    if (arena.allowExplosionBlockBreaking() && arena.resetAfterMatch()) {
                        Set<Block> canBeBroken = ultimateGames.getWhitelistManager().getBlockBreakWhitelist().blocksWhitelisted(arena.getGame(), new HashSet<Block>(event.blockList()));
                        event.blockList().clear();
                        event.blockList().addAll(canBeBroken);
                        arena.getGame().getGamePlugin().onEntityExplode(arena, event);
                        if (arena.resetAfterMatch() && !event.isCancelled()) {
                            ultimateGames.getLogManager().logBlockChanges(arena, event.blockList());
                        }
                    } else {
                        if (arena.allowExplosionDamage()) {
                            event.blockList().clear();
                        } else {
                            event.setCancelled(true);
                        }
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Handles explosion damage caused by TnT, Creepers, or Fireballs in arenas.<br>
     * If the arena doesn't allow explosion damage, the event is cancelled.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (event.getEntity() instanceof Player && (entity instanceof TNTPrimed || entity instanceof Creeper || entity instanceof Fireball)) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null && !arena.allowExplosionDamage()) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Blocks piston extension in arenas that require resets.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null && arena.resetAfterMatch()) {
            event.setCancelled(true);
        }
    }

    /**
     * Blocks piston retraction in arenas that require resets.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null && arena.resetAfterMatch()) {
            event.setCancelled(true);
        }
    }

    /**
     * Logs block physics events in arenas.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Material material = event.getChangedType();
        if (material == Material.WATER || material == Material.LAVA || ultimateGames.getUtils().hasPhysics(material)) {
            Block block = event.getBlock();
            Arena arena = ultimateGames.getArenaManager().getLocationArena(block.getLocation());
            if (arena != null) {
                if (arena.resetAfterMatch() && arena.getStatus() == ArenaStatus.RUNNING) {
                    ultimateGames.getLogManager().logBlockChange(arena, material, block.getData(), block.getLocation());
                } else if (arena.getStatus() == ArenaStatus.RESETTING) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Handles block fading events in arenas.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        Block block = event.getBlock();
        Arena arena = ultimateGames.getArenaManager().getLocationArena(block.getLocation());
        if (arena != null) {
            arena.getGame().getGamePlugin().onBlockFade(arena, event);
            if (!event.isCancelled() && arena.resetAfterMatch() && arena.getStatus() == ArenaStatus.RUNNING) {
                ultimateGames.getLogManager().logBlockChange(arena, block.getType(), block.getData(), block.getLocation());
            } else if (arena.getStatus() == ArenaStatus.RESETTING) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Calls a game's onPlayerDeath method, and stops death message when a player in an arena is killed.
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

    /**
     * Calls a game's onPlayerRespawn method when a player in an arena respawns.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerRespawn(arena, event);
        }
    }

    /**
     * Stops spectators from taking damage,<br>
     * Calls a game's onEntityDamage method.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            String playerName = ((Player) event.getEntity()).getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                arena.getGame().getGamePlugin().onEntityDamage(arena, event);
            } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
                event.setCancelled(true);
            }
        } else {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null) {
                arena.getGame().getGamePlugin().onEntityDamage(arena, event);
            }
        }
    }

    /**
     * Stops players not in an arena from damaging players in that arena,<br>
     * players in an arena from damaging players not in that arena,<br>
     * and players on the same team from damaging each other if the team has friendly fire off.<br>
     * Calls the game's onEntityDamageByEntity event if the damage is allowed.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();
            String playerName = player.getName();
            PlayerManager playerManager = ultimateGames.getPlayerManager();
            if (playerManager.isPlayerInArena(playerName)) {
                Arena arena = playerManager.getPlayerArena(playerName);

                Entity damagerEntity = event.getDamager();
                Player damager = null;
                if (damagerEntity instanceof Player) {
                    damager = (Player) damagerEntity;
                } else if (damagerEntity instanceof Arrow) {
                    Arrow arrow = (Arrow) damagerEntity;
                    LivingEntity shooter = arrow.getShooter();
                    if (shooter instanceof Player) {
                        damager = (Player) shooter;
                    }
                }

                if (damager != null) {
                    String damagerName = damager.getName();
                    if (playerManager.isPlayerInArena(damagerName) && playerManager.getPlayerArena(damagerName).equals(arena)) {
                        TeamManager teamManager = ultimateGames.getTeamManager();
                        Team playerTeam = teamManager.getPlayerTeam(playerName);
                        Team damagerTeam = teamManager.getPlayerTeam(damagerName);
                        if (playerTeam != null && damagerTeam != null && playerTeam.equals(damagerTeam) && !playerTeam.hasFriendlyFire()) {
                            ultimateGames.getMessageManager().sendMessage(damager, "teams.friendlyfire");
                            event.setCancelled(true);
                        } else {
                            arena.getGame().getGamePlugin().onEntityDamageByEntity(arena, event);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        } else {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null) {
                arena.getGame().getGamePlugin().onEntityDamageByEntity(arena, event);
            }
        }
    }

    /**
     * Handles player interactions in arenas.<br>
     * If the player is in an arena, the game's onPlayerInteract method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerInteract(arena, event);
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles player food level changes.<br>
     * If the player is in an arena, the game's onPlayerFoodLevelChange method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            String playerName = ((Player) event.getEntity()).getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                arena.getGame().getGamePlugin().onPlayerFoodLevelChange(arena, event);
            } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles item picking up.<br>
     * If the player is in an arena, the game's onItemPickup method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onItemPickup(arena, event);
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles item dropping.<br>
     * If the player is in an arena, the game's onItemDrop method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onItemDrop(arena, event);
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles mob spawning.<br>
     * Checks if an arena allows mob spawning before letting a mob spawn inside it.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == SpawnReason.NATURAL) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getLocation());
            if (arena != null && !arena.allowMobSpawning()) {
                event.setCancelled(true);
            }
        }
    }

}
