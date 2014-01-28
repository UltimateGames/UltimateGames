/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.games;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.signs.Sign;
import me.ampayne2.ultimategames.signs.SignType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

/**
 * The API used for creating an Ultimate Game.<br>
 * Extend this class, implement all the abstract methods, and override any non-abstract methods you may need.
 */
public abstract class GamePlugin implements Listener {

    /**
     * Called when a game is being loaded.
     *
     * @param ultimateGames The UltimateGames plugin instance.
     * @param game          The game that is being loaded.
     * @return True if everything went well else false.
     */
    public abstract boolean loadGame(UltimateGames ultimateGames, Game game);

    /**
     * Called when a game is being unloaded.
     */
    public abstract void unloadGame();

    /**
     * Called when a game is being reloaded.
     *
     * @return True if everything went well else false.
     */
    public abstract boolean reloadGame();

    /**
     * Sets all arenas to ArenaStatus.GAME_STOPPED. Disables the game.
     *
     * @return True if everything went well else false.
     */
    public abstract boolean stopGame();

    /**
     * Called when an arena is being loaded.
     *
     * @param arena The arena being loaded.
     * @return True if everything went well else false.
     */
    public abstract boolean loadArena(Arena arena);

    /**
     * Called when an arena is being unloaded.
     *
     * @param arena The arena being unloaded.
     * @return True if everything went well else false.
     */
    public abstract boolean unloadArena(Arena arena);

    /**
     * Checks to see if a start is possible. Used in force starting etc.
     *
     * @param arena The arena to attempt to start.
     * @return True if everything went well else false.
     */
    public abstract boolean isStartPossible(Arena arena);

    /**
     * Sets to ArenaStatus.STARTING, called at countdown start. Should be used for preparing an arena.
     *
     * @param arena The arena to start.
     * @return True if everything went well else false.
     */
    public abstract boolean startArena(Arena arena);

    /**
     * Sets to ArenaStatus.RUNNING, called at countdown end.
     *
     * @param arena The arena to begin.
     * @return True if everything went well else false.
     */
    public abstract boolean beginArena(Arena arena);

    /**
     * Sets to ArenaStatus.ENDING. Called when the arena ends.
     *
     * @param arena The arena to stop.
     */
    public abstract void endArena(Arena arena);

    /**
     * Sets to ArenaStatus.OPEN. Called when a arena is open.
     *
     * @param arena The arena being opened.
     * @return True if everything went well else false.
     */
    public abstract boolean openArena(Arena arena);

    /**
     * Sets to ArenaStatus.ARENA_STOPPED. Disables the arena.
     *
     * @param arena The arena to disable.
     * @return True if everything went well else false.
     */
    public abstract boolean stopArena(Arena arena);

    /**
     * Handle player joining.
     *
     * @param player The player joining.
     * @param arena  The arena the player is joining.
     * @return True if the player is added else false.
     */
    public abstract boolean addPlayer(Player player, Arena arena);

    /**
     * Handle player leaving.
     *
     * @param player The player leaving.
     * @param arena  The arena the player is leaving.
     */
    public abstract void removePlayer(Player player, Arena arena);

    /**
     * Handle spectator joining.
     *
     * @param player The spectator joining.
     * @param arena  The arena the spectator is joining.
     * @return True if the spectator is added else false.
     */
    public abstract boolean addSpectator(Player player, Arena arena);

    /**
     * Handle spectator leaving.
     *
     * @param player The spectator leaving.
     * @param arena  The arena the spectator is leaving.
     */
    public abstract void removeSpectator(Player player, Arena arena);

    /**
     * Handle turning players into spectators.
     *
     * @param player The player to turn into a spectator.
     * @param arena  The arena the player is in.
     */
    public void makePlayerSpectator(Player player, Arena arena) {
    }

    /**
     * Handle UG Sign creation.
     *
     * @param ugSign The UG Sign that was created.
     */
    public void handleUGSignCreate(Sign ugSign, SignType signType) {
    }

    /**
     * Handle UG Input Sign triggering.
     *
     * @param ugSign   The UG Sign that was triggered.
     * @param signType The SignType of the UG Sign.
     */
    public void handleInputSignTrigger(Sign ugSign, SignType signType, Event event) {
    }

