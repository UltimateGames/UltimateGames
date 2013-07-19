package me.ampayne2.UltimateGames.Countdowns;

import org.bukkit.scheduler.BukkitRunnable;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;

public class StartingCountdown extends BukkitRunnable{
	
	private UltimateGames ultimateGames;
	private Arena arena;
	private Integer initialSeconds;
	private Integer secondsLeft;
	
	public StartingCountdown(UltimateGames ultimateGames, Arena arena, Integer initialSeconds, Integer secondsLeft) {
		this.ultimateGames = ultimateGames;
		this.arena = arena;
		this.initialSeconds = initialSeconds;
		this.secondsLeft = secondsLeft;
	}

	@Override
	public void run() {
		if (initialSeconds == secondsLeft) {
			// send countdown start message
			arena.setStatus(ArenaStatus.STARTING);
			ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
		} else if (secondsLeft <= 10) {
			// send last 10 seconds message
		} else if (secondsLeft == 0) {
			// send game start message
			arena.setStatus(ArenaStatus.RUNNING);
			ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
			arena.getGame().getGamePlugin().beginArena(arena);
			ultimateGames.getCountdownManager().stopStartingCountdown(arena);
		}
		if (ultimateGames.getCountdownManager().isStartingCountdownEnabled(arena)) {
			new StartingCountdown(ultimateGames, arena, initialSeconds, secondsLeft-1).runTaskLater(ultimateGames, 20L);
		}
	}

}
