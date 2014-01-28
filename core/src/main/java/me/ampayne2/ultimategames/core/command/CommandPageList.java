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
package me.ampayne2.ultimategames.core.command;

import me.ampayne2.ultimategames.api.message.PageList;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * A PageList that lists all of the ultimate games commands and their description.
 */
public class CommandPageList extends PageList {

    /**
     * Creates the PageList of ultimate games commands.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public CommandPageList(UG ultimateGames, Command command) {
        super(ultimateGames, "Commands", 8);
        List<String> strings = new ArrayList<>();
        for (Command subCommand : command.getChildren(true)) {
            strings.add(ChatColor.AQUA + ((UGCommand) subCommand).getCommandUsage());
            strings.add(ChatColor.DARK_GRAY + "-" + ((UGCommand) subCommand).getDescription());
        }
        setStrings(strings);
    }
}