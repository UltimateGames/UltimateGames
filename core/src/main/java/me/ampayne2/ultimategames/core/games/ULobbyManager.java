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
package me.ampayne2.ultimategames.core.games;

import me.ampayne2.ultimategames.api.config.ConfigType;
import me.ampayne2.ultimategames.api.games.LobbyManager;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Temporary ULobbyManager class.
 * TODO: Support for multiple lobbies per game.
 */
public class ULobbyManager implements LobbyManager {
    private final UG ultimateGames;
    private Location lobby;

    /**
     * Creates a new LobbyManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public ULobbyManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        FileConfiguration lobbyConfig = ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY);
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

    @Override
    public Location getLobby() {
        return lobby;
    }

    @Override
    public void setLobby(Location location) {
        lobby = location;
        FileConfiguration lobbyConfig = ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY);
        lobbyConfig.set("world", location.getWorld().getName());
        lobbyConfig.set("x", location.getX());
        lobbyConfig.set("y", location.getY());
        lobbyConfig.set("z", location.getZ());
        lobbyConfig.set("pitch", location.getPitch());
        lobbyConfig.set("yaw", location.getYaw());
        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.LOBBY).saveConfig();
    }
}
