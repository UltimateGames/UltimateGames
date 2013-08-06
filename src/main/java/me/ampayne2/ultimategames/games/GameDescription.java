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

import java.util.List;

import me.ampayne2.ultimategames.enums.PlayerType;
import me.ampayne2.ultimategames.enums.ScoreType;

public class GameDescription {
    private String name, description, version, author, scoreTypeName, secondaryScoreTypeName;
    private ScoreType scoreType, secondaryScoreType;
    private PlayerType playerType;
    private List<String> instructionPages;

    /**
     * Creates a new Game Description.
     * @param name
     * @param description
     * @param version
     * @param author
     * @param scoreTypeName
     * @param secondaryScoreTypeName
     * @param scoreType
     * @param secondaryScoreType
     * @param playerType
     * @param instructionPages
     */
    public GameDescription(String name, String description, String version, String author, String scoreTypeName, String secondaryScoreTypeName, ScoreType scoreType, ScoreType secondaryScoreType, PlayerType playerType, List<String> instructionPages) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.scoreTypeName = scoreTypeName;
        this.secondaryScoreTypeName = secondaryScoreTypeName;
        this.scoreType = scoreType;
        this.secondaryScoreType = secondaryScoreType;
        this.playerType = playerType;
        this.instructionPages = instructionPages;
    }

    /**
     * Gets the name of the game.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the game.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the version string of the game.
     * @return The version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the author of the game.
     * @return The author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the main ScoreType of the game.
     * @return The main score type.
     */
    public ScoreType getScoreType() {
        return scoreType;
    }

    /**
     * Gets the secondary ScoreType of the game.
     * @return The secondary score type.
     */
    public ScoreType getSecondaryScoreType() {
        return secondaryScoreType;
    }

    /**
     * Gets the name of the main ScoreType of the game.
     * @return The name of the main score type.
     */
    public String getScoreTypeName() {
        return scoreTypeName;
    }

    /**
     * Gets the name of the secondary ScoreType of the game.
     * @return The name of the secondary score type.
     */
    public String getSecondaryScoreTypeName() {
        return secondaryScoreTypeName;
    }

    /**
     * Gets the PlayerType of the game.
     * @return The player type.
     */
    public PlayerType getPlayerType() {
        return playerType;
    }

    /**
     * Gets the instruction pages of the game.
     * @return The instruction pages.
     */
    public List<String> getInstructionPages() {
        return instructionPages;
    }
}
