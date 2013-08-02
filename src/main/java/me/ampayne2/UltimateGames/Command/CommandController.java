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
package me.ampayne2.ultimategames.command;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.command.commands.SetLobby;
import me.ampayne2.ultimategames.command.commands.SpawnParticle;
import me.ampayne2.ultimategames.command.commands.arenas.AddSpawn;
import me.ampayne2.ultimategames.command.commands.arenas.Begin;
import me.ampayne2.ultimategames.command.commands.arenas.Create;
import me.ampayne2.ultimategames.command.commands.arenas.End;
import me.ampayne2.ultimategames.command.commands.arenas.Join;
import me.ampayne2.ultimategames.command.commands.arenas.Leave;
import me.ampayne2.ultimategames.command.commands.arenas.Open;
import me.ampayne2.ultimategames.command.commands.arenas.Stop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin implements Listener {
	private UltimateGames ultimateGames;
	private final SubCommand mainCommand = new SubCommand();

	public CommandController(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;

		SubCommand arena = new SubCommand();
		Create create = new Create(ultimateGames);
		ultimateGames.getServer().getPluginManager().registerEvents(create, ultimateGames);
		arena.addCommand("create", "ultimategames.arena.create", create);
		arena.addCommand("addspawn", "ultimategames.arena.addspawn", new AddSpawn(ultimateGames));
		arena.addCommand("open", "ultimategames.arena.open", new Open(ultimateGames));
		arena.addCommand("begin", "ultimategames.arena.begin", new Begin(ultimateGames));
		arena.addCommand("end", "ultimategames.arena.end", new End(ultimateGames));
		arena.addCommand("stop", "ultimategames.arena.stop", new Stop(ultimateGames));
		arena.addCommand("join", "ultimategames.arena.join", new Join(ultimateGames));
		arena.addCommand("leave", "ultimategames.arena.leave", new Leave(ultimateGames));
		mainCommand.addCommand("arena", null, arena);

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

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
			String[] command = event.getMessage().split(" ");
			if (command.length > 0 && !command[0].equalsIgnoreCase("/ug") && !command[0].equalsIgnoreCase("/ultimategames") && !player.hasPermission("ultimategames.bypasscommandblock")) {
				event.setCancelled(true);
				Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
				String[] args = new String[command.length - 1];
				System.arraycopy(command, 1, args, command.length - 1, command.length - 1);
				if (!arena.getGame().getGamePlugin().onArenaCommand(arena, command[0].replace("/", ""), (CommandSender) player, args)) {
					event.setCancelled(false);
				}
			}
		}
	}
}
