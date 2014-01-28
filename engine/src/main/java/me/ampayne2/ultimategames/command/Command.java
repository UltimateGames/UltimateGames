/*
 * This file is part of UltimateGames ENGINE.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames ENGINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames ENGINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames ENGINE.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.command;

import me.ampayne2.ultimategames.UG;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The base layout for a command.
 */
public class Command {
    private final UG ultimateGames;
    private final String name;
    private final Permission permission;
    private final int minArgsLength;
    private final int maxArgsLength;
    private final boolean playerOnly;
    private final Map<String, Command> children = new LinkedHashMap<>();

    /**
     * Creates a new Command that must have an amount of args within a range.
     *
     * @param ultimateGames The UltimateGames instance.
     * @param name          The name of the command.
     * @param permission    The permission of the command.
     * @param minArgsLength The minimum required args length of the command.
     * @param maxArgsLength The maximum required args length of the command.
     * @param playerOnly    If the command can only be run by a player.
     */
    public Command(UG ultimateGames, String name, Permission permission, int minArgsLength, int maxArgsLength, boolean playerOnly) {
        this.ultimateGames = ultimateGames;
        this.name = name.toLowerCase();
        this.permission = permission;
        this.minArgsLength = minArgsLength;
        this.maxArgsLength = maxArgsLength;
        this.playerOnly = playerOnly;
        try {
            ultimateGames.getServer().getPluginManager().addPermission(permission);
        } catch (IllegalArgumentException e) {
            ultimateGames.getMessenger().debug(e);
        }
    }

    /**
     * Creates a new Command that must have an exact amount of args.
     *
     * @param ultimateGames   The UltimateGames instance.
     * @param name            The name of the command.
     * @param permission      The permission of the command.
     * @param exactArgsLength The exact required args length of the command.
     * @param playerOnly      If the command can only be run by a player.
     */
    public Command(UG ultimateGames, String name, Permission permission, int exactArgsLength, boolean playerOnly) {
        this(ultimateGames, name, permission, exactArgsLength, exactArgsLength, playerOnly);
    }

    /**
     * Creates a new Command that can have any amount of args.
     *
     * @param ultimateGames The UltimateGames instance.
     * @param name          The name of the command.
     * @param permission    The permission of the command.
     * @param playerOnly    If the command can only be run by a player.
     */
    public Command(UG ultimateGames, String name, Permission permission, boolean playerOnly) {
        this(ultimateGames, name, permission, -1, -1, playerOnly);
    }

    /**
     * Gets the command's name.
     *
     * @return The command's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the command's permission.
     *
     * @return The command's permission.
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Gets the minimum required args length for the command.
     *
     * @return The minimum required args length.
     */
    public int getMinArgsLength() {
        return minArgsLength;
    }

    /**
     * Gets the maximum required args length for the command.
     *
     * @return The maximum required args length.
     */
    public int getMaxArgsLength() {
        return maxArgsLength;
    }

    /**
     * Checks to see if the command can only be run by a player.
     *
     * @return True if the command is player only, else false.
     */
    public boolean isPlayerOnly() {
        return playerOnly;
    }

    /**
     * Adds a child command to the command.
     *
     * @param command The child command.
     * @return The command the child command was added to.
     */
    public Command addChildCommand(Command command) {
        children.put(command.getName().toLowerCase(), command);
        command.getPermission().addParent(permission, true);
        return this;
    }

    /**
     * Checks to see if the command has the child command.
     *
     * @param name The name of the child command.
     * @return True if the command has the child command, else false.
     */
    public boolean hasChildCommand(String name) {
        return children.containsKey(name.toLowerCase());
    }

    /**
     * Gets a child command of the command.
     *
     * @param name The name of the child command.
     * @return The child command.
     */
    public Command getChildCommand(String name) {
        return children.get(name.toLowerCase());
    }

    /**
     * Gets the command's children.
     *
     * @param deep If the method should return all children, or only the command's immediate children.
     * @return The command's children.
     */
    public List<Command> getChildren(boolean deep) {
        if (deep) {
            List<Command> deepChildren = new ArrayList<>();
            for (Command child : children.values()) {
                if (child instanceof UGCommand) {
                    deepChildren.add(child);
                }
                for (Command deepChild : child.getChildren(true)) {
                    deepChildren.add(deepChild);
                }
            }
            return deepChildren;
        } else {
            return new ArrayList<>(children.values());
        }
    }

    /**
     * Gets the tab completion list of the command.
     *
     * @param args The args already entered.
     * @return The tab completion list of the command.
     */
    public List<String> getTabCompleteList(String[] args) {
        return new ArrayList<>(children.keySet());
    }

    /**
     * The command executor
     *
     * @param sender The sender of the command
     * @param args   The arguments sent with the command
     */
    public void execute(String command, CommandSender sender, String[] args) {
        Command entry = children.get(command.toLowerCase());
        if (entry instanceof UGCommand) {
            if ((entry.getMinArgsLength() <= args.length || entry.getMinArgsLength() == -1) && (entry.getMaxArgsLength() >= args.length || entry.getMaxArgsLength() == -1)) {
                if (sender.hasPermission(entry.getPermission())) {
                    if (sender instanceof Player || !entry.isPlayerOnly()) {
                        entry.execute(command, sender, args);
                    } else {
                        ultimateGames.getMessenger().sendMessage(sender, "error.command.notaplayer");
                    }
                } else {
                    ultimateGames.getMessenger().sendMessage(sender, "permissions.nopermission", command);
                }
            } else {
                ultimateGames.getMessenger().sendMessage(sender, "error.command.usage", ((UGCommand) entry).getCommandUsage());
            }
        } else {
            String subCommand = args.length == 0 ? "" : args[0];
            if (entry.hasChildCommand(subCommand)) {
                String[] newArgs;
                if (args.length == 0) {
                    newArgs = args;
                } else {
                    newArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                }
                entry.execute(subCommand, sender, newArgs);
            } else {
                ultimateGames.getMessenger().sendMessage(sender, "error.command.invalidsubcommand", "\"" + subCommand + "\"", "\"" + command + "\"");
            }
        }
    }
}
