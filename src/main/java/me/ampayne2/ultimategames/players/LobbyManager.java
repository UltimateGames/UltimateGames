/*
 * This file is part of UltimateGames.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.players;

import me.ampayne2.ultimategames.UltimateGames;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class LobbyManager {
    private UltimateGames ultimateGames;
    private Location lobby;

    public LobbyManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        FileConfiguration lobbyConfig = ultimateGames.getConfigManager().getLobbyConfig().getConfig();
        if (lobbyConfig.contains("world") && lobbyConfig.contains("x") && lobbyConfig.contains("y") && lobbyConfig.contains("z") && lobbyConfig.contains("pitch") && lobbyConfig.contains("yaw")) {
            String world = lobbyConfig.getString("world");
            Double x = lobbyConfig.getDouble("x");
            Double y = lobbyConfig.getDouble("y");
            Double z = lobbyConfig.getDouble("z");
            Float pitch = Float.valueOf(lobbyConfig.getString("pitch"));
            Float yaw = Float.valueOf(lobbyConfig.getString("yaw"));
            Location location = new Location(Bukkit.getWorld(world), x, y, z);
            location.setPitch(pitch);
            location.setYaw(yaw);
            this.lobby = location;
        }
    }

    public Location getLobby() {
        return this.lobby;
    }

    public void setLobby(Location location) {
        this.lobby = location;
        FileConfiguration lobbyConfig = ultimateGames.getConfigManager().getLobbyConfig().getConfig();
        lobbyConfig.set("world", location.getWorld().getName());
        lobbyConfig.set("x", location.getX());
        lobbyConfig.set("y", location.getY());
        lobbyConfig.set("z", location.getZ());
        lobbyConfig.set("pitch", location.getPitch());
        lobbyConfig.set("yaw", location.getYaw());
        ultimateGames.getConfigManager().getLobbyConfig().saveConfig();
    }
}
