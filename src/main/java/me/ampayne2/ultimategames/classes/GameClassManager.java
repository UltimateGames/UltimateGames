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
package me.ampayne2.ultimategames.classes;

import me.ampayne2.ultimategames.games.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages player classes for games.
 */
public class GameClassManager {
	private Map<Game, Set<GameClass>> gameClasses = new HashMap<Game, Set<GameClass>>();

	/**
	 * Checks if a game has a certain GameClass.
	 *
	 * @param game      The game.
	 * @param gameClass The GameClass.
	 *
	 * @return True if the game has the GameClass, else false.
	 */
	public boolean hasGameClass(Game game, GameClass gameClass) {
		return gameClasses.containsKey(game) && gameClasses.get(game).contains(gameClass);
	}

	/**
	 * Gets the classes of a game.
	 *
	 * @param game The game.
	 *
	 * @return The classes of a game.
	 */
	public Set<GameClass> getGameClasses(Game game) {
		return gameClasses.containsKey(game) ? new HashSet<GameClass>(gameClasses.get(game)) : new HashSet<GameClass>();
	}

	/**
	 * Adds a GameClass to the list of game classes.
	 *
	 * @param gameClass The GameClass.
	 *
	 * @return True if the GameClass was added successfully, else false.
	 */
	public boolean addGameClass(GameClass gameClass) {
		Game game = gameClass.getGame();
		Set<GameClass> classes = gameClasses.containsKey(game) ? gameClasses.get(game) : new HashSet<GameClass>();
		if (classes.add(gameClass)) {
			gameClasses.put(game, classes);
			return true;
		}
		return false;
	}

	/**
	 * Removes a GameClass from the list of game classes.
	 *
	 * @param gameClass The GameClass.
	 */
	public void removeGameClass(GameClass gameClass) {
		gameClass.removePlayersFromClass();
		Game game = gameClass.getGame();
		if (gameClasses.containsKey(game)) {
			gameClasses.get(game).remove(gameClass);
		}
	}

	/**
	 * Gets a player's GameClass.
	 *
	 * @param game       The game of the player.
	 * @param playerName The player's name.
	 *
	 * @return The GameClass, null if the player isn't in one.
	 */
	public GameClass getPlayerClass(Game game, String playerName) {
		for (GameClass gameClass : getGameClasses(game)) {
			if (gameClass.hasPlayer(playerName)) {
				return gameClass;
			}
		}
		return null;
	}
}
