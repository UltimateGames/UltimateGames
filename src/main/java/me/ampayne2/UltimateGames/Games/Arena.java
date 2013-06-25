package me.ampayne2.UltimateGames.Games;

import java.util.List;

import me.ampayne2.UltimateGames.Enums.ArenaStatus;

public class Arena {
	private List<String> players;
	private ArenaStatus arenaStatus;

	public Arena(Game game) {

	}

	public List<String> getPlayers() {
		return players;
	}

	public ArenaStatus getStatus() {
		return arenaStatus;
	}

	public void setStatus(ArenaStatus status) {
		arenaStatus = status;
	}

}
