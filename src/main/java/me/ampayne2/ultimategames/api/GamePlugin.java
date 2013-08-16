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
package me.ampayne2.ultimategames.api;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.SignType;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.signs.UGSign;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

public abstract class GamePlugin implements Listener {

    /**
     * Called when a game is being loaded.
     * @param ultimateGames The UltimateGames plugin instance.
     * @param game The game that is being loaded.
     * @return True if everything went well else false.
     */
    public abstract Boolean loadGame(UltimateGames ultimateGames, Game game);

    /**
     * Called when a game is being unloaded.
     * @return True if everything went well else false.
     */
    public abstract Boolean unloadGame();

    /**
     * Sets all arenas to ArenaStatus.GAME_STOPPED. Disables the game.
     * @return True if everything went well else false.
     */
    public abstract Boolean stopGame();

    /**
     * Called when an arena is being loaded.
     * @param arena The arena being loaded.
     * @return True if everything went well else false.
     */
    public abstract Boolean loadArena(Arena arena);

    /**
     * Called when an arena is being unloaded.
     * @param arena The arena being unloaded.
     * @return True if everything went well else false.
     */
    public abstract Boolean unloadArena(Arena arena);

    /**
     * Checks to see if a start is possible. Used in force starting etc.
     * @param arena The arena to attempt to start.
     * @return True if everything went well else false.
     */
    public abstract Boolean isStartPossible(Arena arena);

    /**
     * Sets to ArenaStatus.STARTING, called at countdown start. Should be used for preparing an arena.
     * @param arena The arena to start.
     * @return True if everything went well else false.
     */
    public abstract Boolean startArena(Arena arena);

    /**
     * Sets to ArenaStatus.RUNNING, called at countdown end.
     * @param arena The arena to begin.
     * @return True if everything went well else false.
     */
    public abstract Boolean beginArena(Arena arena);

    /**
     * Sets to ArenaStatus.ENDING. Called when the arena ends.
     * @param arena The arena to stop.
     * @return True if everything went well else false.
     */
    public abstract void endArena(Arena arena);

    /**
     * Sets to ArenaStatus.RESETTING. Should be used to reset anything in the arena. If returns false, sets to ArenaStatus.RESET_FAILED.
     * @param arena The arena to reset.
     * @return True if everything went well else false.
     */
    public abstract Boolean resetArena(Arena arena);

    /**
     * Sets to ArenaStatus.OPEN. Called when a arena is open.
     * @param arena The arena being opened.
     * @return True if everything went well else false.
     */
    public abstract Boolean openArena(Arena arena);

    /**
     * Sets to ArenaStatus.ARENA_STOPPED. Disables the arena.
     * @param arena The arena to disable.
     * @return True if everything went well else false.
     */
    public abstract Boolean stopArena(Arena arena);

    /**
     * Handle player joining.
     * @param arena The arena the player is joining.
     * @param playerName The ArenaPlayer object of the player joining.
     * @return True if the player is added else false.
     */
    public abstract Boolean addPlayer(Player player, Arena arena);

    /**
     * Handle player leaving.
     * @param arena The arena the player is leaving.
     * @param arenaPlayer The ArenaPlayer object of the player leaving.
     */
    public abstract void removePlayer(Player player, Arena arena);

    /**
     * Handle UG Sign creation.
     * @param ugSign The UG Sign that was created.
     */
    public void handleUGSignCreate(UGSign ugSign, SignType signType) {}

    /**
     * Handle UG Input Sign triggering.
     * @param ugSign The UG Sign that was triggered.
     * @param signType The SignType of the UG Sign.
     */
    public void handleInputSignTrigger(UGSign ugSign, SignType signType, Event event) {}

    /**
     * Handle arena commands.
     * @param arena The arena that the command was done in.
     * @param command The command.
     * @param sender The sender of the command.
     * @param args The arguments.
     */
    public void onArenaCommand(Arena arena, String command, CommandSender sender, String[] args) {}
    
    /**
     * Handles block placing.
     * @param arena The arena.
     * @param event The BlockPlaceEvent.
     */
    public void onBlockPlace(Arena arena, BlockPlaceEvent event) {}
    
    /**
     * Handles block breaking.
     * @param arena The arena.
     * @param event The BlockBreakEvent.
     */
    public void onBlockBreak(Arena arena, BlockBreakEvent event) {}

    /**
     * Handle player death.
     * @param arena The arena that the player died in.
     * @param event The PlayerDeathEvent.
     */
    public void onPlayerDeath(Arena arena, PlayerDeathEvent event) {}

    /**
     * Handle player respawning.
     * @param arena The arena the player is respawning in.
     * @param event The PlayerRespawnEvent.
     */
    public void onPlayerRespawn(Arena arena, PlayerRespawnEvent event) {}

    /**
     * Handle entity damage.
     * @param arena The arena the entity got damaged in.
     * @param event The EntityDamageEvent.
     */
    public void onEntityDamage(Arena arena, EntityDamageEvent event) {}

    /**
     * Handle entities damaging entities.
     * @param arena The arena the entity got damaged in.
     * @param event The EntityDamageByEntityEvent.
     */
    public void onEntityDamageByEntity(Arena arena, EntityDamageByEntityEvent event) {}
    
    /**
     * Handle entity explosions.
     * @param arena The arena the entity exploded in.
     * @param event The EntityExplodeEvent.
     */
    public void onEntityExplode(Arena arena, EntityExplodeEvent event) {}

    /**
     * Handles player interactions.
     * @param arena The arena the player is in.
     * @param event The PlayerInteractEvent.
     */
    public void onPlayerInteract(Arena arena, PlayerInteractEvent event) {}
    
    /**
     * Handles player food level changes.
     * @param arena The arena the player is in.
     * @param event The FoodLevelChangeEvent.
     */
    public void onPlayerFoodLevelChange(Arena arena, FoodLevelChangeEvent event) {}
    
    /**
     * Handles players picking up items.
     * @param arena The arena the player is in.
     * @param event The PlayerPickupItemEvent.
     */
    public void onItemPickup(Arena arena, PlayerPickupItemEvent event) {}
    
    /**
     * Handles players dropping items.
     * @param arena The arena the player is in.
     * @param event The PlayerDropItemEvent.
     */
    public void onItemDrop(Arena arena, PlayerDropItemEvent event) {}

}
