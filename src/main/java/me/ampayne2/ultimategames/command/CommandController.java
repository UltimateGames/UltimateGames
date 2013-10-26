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
import me.ampayne2.ultimategames.command.commands.ExtCmd;
import me.ampayne2.ultimategames.command.commands.Lobby;
import me.ampayne2.ultimategames.command.commands.Reload;
import me.ampayne2.ultimategames.command.commands.SetLobby;
import me.ampayne2.ultimategames.command.commands.arenas.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CommandController extends JavaPlugin implements Listener {
	private final UltimateGames ultimateGames;
	private final SubCommand mainCommand;
	private List<String> bypassers = new ArrayList<String>();

	public CommandController(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		mainCommand = new SubCommand(ultimateGames);

		SubCommand arena = new SubCommand(ultimateGames);
		Create create = new Create(ultimateGames);
		ultimateGames.getServer().getPluginManager().registerEvents(create, ultimateGames);
		arena.addCommand(create, "create", "ultimategames.arena.create", 2, true);
		arena.addCommand(new AddSpawn(ultimateGames), "addspawn", "ultimategames.arena.addspawn", 3, true);
		arena.addCommand(new SetSpectatorSpawn(ultimateGames), "setspectatorspawn", "ultimategames.arena.setspectatorspawn", 2, true);
		arena.addCommand(new Open(ultimateGames), "open", "ultimategames.arena.open", 2, false);
		arena.addCommand(new Begin(ultimateGames), "begin", "ultimategames.arena.begin", 2, false);
		arena.addCommand(new End(ultimateGames), "end", "ultimategames.arena.end", 2, false);
		arena.addCommand(new Stop(ultimateGames), "stop", "ultimategames.arena.stop", 2, false);
		arena.addCommand(new Join(ultimateGames), "join", "ultimategames.arena.join", 2, true);
		arena.addCommand(new Edit(ultimateGames), "edit", "ultimategames.arena.edit", 0, true);
		arena.addCommand(new Spectate(ultimateGames), "spectate", "ultimategames.arena.spectate", 2, true);
		mainCommand.addCommand(arena, "arena", null, null, false);

		mainCommand.addCommand(new Leave(ultimateGames), "leave", "ultimategames.arena.leave", 0, true);

		mainCommand.addCommand(new SetLobby(ultimateGames), "setlobby", "ultimategames.setlobby", 0, true);
		mainCommand.addCommand(new Lobby(ultimateGames), "lobby", "ultimategames.lobby", 0, true);

		mainCommand.addCommand(new ExtCmd(ultimateGames), "cmd", "ultimategames.extcmd", -1, true);

		mainCommand.addCommand(new Reload(ultimateGames), "reload", "ultimategames.reload", 0, false);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String subCommand = "";
		if (args.length > 0) {
			subCommand = args[0];
		}
		if (!cmd.getName().equalsIgnoreCase("ultimategames")) {
			return false;
		} else if (subCommand.equals("") && mainCommand.commandExists(subCommand)) {
			mainCommand.execute(subCommand, sender, args);
		} else if (mainCommand.commandExists(subCommand)) {
			String[] newArgs;
			if (args.length == 1) {
				newArgs = new String[0];
			} else {
				newArgs = new String[args.length - 1];
				System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			}
			mainCommand.execute(subCommand, sender, newArgs);
		} else {
			ultimateGames.getMessageManager().sendMessage(sender, "commands.invalidsubcommand", "\"" + subCommand + "\"", "\"ultimategames\"");
			ultimateGames.getMessageManager().sendMessage(sender, "commands.validsubcommands", mainCommand.getSubCommandList());
		}
		return true;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
			String[] command = event.getMessage().split(" ");
			if (command.length > 0 && !command[0].equalsIgnoreCase("/ug") && !command[0].equalsIgnoreCase("/ultimategames") && !bypassers.contains(player.getName())) {
				event.setCancelled(true);
				Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
				String[] args;
				if (command.length == 1) {
					args = new String[0];
				} else {
					args = new String[command.length - 1];
					System.arraycopy(command, 1, args, 0, command.length - 1);
				}
				arena.getGame().getGamePlugin().onArenaCommand(arena, command[0].replace("/", ""), player, args);
				event.setCancelled(true);
			}
		}
	}

	public void addBlockBypasser(String playerName) {
		bypassers.add(playerName);
	}

	public void removeBlockBypasser(String playerName) {
		if (bypassers.contains(playerName)) {
			bypassers.remove(playerName);
		}
	}
}
