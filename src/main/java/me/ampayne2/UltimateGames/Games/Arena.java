package me.ampayne2.UltimateGames.Games;

import java.util.ArrayList;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;

public class Arena {
	private String arenaName;
	private Game game;
	private ArrayList<String> players;
	private Integer maxPlayers;
	private ArenaStatus arenaStatus;

	public Arena(Game game, String arenaName, Integer maxPlayers) {
		this.arenaName = arenaName;
		this.game = game;
		this.players = new ArrayList<String>();
		this.maxPlayers = maxPlayers;
	}
	
	public String getName() {
		return arenaName;
	}
	
	public Game getGame() {
		return game;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}
	
	public Integer getMaxPlayers() {
		return maxPlayers;
	}

	public ArenaStatus getStatus() {
		return arenaStatus;
	}

	public void setStatus(ArenaStatus status) {
		arenaStatus = status;
	}

}
