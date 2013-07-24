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
package me.ampayne2.UltimateGames.Command.Commands.Arenas;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;

public class AddSpawn implements UGCommand{

	private UltimateGames ultimateGames;
	
	public AddSpawn(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 3 || !(args[2].equals("true") || args[2].equals("false"))) {
			return;
		}
		String arenaName = args[0];
		String gameName = args[1];
		Boolean locked = Boolean.valueOf(args[2]);
		if (!ultimateGames.getGameManager().gameExists(gameName) || !ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
			return; //game or arena doesn't exist
		}
		Player player = (Player) sender;
		ultimateGames.getSpawnpointManager().createSpawnPoint(ultimateGames.getArenaManager().getArena(arenaName, gameName), player.getLocation(), locked);
		ultimateGames.getMessageManager().sendReplacedMessage(player.getName(), "spawnpoints.create", arenaName, gameName);
	}

}
