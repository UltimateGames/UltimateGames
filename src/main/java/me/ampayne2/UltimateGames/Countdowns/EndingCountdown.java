package me.ampayne2.UltimateGames.Countdowns;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndingCountdown extends BukkitRunnable{
	
	private UltimateGames ultimateGames;
	private Arena arena;
	private Integer initialSeconds;
	private Integer secondsLeft;
	private Boolean expDisplay;
	
	public EndingCountdown(UltimateGames ultimateGames, Arena arena, Integer initialSeconds, Integer secondsLeft, Boolean expDisplay) {
		this.ultimateGames = ultimateGames;
		this.arena = arena;
		this.initialSeconds = initialSeconds;
		this.secondsLeft = secondsLeft;
		this.expDisplay = expDisplay;
	}

	@Override
	public void run() {
		if (expDisplay) {
			for (String playerName : arena.getPlayers()) {
				Player player = Bukkit.getPlayer(playerName);
				player.setLevel(secondsLeft);
			}
		}
		if (initialSeconds == secondsLeft) {
			arena.setStatus(ArenaStatus.STARTING);
			ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
		} else if (secondsLeft <= 10) {
			// send last 10 seconds message
		} else if (secondsLeft == 0) {
			// send game end message
			arena.setStatus(ArenaStatus.ENDING);
			ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
			arena.getGame().getGamePlugin().endArena(arena);
			ultimateGames.getCountdownManager().stopEndingCountdown(arena);
		}
		if (ultimateGames.getCountdownManager().isEndingCountdownEnabled(arena)) {
			new EndingCountdown(ultimateGames, arena, initialSeconds, secondsLeft-1, expDisplay).runTaskLater(ultimateGames, 20L);
		}
	}

}