    /**
     * Handle players joining the queue. Can be used to fill up an uneven team.
     *
     * @param player The player that joined the queue.
     * @param arena  The arena of the queue they joined.
     */
    public void onPlayerJoinQueue(Player player, Arena arena) {
    }

    /**
     * Handle arena commands.
     *
     * @param arena   The arena that the command was done in.
     * @param command The command.
     * @param sender  The sender of the command.
     * @param args    The arguments.
     */
    public void onArenaCommand(Arena arena, String command, CommandSender sender, String[] args) {
    }

    /**
     * Handles block placing.
     *
     * @param arena The arena.
     * @param event The BlockPlaceEvent.
     */
    public void onBlockPlace(Arena arena, BlockPlaceEvent event) {
    }

    /**
     * Handles block breaking.
     *
     * @param arena The arena.
     * @param event The BlockBreakEvent.
     */
    public void onBlockBreak(Arena arena, BlockBreakEvent event) {
    }

    /**
     * Handle player death.
     *
     * @param arena The arena that the player died in.
     * @param event The PlayerDeathEvent.
     */
    public void onPlayerDeath(Arena arena, PlayerDeathEvent event) {
    }

    /**
     * Handle player respawning.
     *
     * @param arena The arena the player is respawning in.
     * @param event The PlayerRespawnEvent.
     */
    public void onPlayerRespawn(Arena arena, PlayerRespawnEvent event) {
    }

    /**
     * Handle entity damage.
     *
     * @param arena The arena the entity got damaged in.
     * @param event The EntityDamageEvent.
     */
    public void onEntityDamage(Arena arena, EntityDamageEvent event) {
    }

    /**
     * Handle entities damaging entities.
     *
     * @param arena The arena the entity got damaged in.
     * @param event The EntityDamageByEntityEvent.
     */
    public void onEntityDamageByEntity(Arena arena, EntityDamageByEntityEvent event) {
    }

    /**
     * Handle entity explosions.
     *
     * @param arena The arena the entity exploded in.
     * @param event The EntityExplodeEvent.
     */
    public void onEntityExplode(Arena arena, EntityExplodeEvent event) {
    }

    /**
     * Handles player interactions.
     *
     * @param arena The arena the player is in.
     * @param event The PlayerInteractEvent.
     */
    public void onPlayerInteract(Arena arena, PlayerInteractEvent event) {
    }

    /**
     * Handles player food level changes.
     *
     * @param arena The arena the player is in.
     * @param event The FoodLevelChangeEvent.
     */
    public void onPlayerFoodLevelChange(Arena arena, FoodLevelChangeEvent event) {
    }

    /**
     * Handles players picking up items.
     *
     * @param arena The arena the player is in.
     * @param event The PlayerPickupItemEvent.
     */
    public void onItemPickup(Arena arena, PlayerPickupItemEvent event) {
    }

    /**
     * Handles players dropping items.
     *
     * @param arena The arena the player is in.
     * @param event The PlayerDropItemEvent.
     */
    public void onItemDrop(Arena arena, PlayerDropItemEvent event) {
    }

    /**
     * Handles players moving in arenas.
     *
     * @param arena The arena the player is in.
     * @param event The PlayerMoveEvent.
     */
    public void onPlayerMove(Arena arena, PlayerMoveEvent event) {
    }

    /**
     * Handles blocks fading.
     *
     * @param arena The arena the block is in.
     * @param event The BlockFadeEvent.
     */
    public void onBlockFade(Arena arena, BlockFadeEvent event) {
    }

    /**
     * Handles inventories opening.
     *
     * @param arena The arena the inventory is being opened in.
     * @param event The InventoryOpenEvent.
     */
    public void onInventoryOpen(Arena arena, InventoryOpenEvent event) {
    }

    /**
     * Handles item consumption.
     *
     * @param arena The arena the item is being consumed in.
     * @param event The PlayerItemConsumeEvent.
     */
    public void onPlayerItemConsume(Arena arena, PlayerItemConsumeEvent event) {
    }
}
