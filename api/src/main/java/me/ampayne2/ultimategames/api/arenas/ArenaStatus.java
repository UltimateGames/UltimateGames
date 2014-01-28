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

import org.bukkit.ChatColor;

/**
 * An enum of the possible statuses of an arena.
 */
public enum ArenaStatus {
    OPEN(ChatColor.GREEN, "Open"),
    STARTING(ChatColor.DARK_GRAY, "Starting"),
    RUNNING(ChatColor.DARK_GRAY, "Running"),
    ENDING(ChatColor.DARK_GRAY, "Ending"),
    ARENA_STOPPED(ChatColor.BLACK, "Stopped"),
    GAME_STOPPED(ChatColor.BLACK, "Stopped");

    private final ChatColor color;
    private final String displayName;

    private ArenaStatus(ChatColor color, String displayName) {
        this.color = color;
        this.displayName = displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }
}
