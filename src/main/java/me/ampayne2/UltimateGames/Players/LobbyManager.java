package me.ampayne2.UltimateGames.Players;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import me.ampayne2.UltimateGames.UltimateGames;

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
