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
package me.ampayne2.ultimategames.countdowns;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.ArenaStatus;

/**
 * Manages different types of countdowns for arenas.
 */
public class CountdownManager implements Manager {

    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private List<Arena> starting = new ArrayList<Arena>();
    private List<Arena> ending = new ArrayList<Arena>();

    /**
     * Creates a new Countdown Manager.
     * @param ultimateGames A reference to the UltimateGames instance.
     */
    public CountdownManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean load() {
        loaded = true;
        return true;
    }

    @Override
    public boolean reload() {
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        for (Arena arena : starting) {
            arena.setStatus(ArenaStatus.OPEN);
        }
        starting.clear();
        ending.clear();
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Checks to see if an arena has a starting countdown running.
     * @param arena The arena.
     * @return If the arena has a starting countdown running.
     */
    public boolean isStartingCountdownEnabled(Arena arena) {
        return starting.contains(arena);
    }

    /**
     * Checks to see if an arena has an ending countdown running.
     * @param arena The arena.
     * @return If the arena has an ending countdown running.
     */
    public boolean isEndingCountdownEnabled(Arena arena) {
        return ending.contains(arena);
    }

    /**
     * Creates a starting countdown for an arena.
     * @param arena The arena.
     * @param seconds Initial seconds on the countdown.
     */
    public void createStartingCountdown(Arena arena, Integer seconds) {
        if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
            if (!starting.contains(arena)) {
                starting.add(arena);
            }
            new StartingCountdown(ultimateGames, arena, seconds, seconds).runTask(ultimateGames);
        }
    }

    /**
     * Stops a starting countdown for an arena.
     * @param arena The arena.
     */
    public void stopStartingCountdown(Arena arena) {
        if (starting.contains(arena)) {
            starting.remove(arena);
            arena.setStatus(ArenaStatus.OPEN);
        }
    }

    /**
     * Creates an ending countdown for an arena.
     * @param arena The arena.
     * @param seconds Initial seconds on the countdown.
     * @param expDisplay Should the exp bar be used to display the countdown?
     */
    public void createEndingCountdown(Arena arena, Integer seconds, Boolean expDisplay) {
        if (!ending.contains(arena)) {
            ending.add(arena);
        }
        new EndingCountdown(ultimateGames, arena, seconds, seconds, expDisplay).runTask(ultimateGames);
    }

    /**
     * Stops an ending countdown for an arena.
     * @param arena The arena.
     */
    public void stopEndingCountdown(Arena arena) {
        if (ending.contains(arena)) {
            ending.remove(arena);
        }
    }

}
