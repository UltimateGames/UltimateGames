package me.ampayne2.UltimateGames.Games;

import java.util.List;

import me.ampayne2.UltimateGames.Enums.ArenaStatus;

public class Arena {
	private String arenaName;
	private Game game;
	private List<String> players;
	private ArenaStatus arenaStatus;

	public Arena(Game game, String arenaName) {
		this.arenaName = arenaName;
		this.game = game;
	}
	
	public String getName() {
		return arenaName;
	}
	
	public Game getGame() {
		return game;
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
