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
package me.ampayne2.ultimategames.core.command.commands.arenas;

import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.command.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that adds a spawn to an arena.
 */
public class AddSpawn extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the AddSpawn command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public AddSpawn(UG ultimateGames) {
        super(ultimateGames, "addspawn", "Adds a spawn to an arena.", "/ug arena addspawn <arena> <game> <locked (true/false)>", new Permission("ultimategames.arena.addspawn", PermissionDefault.OP), 3, true);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        if (!(args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false"))) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ERROR_BOOLEANFORMAT);
            return;
        }
        String arenaName = args[0];
        String gameName = args[1];
        boolean locked = Boolean.valueOf(args[2].toLowerCase());
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.GAME_DOESNTEXIST);
            return;
        } else if (!ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ARENA_DOESNTEXIST);
            return;
        }
        ultimateGames.getSpawnpointManager().createSpawnPoint(ultimateGames.getArenaManager().getArena(arenaName, gameName), ((Player) sender).getLocation(), locked);
        ultimateGames.getMessenger().sendMessage(sender, UGMessage.SPAWNPOINT_SET, arenaName, gameName);
    }
}
