/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.core.arenas;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.blocks.GameBlockManager;
import me.ampayne2.ultimategames.api.games.items.GameItem;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.api.players.PlayerManager;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.api.players.teams.TeamManager;
import me.ampayne2.ultimategames.api.utils.UGUtils;
import me.ampayne2.ultimategames.api.whitelist.Whitelist;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;

/**
 * Handles all arena events.
 */
public class ArenaListener implements Listener {
    private final UG ultimateGames;

    /**
     * Creates a new ArenaListener.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public ArenaListener(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Blocks block placing inside an arena for players not in that arena,<br>
     * or if the block's material is not whitelisted for placing.
     * @param event The {@link org.bukkit.event.block.BlockPlaceEvent} event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            Game game = arena.getGame();
            Player player = event.getPlayer();
            String playerName = player.getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                if (!ultimateGames.getPlayerManager().getArenaPlayer(playerName).isEditing()) {
                    Material material = event.getBlock().getType();
                    if (arena.getStatus() == ArenaStatus.RUNNING && ultimateGames.getWhitelistManager().getBlockPlaceWhitelist().isWhitelisted(arena.getGame(), material)) {
                        if (!ultimateGames.getGameBlockManager().isRegistered(game, material)) {
                            arena.getGame().getGamePlugin().onBlockPlace(arena, event);
                        } else if (!ultimateGames.getGameBlockManager().getGameBlock(game, material).place(arena, event)) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Blocks block breaking inside an arena for players not in that arena,<br>
     * or if the block's material is not whitelisted for breaking,<br>
     * or if the block is a GameBlock that cannot be broken.
     * <p></p>
     * Stops blocks with ug signs attached from breaking.
     * @param event The {@link org.bukkit.event.block.BlockBreakEvent} event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null) {
            String playerName = event.getPlayer().getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                if (!ultimateGames.getPlayerManager().getArenaPlayer(playerName).isEditing()) {
                    if (arena.getStatus() == ArenaStatus.RUNNING && ultimateGames.getWhitelistManager().getBlockBreakWhitelist().isWhitelisted(arena.getGame(), event.getBlock().getType())) {
                        Block block = event.getBlock();
                        Material material = block.getType();
                        if ((material == Material.SIGN_POST || material == Material.WALL_SIGN) && ultimateGames.getSignManager().isSign((Sign) block.getState())) {
                            event.setCancelled(true);
                            return;
                        }
                        for (Sign attachedSign : UGUtils.getAttachedSigns(block, true)) {
                            if (ultimateGames.getSignManager().isSign(attachedSign)) {
                                event.setCancelled(true);
                                return;
                            }
                        }
                        Game game = arena.getGame();
                        if (!ultimateGames.getGameBlockManager().isRegistered(game, material)) {
                            arena.getGame().getGamePlugin().onBlockBreak(arena, event);
                        } else if (!ultimateGames.getGameBlockManager().getGameBlock(game, material).canBeBroken()) {
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
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
     * @param event The {@link org.bukkit.event.entity.EntityExplodeEvent} event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof TNTPrimed || entity instanceof Creeper || entity instanceof Fireball) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(entity.getLocation());
            if (arena != null) {
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    Game game = arena.getGame();
                    GameBlockManager gameBlockManager = ultimateGames.getGameBlockManager();
                    if (arena.allowExplosionBlockBreaking()) {
                        Whitelist<Material> blockBreakWhitelist = ultimateGames.getWhitelistManager().getBlockBreakWhitelist();
                        for (Block block : new ArrayList<>(event.blockList())) {
                            Material material = block.getType();
                            if (blockBreakWhitelist.isBlacklisted(arena.getGame(), material) || (gameBlockManager.isRegistered(game, material) && !gameBlockManager.getGameBlock(game, material).canBeBroken())) {
                                event.blockList().remove(block);
                            }
                        }
                        arena.getGame().getGamePlugin().onEntityExplode(arena, event);
                    } else if (arena.allowExplosionDamage()) {
                        event.blockList().clear();
                        arena.getGame().getGamePlugin().onEntityExplode(arena, event);
                    } else {
                        event.setCancelled(true);
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
     * @param event The {@link org.bukkit.event.entity.EntityDamageByEntityEvent} event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (entity instanceof TNTPrimed || entity instanceof Creeper || entity instanceof Fireball) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getEntity().getLocation());
            if (arena != null && !arena.allowExplosionDamage()) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Cancels block physics events in arenas not currently running.
     * @param event The {@link org.bukkit.event.block.BlockPhysicsEvent} event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Material material = event.getChangedType();
        if (material == Material.WATER || material == Material.LAVA || UGUtils.hasPhysics(material)) {
            Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
            if (arena != null && arena.getStatus() != ArenaStatus.RUNNING) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Cancels block fade events in arenas not currently running.
     * @param event The {@link org.bukkit.event.block.BlockFadeEvent} event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
        if (arena != null && arena.getStatus() == ArenaStatus.RUNNING) {
            arena.getGame().getGamePlugin().onBlockFade(arena, event);
        } else {
            event.setCancelled(true);
        }
    }

    /**
     * Handles player death events, removes the death message.
     * @param event The {@link org.bukkit.event.entity.PlayerDeathEvent} event.
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerName = event.getEntity().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            arena.getGame().getGamePlugin().onPlayerDeath(arena, event);
            event.setDeathMessage("");
        }
    }

    /**
     * Handles player respawn events.
     * @param event The {@link org.bukkit.event.player.PlayerRespawnEvent} event.
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
     * @param event The {@link org.bukkit.event.entity.EntityDamageEvent} event.
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
     * @param event The {@link org.bukkit.event.entity.EntityDamageByEntityEvent} event.
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
                } else if (damagerEntity instanceof Projectile) {
                    Projectile projectile = (Projectile) damagerEntity;
                    ProjectileSource shooter = projectile.getShooter();
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
                        if (playerTeam != null && playerTeam.equals(damagerTeam) && !playerTeam.hasFriendlyFire()) {
                            ultimateGames.getMessenger().sendMessage(damager, UGMessage.TEAM_FRIENDLYFIRE);
                            event.setCancelled(true);
                        } else {
                            arena.getGame().getGamePlugin().onEntityDamageByEntity(arena, event);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                }
            } else if (playerManager.isPlayerSpectatingArena(playerName)) {
                Entity damagerEntity = event.getDamager();
                if (damagerEntity instanceof Projectile) {
                    Projectile projectile = (Projectile) damagerEntity;

                    player.teleport(player.getLocation().add(0, 5, 0));
                    player.setFlying(true);

                    Projectile newProjectile = projectile.getWorld().spawn(projectile.getLocation(), projectile.getClass());
                    newProjectile.setShooter(projectile.getShooter());
                    newProjectile.setVelocity(projectile.getVelocity());
                    newProjectile.setBounce(false);

                    event.setCancelled(true);
                    projectile.remove();
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
     * @param event {@link org.bukkit.event.player.PlayerInteractEvent} event
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            Game game = arena.getGame();
            ItemStack item = player.getItemInHand();
            if (item != null && ultimateGames.getGameItemManager().isRegistered(game, item)) {
                GameItem gameItem = ultimateGames.getGameItemManager().getGameItem(game, item);
                event.setCancelled(true);
                if (gameItem.click(arena, event) && gameItem.hasSingleUse()) {
                    ItemStack itemStack = player.getItemInHand();
                    itemStack.setAmount(itemStack.getAmount() - 1);
                    player.setItemInHand(itemStack.getAmount() == 0 ? null : itemStack);
                }
                player.updateInventory();
            } else {
                game.getGamePlugin().onPlayerInteract(arena, event);
            }
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles player interactions with entities in arenas.<br>
     * If the player is in an arena, the game's onPlayerInteractEntity method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     * @param event The {@link org.bukkit.event.player.PlayerInteractEntityEvent} event.
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            Game game = arena.getGame();
            ItemStack item = player.getItemInHand();
            if (item != null && ultimateGames.getGameItemManager().isRegistered(game, item)) {
                GameItem gameItem = ultimateGames.getGameItemManager().getGameItem(game, item);
                event.setCancelled(true);
                if (gameItem.click(arena, event) && gameItem.hasSingleUse()) {
                    ItemStack itemStack = player.getItemInHand();
                    itemStack.setAmount(itemStack.getAmount() - 1);
                    player.setItemInHand(itemStack.getAmount() == 0 ? null : itemStack);
                }
                player.updateInventory();
            } else {
                game.getGamePlugin().onPlayerInteractEntity(arena, event);
            }
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles player food level changes.<br>
     * If the player is in an arena, the game's onPlayerFoodLevelChange method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     * @param event The {@link org.bukkit.event.entity.FoodLevelChangeEvent} event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            String playerName = event.getEntity().getName();
            if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    arena.getGame().getGamePlugin().onPlayerFoodLevelChange(arena, event);
                } else {
                    event.setCancelled(true);
                }
            } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Handles item picking up.<br>
     * If the player is in an arena, the game's onItemPickup method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     * @param event The {@link org.bukkit.event.player.PlayerPickupItemEvent} event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            if (arena.getStatus() == ArenaStatus.RUNNING) {
                arena.getGame().getGamePlugin().onItemPickup(arena, event);
            } else {
                event.setCancelled(true);
            }
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles item dropping.<br>
     * If the player is in an arena, the game's onItemDrop method is called.<br>
     * If the player is spectating an arena, the event is cancelled.
     * @param event the {@link org.bukkit.event.player.PlayerDropItemEvent} event.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            if (arena.getStatus() == ArenaStatus.RUNNING) {
                arena.getGame().getGamePlugin().onItemDrop(arena, event);
            } else {
                event.setCancelled(true);
            }
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles mob spawning.<br>
     * Checks if an arena allows mob spawning before letting a mob spawn inside it.
     * @param event The {@link org.bukkit.event.entity.CreatureSpawnEvent} event.
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            if (arena.getStatus() == ArenaStatus.RUNNING) {
                arena.getGame().getGamePlugin().onInventoryOpen(arena, event);
            } else {
                event.setCancelled(true);
            }
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        String playerName = event.getPlayer().getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
            if (arena.getStatus() == ArenaStatus.RUNNING) {
                arena.getGame().getGamePlugin().onPlayerItemConsume(arena, event);
            } else {
                event.setCancelled(true);
            }
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();
        if (ultimateGames.getMessenger().sendPlayerChatMessage(playerName, message)) {
            ultimateGames.getMessenger().getLogger().info("<" + playerName + "> " + message);
            event.setCancelled(true);
        }
    }
}
