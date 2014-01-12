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
package me.ampayne2.ultimategames.players.streaks;

import me.ampayne2.ultimategames.players.ArenaPlayer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple class that keeps track of a streak.
 */
public class Streak {
    private final ArenaPlayer player;
    private final Set<StreakAction> actions;
    private int count = 0;

    /**
     * Creates a new Streak.
     *
     * @param player  The player of the streak.
     * @param actions The actions to as the streak increases.
     */
    public Streak(ArenaPlayer player, StreakAction... actions) {
        this.player = player;
        this.actions = new HashSet<StreakAction>(Arrays.asList(actions));
    }

    /**
     * Gets the player of the streak.
     *
     * @return The streak's ArenaPlayer.
     */
    public ArenaPlayer getPlayer() {
        return player;
    }

    /**
     * Gets the actions of the streak.
     *
     * @return The streak's StreakActions.
     */
    public Set<StreakAction> getActions() {
        return actions;
    }

    /**
     * Gets the current streak amount.
     *
     * @return The current streak amount.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increases the streak by one.
     */
    public void increaseCount() {
        count++;
        triggerActions();
    }

    /**
     * Increases the count by an amount.
     *
     * @param amount The amount.
     */
    public void increaseCount(int amount) {
        count += amount;
        triggerActions();
    }

    /**
     * Resets the streak.
     */
    public void reset() {
        count = 0;
        for (StreakAction action : actions) {
            action.setTriggered(false);
        }
    }

    /**
     * Performs any streak actions that meet the requirements.
     */
    private void triggerActions() {
        for (StreakAction action : actions) {
            if (count >= action.getRequiredCount() && !action.hasBeenTriggered()) {
                action.perform(player);
                action.setTriggered(true);
            }
        }
    }
}
