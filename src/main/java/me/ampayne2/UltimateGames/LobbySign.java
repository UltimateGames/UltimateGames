package me.ampayne2.UltimateGames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Games.Arena;

public class LobbySign {
	
	private Sign sign;
	private Arena arena;
	
	public LobbySign(Sign sign, Arena arena) {
		this.sign = sign;
		this.arena = arena;
	}
	
	public Sign getSign() {
		return sign;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public void updateSign() {
		Bukkit.getServer().broadcastMessage("updating");
		ArenaStatus arenaStatus = arena.getStatus();
		if (arenaStatus == ArenaStatus.ARENA_STOPPED || arenaStatus == ArenaStatus.GAME_STOPPED) {
			sign.setLine(0, ChatColor.GRAY + arenaStatus.toString());
		} else if (arenaStatus == ArenaStatus.RESET_FAILED) {
			sign.setLine(0, ChatColor.DARK_RED + arenaStatus.toString());
		} else {
			sign.setLine(0, ChatColor.GREEN + arenaStatus.toString());
		}
		sign.setLine(1, ChatColor.BLUE + "Game Name");
		sign.setLine(2, ChatColor.BLUE + arena.getName());
		sign.setLine(3, ChatColor.BLUE + "min / max");
		sign.update();
		Bukkit.getServer().broadcastMessage("updated");
	}
	
}
