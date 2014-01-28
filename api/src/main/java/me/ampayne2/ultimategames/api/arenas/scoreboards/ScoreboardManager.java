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
package me.ampayne2.ultimategames.api.arenas.scoreboards;

import me.ampayne2.ultimategames.api.arenas.Arena;

/**
 * Manages arena scoreboards.
 */
public interface ScoreboardManager {

    /**
     * Checks if an arena has an associated ArenaScoreboard.
     *
     * @param arena The arena.
     * @return True if the arena has a scoreboard, else false.
     */
    boolean hasScoreboard(Arena arena);

    /**
     * Gets the ArenaScoreboard of an arena.
     *
     * @param arena The arena.
     * @return The arena's ArenaScoreboards.
     */
    Scoreboard getScoreboard(Arena arena);

    /**
     * Creates an ArenaScoreboard for an arena.
     *
     * @param arena The arena.
     * @param name  Name of the ArenaScoreboard.
     * @return The ArenaScoreboard created.
     */
    Scoreboard createScoreboard(Arena arena, String name);

    /**
     * Removes an Arena's ArenaScoreboard from the manager.
     *
     * @param arena The arena.
     */
    void removeScoreboard(Arena arena);
}
