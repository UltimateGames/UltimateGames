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
package me.ampayne2.ultimategames.players;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArenaSpectator {
	private String playerName;
	private Arena arena;

	/**
	 * Represents a player spectating an arena.
	 *
	 * @param playerName The player's name.
	 * @param arena      The arena.
	 */
	public ArenaSpectator(String playerName, Arena arena) {
		this.playerName = playerName;
		this.arena = arena;
	}

	/**
	 * Gets the player. Calls Bukkit.getPlayerExact(), so is an expensive call.
	 *
	 * @return The player.
	 */
	public Player getPlayer() {
		return Bukkit.getPlayerExact(playerName);
	}

	/**
	 * Gets a player's name.
	 *
	 * @return The player's name.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Gets the player's arena.
	 *
	 * @return The player's arena.
	 */
	public Arena getArena() {
		return arena;
	}
}
