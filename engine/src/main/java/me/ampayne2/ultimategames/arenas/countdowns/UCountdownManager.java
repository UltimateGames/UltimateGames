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
package me.ampayne2.ultimategames.arenas.countdowns;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;

import java.util.HashMap;
import java.util.Map;

public class UCountdownManager implements CountdownManager {
    private final UltimateGames ultimateGames;
    private Map<Arena, StartingCountdown> starting = new HashMap<>();
    private Map<Arena, EndingCountdown> ending = new HashMap<>();

    /**
     * Creates a new Countdown Manager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     */
    public UCountdownManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean hasStartingCountdown(Arena arena) {
        return starting.containsKey(arena);
    }

    @Override
    public boolean hasEndingCountdown(Arena arena) {
        return ending.containsKey(arena);
    }

    /**
     * Gets an arena's starting countdown.
     *
     * @param arena The arena.
     * @return The arena's starting countdown.
     */
    public StartingCountdown getStartingCountdown(Arena arena) {
        return starting.get(arena);
    }

    @Override
    public EndingCountdown getEndingCountdown(Arena arena) {
        return ending.get(arena);
    }

    @Override
    public void createStartingCountdown(Arena arena, Integer seconds) {
        if (arena.getGame().getGamePlugin().isStartPossible(arena) && !starting.containsKey(arena)) {
            StartingCountdown countdown = new StartingCountdown(ultimateGames, arena, seconds);
            countdown.start();
            starting.put(arena, countdown);
            ultimateGames.getMessenger().debug("Created starting countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }

    @Override
    public void stopStartingCountdown(Arena arena) {
        if (starting.containsKey(arena)) {
            starting.get(arena).stop();
            starting.remove(arena);
            ultimateGames.getMessenger().debug("Stopped starting countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }

    @Override
    public void createEndingCountdown(Arena arena, Integer seconds, Boolean expDisplay) {
        if (!ending.containsKey(arena)) {
            EndingCountdown countdown = new EndingCountdown(ultimateGames, arena, seconds, expDisplay);
            countdown.start();
            ending.put(arena, countdown);
            ultimateGames.getMessenger().debug("Created ending countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }

    @Override
    public void stopEndingCountdown(Arena arena) {
        if (ending.containsKey(arena)) {
            ending.get(arena).stop();
            ending.remove(arena);
            ultimateGames.getMessenger().debug("Stopped ending countdown for arena " + arena.getName() + " of game " + arena.getGame().getName());
        }
    }
}
