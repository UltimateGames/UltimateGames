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
import me.ampayne2.ultimategames.command.interfaces.Command;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SubCommand implements Command {
    private final UltimateGames ultimateGames;
    private Map<String, Command> commandList = new HashMap<String, Command>();
    private Map<String, String> permissionList = new HashMap<String, String>();
    private Map<String, Integer> argsLength = new HashMap<String, Integer>();
    private Map<String, Boolean> playerOnly = new HashMap<String, Boolean>();

    public SubCommand(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    public void addCommand(Command command, String name, String permission, Integer argsLength, boolean playerOnly) {
        commandList.put(name, command);
        if (command instanceof UGCommand) {
            permissionList.put(name, permission);
            this.argsLength.put(name, argsLength);
            this.playerOnly.put(name, playerOnly);
        }
    }

    public boolean commandExists(String name) {
        return commandList.containsKey(name);
    }

    public void execute(String command, CommandSender sender, String[] args) {
        if (commandExists(command)) {
            Command entry = commandList.get(command);
            if (entry instanceof UGCommand) {
                if (argsLength.get(command) == -1 || argsLength.get(command) == args.length) {
                    if (checkPermission(sender,permissionList.get(command))) {
                        if (sender instanceof Player || !playerOnly.get(command)) {
                            ((UGCommand) entry).execute(sender, args);
                        } else {
                            ultimateGames.getMessageManager().sendMessage(sender, "commands.notaplayer");
                        }
                    } else {
                        ultimateGames.getMessageManager().sendMessage(sender, "permissions.nopermission", command);
                    }
                } else {
                    ultimateGames.getMessageManager().sendMessage(sender, "commandusages." + command);
                }
            } else if (entry instanceof SubCommand) {
                SubCommand subCommand = (SubCommand) entry;

                String subSubCommand = "";
                if (args.length != 0) {
                    subSubCommand = args[0];
                }

                if (subCommand.commandExists(subSubCommand)) {
                    String[] newArgs;
                    if (args.length == 0) {
                        newArgs = args;
                    } else {
                        newArgs = new String[args.length - 1];
                        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    }
                    subCommand.execute(subSubCommand, sender, newArgs);
                } else {
                    ultimateGames.getMessageManager().sendMessage(sender, "commands.invalidsubcommand", "\"" + subSubCommand + "\"", "\"" + command + "\"");
                    ultimateGames.getMessageManager().sendMessage(sender, "commands.validsubcommands", subCommand.getSubCommandList());
                }
            }
        }
    }

    public String getSubCommandList() {
        return Arrays.toString(commandList.keySet().toArray());
    }

    private boolean checkPermission(CommandSender sender, String permission) {
        boolean result = true;
        if (permission != null) {
            result = sender.hasPermission(permission);
        }
        return result;
    }
}
