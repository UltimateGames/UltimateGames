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
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Games.Game;

public abstract class GamePlugin implements Listener{
	public abstract Boolean loadGame(UltimateGames ultimateGames, Game game);
	public abstract Boolean unloadGame();
	public abstract Boolean stopGame();
	public abstract Boolean loadArena(Arena arena);
	public abstract Boolean unloadArena(Arena arena);
	public abstract Boolean changeArenaStatus(Arena arena, ArenaStatus status);
	public abstract Boolean addPlayer(Arena arena, String playerName);
	public abstract Boolean removePlayer(Arena arena, String playerName);
	public abstract void onGameCommand(String command, CommandSender sender, String[] args);
	public abstract void onArenaCommand(Arena arena, String command, CommandSender sender, String[] args);
	public abstract void handleInputSignClick(Arena arena, Sign sign, PlayerInteractEvent event);
}
