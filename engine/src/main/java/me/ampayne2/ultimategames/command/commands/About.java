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
package me.ampayne2.ultimategames.command.commands;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.command.UGCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that lists some information about ultimate games.
 */
public class About extends UGCommand {
    private final UG ultimateGames;
    private final static String HEADER = ChatColor.DARK_GRAY + "<-------<| " + ChatColor.AQUA + "About Ultimate Games " + ChatColor.DARK_GRAY + "|>------->";
    private final static String AUTHOR = ChatColor.AQUA + "Authors: ampayne2 and greatman321";
    private final static String VERSION = ChatColor.AQUA + "Version: ";
    private final static String COMMANDS = ChatColor.AQUA + "Commands: /ug help [page]";

    /**
     * Creates the About command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public About(UG ultimateGames) {
        super(ultimateGames, "", "Lists some information about ultimate games.", "/ug", new Permission("ultimategames.about", PermissionDefault.TRUE), false);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        sender.sendMessage(HEADER);
        sender.sendMessage(AUTHOR);
        sender.sendMessage(VERSION + ultimateGames.getDescription().getVersion());
        sender.sendMessage(COMMANDS);
    }
}
