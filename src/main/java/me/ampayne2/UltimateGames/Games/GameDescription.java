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
package me.ampayne2.UltimateGames.Games;

import me.ampayne2.UltimateGames.Enums.PlayerType;
import me.ampayne2.UltimateGames.Enums.ScoreType;

public class GameDescription {
	private String name, description, version, author, pack, scoreTypeName, secondaryScoreTypeName;
	private ScoreType scoreType, secondaryScoreType;
	private PlayerType playerType;

	/**
	 * Creates a new Game Description
	 * @param name
	 * @param description
	 * @param version
	 * @param author
	 * @param pack
	 * @param scoreTypeName
	 * @param secondaryScoreTypeName
	 * @param scoreType
	 * @param secondaryScoreType
	 * @param playerType
	 */
	public GameDescription(String name, String description, String version, String author, String pack, String scoreTypeName, String secondaryScoreTypeName, ScoreType scoreType, ScoreType secondaryScoreType, PlayerType playerType) {
		this.name = name;
		this.description = description;
		this.version = version;
		this.author = author;
		this.pack = pack;
		this.scoreTypeName = scoreTypeName;
		this.secondaryScoreTypeName = secondaryScoreTypeName;
		this.scoreType = scoreType;
		this.secondaryScoreType = secondaryScoreType;
		this.playerType = playerType;
	}
	
	/**
	 * Gets the name of the game
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the game
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the version string of the game
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the author of the game
	 * 
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets the main class of the game
	 * 
	 * @return the main class
	 */
	public String getPackage() {
		return pack;
	}

	/**
	 * Gets the main ScoreType of the game
	 * 
	 * @return
	 */
	public ScoreType getScoreType() {
		return scoreType;
	}

	/**
	 * Gets the secondary ScoreType of the game
	 * 
	 * @return
	 */
	public ScoreType getSecondaryScoreType() {
		return secondaryScoreType;
	}

	/**
	 * Gets the name of the main ScoreType of the game
	 * 
	 * @return
	 */
	public String getScoreTypeName() {
		return scoreTypeName;
	}

	/**
	 * Gets the name of the secondary ScoreType of the game
	 * 
	 * @return
	 */
	public String getSecondaryScoreTypeName() {
		return secondaryScoreTypeName;
	}

	/**
	 * Gets the PlayerType of the game
	 * 
	 * @return
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}
}
