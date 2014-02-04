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

import me.ampayne2.ultimategames.api.arenas.zones.RadiusType;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.arenas.zones.UZone;
import me.ampayne2.ultimategames.core.command.UGCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * A command that adds a Zone to an arena.
 */
public class AddZone extends UGCommand {
    private final UG ultimateGames;

    /**
     * Creates the AddZone command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public AddZone(UG ultimateGames) {
        super(ultimateGames, "addzone", "Adds a zone to an arena.", "/ug arena addzone <arena> <game> <name> <radius> <cube/circle>", new Permission("ultimategames.arena.addzone", PermissionDefault.OP), 5, true);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        if (!(args[4].equalsIgnoreCase("cube") || args[4].equalsIgnoreCase("circle"))) {
            ultimateGames.getMessenger().sendMessage(sender, "error.radiustypeformat");
            return;
        }
        String arenaName = args[0];
        String gameName = args[1];
        int radius = 0;
        try {
            radius = Integer.valueOf(args[3]);
        } catch (NumberFormatException e) {
            ultimateGames.getMessenger().sendMessage(sender, "error.numberformat");
        }
        RadiusType radiusType = RadiusType.valueOf(args[4].toUpperCase());
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, "games.doesntexist");
            return;
        } else if (!ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, "arenas.doesntexist");
            return;
        }
        ultimateGames.getZoneManager().addZone(new UZone(ultimateGames, ultimateGames.getArenaManager().getArena(arenaName, gameName), args[2], ((Player) sender).getLocation(), radius, radiusType));
        ultimateGames.getMessenger().sendMessage(sender, "zones.create", arenaName, gameName);
    }
}
