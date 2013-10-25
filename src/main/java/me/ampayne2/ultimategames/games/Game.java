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
package me.ampayne2.ultimategames.games;

import me.ampayne2.ultimategames.api.GamePlugin;
import me.ampayne2.ultimategames.enums.PlayerType;

import java.util.List;

public class Game {
	private final GamePlugin gamePlugin;
	private final String name, description, version, author;
	private final PlayerType playerType;
	private final List<String> instructionPages;
	private boolean enabled = true;

	public Game(GamePlugin gamePlugin, String name, String description, String version, String author, PlayerType playerType, List<String> instructionPages) {
		this.gamePlugin = gamePlugin;
		this.name = name;
		this.description = description;
		this.version = version;
		this.author = author;
		this.playerType = playerType;
		this.instructionPages = instructionPages;
	}

	/**
	 * Checks to see if a game is enabled.
	 *
	 * @return True if the game is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Disables a game.
	 */
	public void disable() {
		enabled = false;
	}

	public GamePlugin getGamePlugin() {
		return gamePlugin;
	}

	/**
	 * Gets the name of the game.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the game.
	 *
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the version string of the game.
	 *
	 * @return The version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the author of the game.
	 *
	 * @return The author.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the PlayerType of the game.
	 *
	 * @return The player type.
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	/**
	 * Gets the instruction pages of the game.
	 *
	 * @return The instruction pages.
	 */
	public List<String> getInstructionPages() {
		return instructionPages;
	}
}
