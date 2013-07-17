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
package me.ampayne2.UltimateGames.API;

import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Games.Game;

public abstract class GamePlugin implements Listener{
	public abstract Boolean loadGame(UltimateGames ultimateGames, Game game); // Setup anything needed for the game to function.
	public abstract Boolean unloadGame(); // Called when removing the game from the game manager.
	public abstract Boolean stopGame(); // Sets all arenas to ArenaStatus.GAME_STOPPED. Disables the game.
	
	public abstract Boolean loadArena(Arena arena); // Setup anything needed for the game to function.
	public abstract Boolean unloadArena(Arena arena); // Called when removing the arena from the arena manager.
	
	public abstract Boolean startArena(Arena arena); // Sets to ArenaStatus.STARTING, called at countdown start. Should be used for preparing an arena.
	public abstract Boolean beginArena(Arena arena); // Sets to ArenaStatus.RUNNING, called at countdown end.
	public abstract Boolean endArena(Arena arena); // Sets to ArenaStatus.ENDING.
	public abstract Boolean resetArena(Arena arena); // Sets to ArenaStatus.RESETTING. Should be used to reset anything in the arena. If returns false, sets to ArenaStatus.RESET_FAILED.
	public abstract Boolean openArena(Arena arena); // Sets to ArenaStatus.OPEN.
	public abstract Boolean stopArena(Arena arena); // Sets to ArenaStatus.ARENA_STOPPED. Disables the arena.
	
	public abstract Boolean addPlayer(Arena arena, String playerName); // Handle player joining.
	public abstract Boolean removePlayer(Arena arena, String playerName); // Handle player leaving.
	
	public abstract void onGameCommand(String command, CommandSender sender, String[] args); // Handle game command.
	public abstract void onArenaCommand(Arena arena, String command, CommandSender sender, String[] args); // Handle arena command.
	
	public abstract void handleInputSignCreate(Arena arena, Sign sign, String label); // Handle input sign creation.
	public abstract void handleInputSignClick(Arena arena, Sign sign, String label, PlayerInteractEvent event); // Handle input sign click.
}
