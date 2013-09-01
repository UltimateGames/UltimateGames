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

import me.ampayne2.ultimategames.api.GamePlugin;
import me.ampayne2.ultimategames.enums.PlayerType;
import me.ampayne2.ultimategames.enums.ScoreType;

public class Game {
    
    private boolean enabled = true;
    private GamePlugin gamePlugin;
    private String name, description, version, author, scoreTypeName, secondaryScoreTypeName;
    private ScoreType scoreType, secondaryScoreType;
    private PlayerType playerType;
    private List<String> instructionPages;

    public Game(GamePlugin gamePlugin) {
        this.gamePlugin = gamePlugin;
    }
    
    /**
     * Checks to see if a game is enabled.
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
    
    /**
     * Sets the name of the game.
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the game.
     * @param description the description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the version string of the game.
     * @param version the version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Sets the author of the game.
     * @param author the author.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the main ScoreType of the game.
     * @param scoreType the main ScoreType of the game.
     */
    public void setScoreType(ScoreType scoreType) {
        this.scoreType = scoreType;
    }

    /**
     * Gets the secondary ScoreType of the game.
     * @param secondaryScoreType the secondary ScoreType of the game.
     */
    public void setSecondaryScoreType(ScoreType secondaryScoreType) {
        this.secondaryScoreType = secondaryScoreType;
    }

    /**
     * Sets the name of the main ScoreType of the game.
     * @param scoreTypeName the main ScoreType's name.
     */
    public void setScoreTypeName(String scoreTypeName) {
        this.scoreTypeName = scoreTypeName;
    }

    /**
     * Sets the name of the secondary ScoreType of the game.
     * @param secondaryScoreTypeName the secondary ScoreType's name.
     */
    public void setSecondaryScoreTypeName(String secondaryScoreTypeName) {
        this.secondaryScoreTypeName = secondaryScoreTypeName;
    }

    /**
     * Sets the PlayerType of the game.
     * @param playerType the PlayerType.
     */
    public void setPlayerType(PlayerType playerType) {
        this.playerType = playerType;
    }

    /**
     * Sets the instruction pages of the game.
     * @param instructionPages the instruction pages.
     */
    public void setInstructionPages(List<String> instructionPages) {
        this.instructionPages = instructionPages;
    }
}
