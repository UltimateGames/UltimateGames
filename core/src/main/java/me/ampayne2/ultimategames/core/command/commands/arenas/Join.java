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

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.arenas.QueueManager;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.command.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that adds the sender to an arena.
 */
public class Join extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the Join command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public Join(UG ultimateGames) {
        super(ultimateGames, "join", "Joins an arena", "/ug arena join <arena> <game>", new Permission("ultimategames.arena.join", PermissionDefault.TRUE), 2, true);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String arenaName = args[0];
        String gameName = args[1];
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.GAME_DOESNTEXIST);
            return;
        } else if (!ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ARENA_DOESNTEXIST);
            return;
        }
        Arena arena = ultimateGames.getArenaManager().getArena(arenaName, gameName);
        Player player = (Player) sender;
        String playerName = player.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ARENA_ALREADYIN);
            return;
        } else if (ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ARENA_ALREADYSPECTATING);
            return;
        }
        ArenaStatus arenaStatus = arena.getStatus();
        if (arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING) {
            ultimateGames.getPlayerManager().addPlayerToArena(player, arena, true);
        } else if (arenaStatus == ArenaStatus.RUNNING || arenaStatus == ArenaStatus.ENDING || arena.getPlayers().size() >= arena.getMaxPlayers()) {
            QueueManager queue = ultimateGames.getQueueManager();
            if (!queue.isPlayerInQueue(playerName, arena)) {
                queue.addPlayerToQueue(player, arena);
            }
        } else {
            ultimateGames.getMessenger().sendMessage(sender, UGMessage.ARENA_NOTLOADED);
        }
    }
}
