/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.games;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.GamePlugin;
import me.ampayne2.ultimategames.api.games.PlayerType;

import java.util.List;

public class UGame implements Game {
    private final GamePlugin gamePlugin;
    private final String name, description, version, author;
    private final PlayerType playerType;
    private final List<String> instructionPages;
    private boolean enabled = true;

    public UGame(GamePlugin gamePlugin, String name, String description, String version, String author, PlayerType playerType, List<String> instructionPages) {
        this.gamePlugin = gamePlugin;
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.playerType = playerType;
        this.instructionPages = instructionPages;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public GamePlugin getGamePlugin() {
        return gamePlugin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public PlayerType getPlayerType() {
        return playerType;
    }

    @Override
    public List<String> getInstructionPages() {
        return instructionPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UGame game = (UGame) o;

        return name.equalsIgnoreCase(game.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
