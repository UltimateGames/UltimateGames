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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.ampayne2.UltimateGames.Command.SubCommand;
import me.ampayne2.UltimateGames.Command.interfaces.Command;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;

public class SubCommand implements Command{
	
	private Map<String, Command> commandList = new HashMap<String, Command>();
	private Map<String, String> permissionList = new HashMap<String, String>();

	public void addCommand(String name, String permission, Command command) {
		commandList.put(name, command);
		permissionList.put(name, permission);
	}

	public boolean commandExist(String name) {
		return commandList.containsKey(name);
	}
	
	public boolean hasCommandPermission(CommandSender sender, String command) {
		if (sender.hasPermission(permissionList.get(command))) {
			return true;
		} else {
			return false;
		}
	}

	public void execute(String command, CommandSender sender, String[] args) {
		if (commandExist(command)) {
			Command entry = commandList.get(command);
			if (entry instanceof UGCommand) {
				((UGCommand) entry).execute(sender, args);
			} else if (entry instanceof SubCommand) {
				SubCommand subCommand = (SubCommand) entry;

				String subSubCommand = "";
				if (args.length != 0) {
					subSubCommand = args[0];
				}

				if (subCommand.commandExist(subSubCommand)) {
					String[] newArgs;
					if (args.length == 0) {
						newArgs = args;
					} else {
						newArgs = new String[args.length - 1];
						System.arraycopy(args, 1, newArgs, 0, args.length - 1);
					}
					((SubCommand) entry).execute(subSubCommand, sender, newArgs);
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "Invalid argument. Valid arguments are: " + subCommand.getSubCommandList());
				}

			}

		}
	}

	public String getSubCommandList() {
		return Arrays.toString(commandList.keySet().toArray());
	}

}
