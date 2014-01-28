/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.arenas.countdowns;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Bukkit;

/**
 * A type of countdown used to end the game.
 */
public class EndingCountdown extends Countdown {
    private final Boolean expDisplay;
    private static final int FINAL_COUNTDOWN_THRESHOLD = 10;
    private static final int END_COUNTDOWN_TIME = 0;
    private static final int TPS = 20;

    /**
     * Creates a new Ending Countdown.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param arena         The arena of the countdown.
     * @param expDisplay    If the countdown should display exp.
     */
    public EndingCountdown(UltimateGames ultimateGames, Arena arena, int initialSeconds, Boolean expDisplay) {
        super(ultimateGames, arena, initialSeconds * TPS, TPS);
        this.expDisplay = expDisplay;
    }

    /**
     * Gets the seconds left on the ending countdown.
     *
     * @return The seconds left.
     */
    public int getSecondsLeft() {
        return ticksLeft / TPS;
    }

    /**
     * Checks if the ending countdown is displayed with exp.
     *
     * @return True if the ending countdown has an exp display, else false.
     */
    public boolean hasExpDisplay() {
        return expDisplay;
    }

    @Override
    public void run() {
        int secondsLeft = getSecondsLeft();
        if (expDisplay) {
            for (String playerName : arena.getPlayers()) {
                Bukkit.getPlayerExact(playerName).setLevel(secondsLeft);
            }
            for (String playerName : arena.getSpectators()) {
                Bukkit.getPlayerExact(playerName).setLevel(secondsLeft);
            }
        }
        if (secondsLeft > END_COUNTDOWN_TIME && secondsLeft <= FINAL_COUNTDOWN_THRESHOLD) {
            ultimateGames.getMessenger().sendMessage(arena, "countdowns.timeleftend", Integer.toString(secondsLeft));
        } else if (secondsLeft == END_COUNTDOWN_TIME) {
            ultimateGames.getCountdownManager().stopEndingCountdown(arena);
            ultimateGames.getArenaManager().endArena(arena);
        }
        ticksLeft -= TPS;
    }
}
