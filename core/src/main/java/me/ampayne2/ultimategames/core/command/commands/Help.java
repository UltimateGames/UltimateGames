/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.command.commands;

import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.command.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that lists all of the ultimate games commands to the sender.
 */
public class Help extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the Help command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public Help(UG ultimateGames) {
        super(ultimateGames, "help", "Lists all of the ultimate games commands.", "/ug help [page]", new Permission("ultimategames.help", PermissionDefault.TRUE), 0, 1, false);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        int pageNumber = 1;
        if (args.length == 1) {
            try {
                pageNumber = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                ultimateGames.getMessenger().sendMessage(sender, "error.numberformat");
                return;
            }
        }
        ultimateGames.getCommandController().getPageList().sendPage(pageNumber, sender);
    }
}
