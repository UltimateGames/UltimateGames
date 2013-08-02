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
package me.ampayne2.ultimategames.events;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.ultimategames.enums.EndType;
import me.ampayne2.ultimategames.games.GameDescription;
import me.ampayne2.ultimategames.games.GameScoreContainer;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {
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
