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
package me.ampayne2.UltimateGames.Countdowns;

import java.util.HashMap;

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
		if (secondsLeft > 0 && initialSeconds == secondsLeft) {
			HashMap<String, String> replace = new HashMap<String, String>();
			replace.put("%Time%", Integer.toString(secondsLeft));
			ultimateGames.getMessageManager().broadcast(arena.getName(), arena.getGame().getGameDescription().getName(), replace, false, "countdowns.timeleftstart");
			arena.setStatus(ArenaStatus.STARTING);
			ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
		} else if (secondsLeft > 0 && secondsLeft <= 10) {
			HashMap<String, String> replace = new HashMap<String, String>();
			replace.put("%Time%", Integer.toString(secondsLeft));
			ultimateGames.getMessageManager().broadcast(arena.getName(), arena.getGame().getGameDescription().getName(), replace, false, "countdowns.timeleftstart");
		} else if (secondsLeft == 0) {
			ultimateGames.getMessageManager().broadcast(arena.getName(), arena.getGame().getGameDescription().getName(), false, "arenas.start");
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
