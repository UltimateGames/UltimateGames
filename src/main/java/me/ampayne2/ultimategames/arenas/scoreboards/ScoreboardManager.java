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
package me.ampayne2.ultimategames.arenas.scoreboards;

import me.ampayne2.ultimategames.arenas.Arena;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the arena scoreboards.
 */
public class ScoreboardManager {
    private final Map<Arena, ArenaScoreboard> scoreboards = new HashMap<Arena, ArenaScoreboard>();

    /**
     * Checks if an arena has an associated ArenaScoreboard.
     *
     * @param arena The arena.
     * @return True if the arena has a scoreboard, else false.
     */
    public boolean hasScoreboard(Arena arena) {
        return scoreboards.containsKey(arena);
    }

    /**
     * Gets the ArenaScoreboard of an arena.
     *
     * @param arena The arena.
     * @return The arena's ArenaScoreboards.
     */
    public ArenaScoreboard getScoreboard(Arena arena) {
        return scoreboards.get(arena);
    }

    /**
     * Creates an ArenaScoreboard for an arena.
     *
     * @param arena The arena.
     * @param name  Name of the ArenaScoreboard.
     * @return The ArenaScoreboard created.
     */
    public ArenaScoreboard createScoreboard(Arena arena, String name) {
        if (scoreboards.containsKey(arena)) {
            scoreboards.get(arena).reset();
        }
        ArenaScoreboard scoreboard = new ArenaScoreboard(name);
        scoreboards.put(arena, scoreboard);
        return scoreboard;
    }

    /**
     * Removes an Arena's ArenaScoreboard from the manager.
     *
     * @param arena The arena.
     */
    public void removeScoreboard(Arena arena) {
        if (scoreboards.containsKey(arena)) {
            scoreboards.get(arena).reset();
            scoreboards.remove(arena);
        }
    }
}
