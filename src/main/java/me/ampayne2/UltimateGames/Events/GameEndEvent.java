package me.ampayne2.UltimateGames.Events;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.UltimateGames.Enums.EndType;
import me.ampayne2.UltimateGames.Games.GameDescription;
import me.ampayne2.UltimateGames.Games.GameScoreContainer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	private List<String> players = new ArrayList<String>();
	private GameDescription gameDescription;
	private GameScoreContainer scoreContainer;
	private EndType endType;
	
	public GameEndEvent(GameDescription gameDescription, List<String> players, GameScoreContainer scoreContainer, EndType endType) {
		this.gameDescription = gameDescription;
		this.players = players;
		this.scoreContainer = scoreContainer;
		this.endType = endType;
	}
	
	public GameDescription getGameDescription() {
		return gameDescription;
	}
	
	public List<String> getPlayers() {
		return players;
	}
	
	public GameScoreContainer getScoreContainer() {
		return scoreContainer;
	}
	
	public EndType getEndType() {
		return endType;
	}

	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
