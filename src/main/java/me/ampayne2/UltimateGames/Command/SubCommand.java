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
