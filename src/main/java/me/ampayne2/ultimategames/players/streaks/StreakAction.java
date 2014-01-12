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

/**
 * Performs an action when a streak reaches the required count.
 */
public abstract class StreakAction {
    private final int requiredCount;
    private boolean triggered = false;

    /**
     * Creates a new StreakAction.
     *
     * @param requiredCount The required count to trigger the Streak Action.
     */
    public StreakAction(int requiredCount) {
        this.requiredCount = requiredCount;
    }

    /**
     * Gets the count required for this StreakAction to be performed.
     *
     * @return The count.
     */
    public int getRequiredCount() {
        return requiredCount;
    }

    /**
     * Checks to see if the StreakAction has already been performed.
     *
     * @return True if it has already been performed, else false.
     */
    public boolean hasBeenTriggered() {
        return triggered;
    }

    /**
     * Sets the StreakAction's triggered status.
     *
     * @param triggered Whether the StreakAction should be triggered.
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    /**
     * Performs the action.
     */
    public abstract void perform(ArenaPlayer player);
}
