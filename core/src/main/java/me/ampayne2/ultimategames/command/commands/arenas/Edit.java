/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.command.commands.arenas;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.command.UGCommand;
import me.ampayne2.ultimategames.players.ArenaPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that sets the sender to arena editing mode.
 */
public class Edit extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the Edit command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public Edit(UG ultimateGames) {
        super(ultimateGames, "edit", "Sets you to arena editing mode.", "/ug arena edit", new Permission("ultimategames.arena.edit", PermissionDefault.OP), true);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String senderName = sender.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(senderName)) {
            ArenaPlayer player = ultimateGames.getPlayerManager().getArenaPlayer(senderName);
            if (player.isEditing()) {
                player.setEditing(false);
            } else {
                player.setEditing(true);
            }
        } else {
            ultimateGames.getMessenger().sendMessage(sender, "arenas.notinarena");
        }
    }
}
