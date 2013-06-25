package me.ampayne2.UltimateGames.Events;

import me.ampayne2.UltimateGames.Games.Arena;
import me.ampayne2.UltimateGames.Games.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameJoinEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private Game game;
	private Arena arena;
	
	public GameJoinEvent(Player player, Game game, Arena arena) {
		this.player = player;
		this.game = game;
		this.arena = arena;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Game getGame() {
		return game;
	}
	
	public Arena getArena() {
		return arena;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
