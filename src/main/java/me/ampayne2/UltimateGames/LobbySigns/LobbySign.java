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
package me.ampayne2.UltimateGames.LobbySigns;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Enums.PlayerType;

public class LobbySign {

	private Sign sign;
	private Arena arena;

	/**
	 * Creates a new lobby sign
	 * 
	 * @param sign Sign to be turned into lobby sign
	 * @param arena Arena of the lobby sign
	 */
	public LobbySign(Sign sign, Arena arena) {
		this.sign = sign;
		this.arena = arena;
		update();
	}

	/**
	 * Gets the Lobby Sign's Sign
	 * 
	 * @return sign The Lobby Sign's Sign
	 */
	public Sign getSign() {
		return sign;
	}

	/**
	 * Gets the Lobby Sign's Arena
	 * 
	 * @return arena The Lobby Sign's Arena
	 */
	public Arena getArena() {
		return arena;
	}

	/**
	 * Gets the updated lines for the sign</br> 
	 * line[0] is the Arena Status</br> 
	 * line[1] is the Game Name</br> 
	 * line[2] is the Arena Name</br> 
	 * line[3] is Current Players / Max Players (Blank if infinite)
	 * 
	 * @return lines the updated lines for the sign
	 */
	public String[] getUpdatedLines() {

		String[] lines = new String[4];

		ArenaStatus arenaStatus = arena.getStatus();
		ChatColor statusColor = ChatColor.BLACK;
		if (arenaStatus == ArenaStatus.RESET_FAILED) {
			statusColor = ChatColor.DARK_RED;
		} else if (arenaStatus == ArenaStatus.OPEN) {
			statusColor = ChatColor.GREEN;
		} else {
			statusColor = ChatColor.GRAY;
		}
		
		if(arenaStatus == ArenaStatus.ARENA_STOPPED || arenaStatus == ArenaStatus.GAME_STOPPED) {
			lines[0] = statusColor + "[STOPPED]";
		}else{
			lines[0] = statusColor + "[" + arenaStatus.toString() + "]";
		}

		lines[1] = arena.getGame().getGameDescription().getName();

		lines[2] = arena.getName();

		PlayerType playerType = arena.getGame().getGameDescription().getPlayerType();
		if (playerType == PlayerType.INFINITE) {
			lines[3] = "";
		} else {
			lines[3] = statusColor + Integer.toString(arena.getPlayers().size()) + " / " + Integer.toString(arena.getMaxPlayers());
		}

		return lines;

	}

	/**
	 * Updates the Lobby Sign
	 */
	public void update() {
		String[] lines = getUpdatedLines();
		for (int i = 0; i < 4; i++) {
			sign.setLine(i, lines[i]);
		}
		sign.update();
	}

}
