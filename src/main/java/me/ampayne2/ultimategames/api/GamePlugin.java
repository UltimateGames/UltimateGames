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
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public abstract class GamePlugin implements Listener {
    /**
     * Setup anything needed for the game to function.
     * @param ultimateGames The UltimateGames plugin instance
     * @param game The game that is being loaded
     * @return True if everything went well else false
     */
    public abstract Boolean loadGame(UltimateGames ultimateGames, Game game);

    /**
     * Called when removing the game from the game manager.
     * @return True if everything went well else false
     */
    public abstract Boolean unloadGame();

    /**
     * Sets all arenas to ArenaStatus.GAME_STOPPED. Disables the game.
     * @return True if everything went well else false
     */
    public abstract Boolean stopGame();

    /**
     * Load a arena.
     * @param arena The arena to load
     * @return True if everything went well else false
     */
    public abstract Boolean loadArena(Arena arena);

    /**
     * Called when removing the arena from the arena manager.
     * @param arena The arena to unload
     * @return True if everything went well else false
     */
    public abstract Boolean unloadArena(Arena arena);

    /**
     * Checks to see if a start is possible. Used in force starting etc.
     * @param arena The arena to attempt to start
     * @return True if everything went well else false
     */
    public abstract Boolean isStartPossible(Arena arena);

    /**
     * Sets to ArenaStatus.STARTING, called at countdown start. Should be used for preparing an arena.
     * @param arena The arena to start
     * @return True if everything went well else false
     */
    public abstract Boolean startArena(Arena arena);

    /**
     * Sets to ArenaStatus.RUNNING, called at countdown end.
     * @param arena The arena to begin
     * @return True if everything went well else false
     */
    public abstract Boolean beginArena(Arena arena);

    /**
     * Sets to ArenaStatus.ENDING. Called when the arena ends
     * @param arena The arena to stop
     * @return True if everything went well else false
     */
    public abstract Boolean endArena(Arena arena);

    /**
     * Sets to ArenaStatus.RESETTING. Should be used to reset anything in the arena. If returns false, sets to ArenaStatus.RESET_FAILED.
     * @param arena The arena to reset
     * @return True if everything went well else false
     */
    public abstract Boolean resetArena(Arena arena);

    /**
     * Sets to ArenaStatus.OPEN. Called when a arena is open.
     * @param arena The arena being opened
     * @return True if everything went well else false
     */
    public abstract Boolean openArena(Arena arena);

    /**
     * Sets to ArenaStatus.ARENA_STOPPED. Disables the arena.
     * @param arena The arena to disable
     * @return True if everything went well else false
     */
    public abstract Boolean stopArena(Arena arena); //

    /**
     * Handle player joining.
     * @param arena The arena the player is joining
     * @param playerName The player that join
     * @return True if the player is added else false
     */
    public abstract Boolean addPlayer(Arena arena, String playerName);

    /**
     * Handle player leaving.
     * @param arena The arena the player is leaving
     * @param playerName the player that leave
     * @return True if everything went well else false
     */
    public abstract Boolean removePlayer(Arena arena, String playerName);

    /**
     * Handle arena command.
     * @param arena The arena that the command was done
     * @param command The command
     * @param sender The server of the command
     * @param args the arguments.
     * @return
     */
    public abstract Boolean onArenaCommand(Arena arena, String command, CommandSender sender, String[] args);

    /**
     * Handle UG Sign creation.
     * @param ugSign
     */
    public abstract void handleUGSignCreate(UGSign ugSign, SignType signType);
    
    /**
     * Handle UG Input Sign triggering.
     * @param ugSign
     * @param signType
     */
    public abstract void handleInputSignTrigger(UGSign ugSign, SignType signType, Event event);
    
}
