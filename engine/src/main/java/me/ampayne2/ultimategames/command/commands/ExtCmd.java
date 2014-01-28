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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that allows you to bypass the arena command blocker.
 */
public class ExtCmd extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the ExtCmd command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public ExtCmd(UG ultimateGames) {
        super(ultimateGames, "cmd", "Allows you to use a non-ug command inside an arena.", "/ug cmd [command]", new Permission("ultimategames.extcmd", PermissionDefault.OP), true);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String playerName = player.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            StringBuilder extCommand = new StringBuilder(args[0]);
            if (args.length != 1) {
                for (int i = 1; i < args.length; i++) {
                    extCommand.append(" ");
                    extCommand.append(args[i]);
                }
            }
            ultimateGames.getCommandController().addBlockBypasser(playerName);
            player.performCommand(extCommand.toString());
            ultimateGames.getCommandController().removeBlockBypasser(playerName);
        }
    }
}
