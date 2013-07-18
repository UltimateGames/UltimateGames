package me.ampayne2.UltimateGames.Players;

import me.ampayne2.UltimateGames.Arenas.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpawnPoint implements Listener{

	private Arena arena;
	private Location location;
	private Boolean locked;
	private String playerName;
	
	public SpawnPoint(Arena arena, Location location, Boolean locked) {
		this.arena = arena;
		this.location = location;
		this.locked = locked;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Boolean locked() {
		return locked;
	}
	
	public void lock(Boolean enabled) {
		this.locked = enabled;
		if (enabled == false) {
			this.playerName = null;
		}
	}
	
	public void teleportPlayer(String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		player.teleport(location);
		if (locked) {
			this.playerName = playerName;
		}
	}
	
	public String getPlayer() {
		return playerName;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (playerName != null && event.getPlayer().getName().equals(playerName)) {
			if ((event.getTo().getX()-1 >= location.getX() || event.getTo().getX()+1 <= location.getX()) && locked) {
				event.setCancelled(true);
				return;
			}
			if ((event.getTo().getY()-1 >= location.getY() || event.getTo().getY()+1 <= location.getY()) && locked) {
				event.setCancelled(true);
				return;
			}
			if ((event.getTo().getZ()-1 >= location.getZ() || event.getTo().getZ()+1 <= location.getZ()) && locked) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
}
