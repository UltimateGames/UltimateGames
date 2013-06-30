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
package me.ampayne2.UltimateGames.Arenas;

import java.util.ArrayList;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Games.Game;

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
