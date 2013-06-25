package me.ampayne2.UltimateGames;

import org.bukkit.block.Sign;

import me.ampayne2.UltimateGames.Games.Arena;
import me.ampayne2.UltimateGames.Games.Game;

public class LobbySign {
	
	private Sign sign;
	private Game game;
	private Arena arena;
	
	public LobbySign(Sign sign, Game game, Arena arena) {
		this.sign = sign;
		this.game = game;
		this.arena = arena;
	}
	
	public Sign getSign() {
		return sign;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Arena getArena() {
		return arena;
	}
	
}
