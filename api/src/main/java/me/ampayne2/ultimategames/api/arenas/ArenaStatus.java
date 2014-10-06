/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.api.arenas;

import net.canarymod.chat.TextFormat;

/**
 * An enum of the possible statuses of an arena.
 */
public enum ArenaStatus {
    OPEN(TextFormat.GREEN, "Open"),
    STARTING(TextFormat.GRAY, "Starting"),
    RUNNING(TextFormat.GRAY, "Running"),
    ENDING(TextFormat.GRAY, "Ending"),
    ARENA_STOPPED(TextFormat.BLACK, "Stopped"),
    GAME_STOPPED(TextFormat.BLACK, "Stopped");

    private final String color;
    private final String displayName;

    private ArenaStatus(String color, String displayName) {
        this.color = color;
        this.displayName = displayName;
    }

    /**
     * Gets the display color of the ArenaStatus.
     *
     * @return The color in a string
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the display name of the ArenaStatus.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }
}
