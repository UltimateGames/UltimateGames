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
package me.ampayne2.UltimateGames.Command;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Command.SubCommand;
import me.ampayne2.UltimateGames.Command.Commands.SetLobby;
import me.ampayne2.UltimateGames.Command.Commands.SpawnParticle;
import me.ampayne2.UltimateGames.Command.Commands.Arenas.AddSpawn;
import me.ampayne2.UltimateGames.Command.Commands.Arenas.Create;
import me.ampayne2.UltimateGames.Command.Commands.Arenas.Leave;
import me.ampayne2.UltimateGames.Command.Commands.Arenas.Begin;
import me.ampayne2.UltimateGames.Command.Commands.Arenas.End;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin {

	@SuppressWarnings("unused")
	private UltimateGames ultimateGames;
	private final SubCommand mainCommand = new SubCommand();

	public CommandController(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		
		SubCommand arena = new SubCommand();
		Create create = new Create(ultimateGames);
		ultimateGames.getServer().getPluginManager().registerEvents(create, ultimateGames);
		arena.addCommand("create", "ultimategames.create.arena", create);
		arena.addCommand("addspawn", "ultimategames.arena.addspawn", new AddSpawn(ultimateGames));
		arena.addCommand("begin", "ultimategames.arena.begin", new Begin(ultimateGames));
		arena.addCommand("end", "ultimategames.arena.end", new End(ultimateGames));
		mainCommand.addCommand("arena", null, arena);
		
		mainCommand.addCommand("leave", "ultimategames.arena.leave", new Leave(ultimateGames));
		
		mainCommand.addCommand("setlobby", "ultimategames.setlobby", new SetLobby(ultimateGames));
		
		mainCommand.addCommand("SpawnParticle", "ultimategames.spawnparticle", new SpawnParticle());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("ultimategames")) {
			return false;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		}

		if (args.length == 0) {
			if (mainCommand.commandExist("")) {
				mainCommand.execute("", sender, args);
				return true;
			} else {
				// Invalid Argument. Valid arguments are mainCommand.getSubCommandList());
				return true;
			}
		}

		if (mainCommand.commandExist(args[0])) {
			String[] newArgs;
			if (args.length == 1) {
				newArgs = new String[0];
			} else {
				newArgs = new String[args.length - 1];
				System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			}

			mainCommand.execute(args[0], sender, newArgs);
			return true;
		} else {
			// Invalid Arguments. Valid arguments are mainCommand.getSubCommandList());
			return true;
		}

	}
}
