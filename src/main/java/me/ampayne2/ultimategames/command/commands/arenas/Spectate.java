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
package me.ampayne2.ultimategames.command.commands.arenas;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.players.QueueManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spectate implements UGCommand {

	private UltimateGames ultimateGames;

	public Spectate(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String arenaName = args[0];
		String gameName = args[1];
		if (ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
			Arena arena = ultimateGames.getArenaManager().getArena(arenaName, gameName);
			Player player = (Player) sender;
			String playerName = player.getName();
			if (!ultimateGames.getPlayerManager().isPlayerInArena(playerName) && !ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
				ArenaStatus arenaStatus = arena.getStatus();
				if (arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING || arenaStatus == ArenaStatus.RUNNING) {
					// TODO: Save and clear player data (inventory, armor, levels, gamemode, effects)
					ultimateGames.getPlayerManager().addSpectatorToArena(player, arena);
					return;
				} else if (arenaStatus == ArenaStatus.ENDING || arenaStatus == ArenaStatus.RESETTING || arena.getPlayers().size() >= arena.getMaxPlayers()) {
					QueueManager queue = ultimateGames.getQueueManager();
					if (!queue.isPlayerInQueue(playerName, arena)) {
						queue.addPlayerToQueue(player, arena);
					}
				}
			}
		}
	}

}
