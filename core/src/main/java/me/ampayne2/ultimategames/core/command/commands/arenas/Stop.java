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

import me.ampayne2.ultimategames.api.message.Messenger;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.command.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that stops an arena.
 */
public class Stop extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the Stop command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public Stop(UG ultimateGames) {
        super(ultimateGames, "stop", "Stops an arena", "/ug arena stop <arena> <game>", new Permission("ultimategames.arena.stop", PermissionDefault.OP), 2, false);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String arenaName = args[0];
        String gameName = args[1];
        Messenger messenger = ultimateGames.getMessenger();
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            messenger.sendMessage(sender, UGMessage.GAME_DOESNTEXIST);
            return;
        } else if (!ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            messenger.sendMessage(sender, UGMessage.ARENA_DOESNTEXIST);
            return;
        }
        ultimateGames.getArenaManager().stopArena(ultimateGames.getArenaManager().getArena(arenaName, gameName));
        messenger.sendMessage(sender, UGMessage.ARENA_FORCESTOP, arenaName, gameName);
    }
}
