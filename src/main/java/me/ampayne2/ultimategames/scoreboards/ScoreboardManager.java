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
package me.ampayne2.ultimategames.scoreboards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ampayne2.ultimategames.arenas.Arena;

public class ScoreboardManager {
    private Map<Arena, List<ArenaScoreboard>> scoreboards = new HashMap<Arena, List<ArenaScoreboard>>();

    /**
     * Gets the ArenaScoreboards of an arena.
     * @param arena The arena.
     * @return The arena's ArenaScoreboards.
     */
    public List<ArenaScoreboard> getArenaScoreboards(Arena arena) {
        return scoreboards.containsKey(arena) ? scoreboards.get(arena) : new ArrayList<ArenaScoreboard>();
    }

    /**
     * Creates an ArenaScoreboard for an arena.
     * @param arena The arena.
     * @param name Name of the ArenaScoreboard.
     * @return The ArenaScoreboard created.
     */
    public ArenaScoreboard createArenaScoreboard(Arena arena, String name) {
        ArenaScoreboard scoreboard = new ArenaScoreboard(name);
        if (scoreboards.containsKey(arena)) {
            scoreboards.get(arena).add(scoreboard);
        } else {
            ArrayList<ArenaScoreboard> newScoreboards = new ArrayList<ArenaScoreboard>();
            newScoreboards.add(scoreboard);
            scoreboards.put(arena, newScoreboards);
        }
        return scoreboard;
    }

    /**
     * Removes an ArenaScoreboard from the manager.
     * @param arena The arena.
     * @param name The name of the scoreboard.
     */
    public void removeArenaScoreboard(Arena arena, String name) {
        if (scoreboards.containsKey(arena)) {
            for (ArenaScoreboard scoreboard : new ArrayList<ArenaScoreboard>(scoreboards.get(arena))) {
                if (name.equals(scoreboard.getName())) {
                    scoreboard.reset();
                    scoreboards.get(arena).remove(scoreboard);
                }
            }
        }
    }
}
