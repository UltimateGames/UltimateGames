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

import me.ampayne2.ultimategames.api.arenas.ArenaManager;
import me.ampayne2.ultimategames.api.message.Messenger;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.command.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that sets the spectator spawnpoint of an arena.
 */
public class SetSpectatorSpawn extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the SetSpectatorSpawn command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public SetSpectatorSpawn(UG ultimateGames) {
        super(ultimateGames, "setspectatorspawn", "Sets the spectator spawn of an arena.", "/ug arena setspectatorspawn <arena> <game>", new Permission("ultimategames.arena.setspectatorspawn", PermissionDefault.OP), 2, true);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String arenaName = args[0];
        String gameName = args[1];
        Messenger messageManager = ultimateGames.getMessenger();
        ArenaManager arenaManager = ultimateGames.getArenaManager();
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            messageManager.sendMessage(sender, "games.doesntexist");
            return;
        } else if (!arenaManager.arenaExists(arenaName, gameName)) {
            messageManager.sendMessage(sender, "arenas.doesntexist");
            return;
        }
        ultimateGames.getSpawnpointManager().setSpectatorSpawnPoint(arenaManager.getArena(arenaName, gameName), ((Player) sender).getLocation());
        messageManager.sendMessage(sender, "spawnpoints.setspectatorspawnpoint", arenaName, gameName);
    }
}
