/*
 * This file is part of UltimateGames ENGINE.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames ENGINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames ENGINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames ENGINE.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.arenas.scoreboards;

import me.ampayne2.ultimategames.arenas.Arena;

import java.util.HashMap;
import java.util.Map;

public class UScoreboardManager implements ScoreboardManager {
    private final Map<Arena, Scoreboard> scoreboards = new HashMap<>();

    @Override
    public boolean hasScoreboard(Arena arena) {
        return scoreboards.containsKey(arena);
    }

    @Override
    public Scoreboard getScoreboard(Arena arena) {
        return scoreboards.get(arena);
    }

    @Override
    public Scoreboard createScoreboard(Arena arena, String name) {
        if (scoreboards.containsKey(arena)) {
            scoreboards.get(arena).reset();
        }
        Scoreboard scoreboard = new UScoreboard(name);
        scoreboards.put(arena, scoreboard);
        return scoreboard;
    }

    @Override
    public void removeScoreboard(Arena arena) {
        if (scoreboards.containsKey(arena)) {
            scoreboards.get(arena).reset();
            scoreboards.remove(arena);
        }
    }
}
