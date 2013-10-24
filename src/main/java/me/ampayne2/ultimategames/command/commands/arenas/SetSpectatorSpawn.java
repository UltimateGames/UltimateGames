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

import me.ampayne2.ultimategames.Message;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.ArenaManager;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpectatorSpawn implements UGCommand {

	private UltimateGames ultimateGames;

	public SetSpectatorSpawn(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String arenaName = args[0];
		String gameName = args[1];
		Message messageManager = ultimateGames.getMessageManager();
		ArenaManager arenaManager = ultimateGames.getArenaManager();
		if (!ultimateGames.getGameManager().gameExists(gameName)) {
			messageManager.sendMessage((Player) sender, "games.doesntexist");
			return;
		} else if (!arenaManager.arenaExists(arenaName, gameName)) {
			messageManager.sendMessage((Player) sender, "arenas.doesntexist");
			return;
		}
		Player player = (Player) sender;
		ultimateGames.getSpawnpointManager().setSpectatorSpawnPoint(arenaManager.getArena(arenaName, gameName), player.getLocation());
		messageManager.sendMessage((Player) sender, "spawnpoints.setspectatorspawnpoint", arenaName, gameName);
	}

}
