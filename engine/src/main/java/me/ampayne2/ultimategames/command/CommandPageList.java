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
import me.ampayne2.ultimategames.message.PageList;
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
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
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