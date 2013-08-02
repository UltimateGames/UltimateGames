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
package me.ampayne2.ultimategames.api;

import java.util.ArrayList;
import java.util.HashMap;

import me.ampayne2.ultimategames.arenas.Arena;

public class ScoreboardManager {
    private HashMap<Arena, ArrayList<ArenaScoreboard>> scoreboards = new HashMap<Arena, ArrayList<ArenaScoreboard>>();

    public ArrayList<ArenaScoreboard> getArenaScoreboards(Arena arena) {
        if (scoreboards.containsKey(arena)) {
            return scoreboards.get(arena);
        } else {
            return new ArrayList<ArenaScoreboard>();
        }
    }

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
