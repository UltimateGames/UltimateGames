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
package me.ampayne2.UltimateGames.Signs;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Enums.PlayerType;
import me.ampayne2.UltimateGames.Players.QueueManager;

public class LobbySign extends UGSign{
	
	private UltimateGames ultimateGames;
	private Arena arena;
	
	public LobbySign(UltimateGames ultimateGames, Sign sign, Arena arena) {
		super(sign, arena);
		this.ultimateGames = ultimateGames;
		this.arena = arena;
		update();
	}
	
	@Override
	public void onSignClick(PlayerInteractEvent event) {
		//TODO: Permission check
		ArenaStatus arenaStatus = arena.getStatus();
		if (arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING) {
			// TODO: Save and clear player data (inventory, armor, levels, gamemode, effects)
			ultimateGames.getPlayerManager().addPlayerToArena(event.getPlayer().getName(), arena, true);
			return;
		} else if (arenaStatus == ArenaStatus.RUNNING || arenaStatus == ArenaStatus.ENDING || arenaStatus == ArenaStatus.RESETTING
				|| arena.getPlayers().size() >= arena.getMaxPlayers()) {
			QueueManager queue = ultimateGames.getQueueManager();
			String playerName = event.getPlayer().getName();
			if (queue.isPlayerInQueue(playerName, arena)) {
				queue.removePlayerFromQueues(playerName);
			} else {
				queue.addPlayerToQueue(playerName, arena);
			}
		}
	}

	@Override
	public String[] getUpdatedLines() {

		String[] lines = new String[4];

		ArenaStatus arenaStatus = arena.getStatus();
		ChatColor statusColor = ChatColor.BLACK;
		if (arenaStatus == ArenaStatus.RESET_FAILED) {
			statusColor = ChatColor.DARK_RED;
		} else if (arenaStatus == ArenaStatus.OPEN) {
			statusColor = ChatColor.GREEN;
		} else if (arenaStatus == ArenaStatus.ARENA_STOPPED || arenaStatus == ArenaStatus.GAME_STOPPED) {
			statusColor = ChatColor.BLACK;
		} else {
			statusColor = ChatColor.DARK_GRAY;
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
	
}
