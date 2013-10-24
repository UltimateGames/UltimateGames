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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Open implements UGCommand {
	private UltimateGames ultimateGames;

	public Open(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String arenaName = args[0];
		String gameName = args[1];
		if (ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
			Arena arena = ultimateGames.getArenaManager().getArena(arenaName, gameName);
			ultimateGames.getArenaManager().openArena(arena);
			ultimateGames.getMessageManager().sendMessage((Player) sender, "arenas.setstatus", arena.getName(), arena.getGame().getName(), arena.getStatus().name());
		}
	}
}
