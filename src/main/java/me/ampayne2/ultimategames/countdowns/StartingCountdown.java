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

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.effects.GameSound;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * A type of countdown used to start the game.
 */
public class StartingCountdown extends Countdown {
    private final int initialSeconds;
    private static final GameSound TICK_SOUND = new GameSound(Sound.NOTE_PLING, 1, 1);
    private static final GameSound FINAL_SOUND = new GameSound(Sound.NOTE_PLING, 1, 5);
    private static final int TICK_SOUND_THRESHOLD = 5;
    private static final int FINAL_COUNTDOWN_THRESHOLD = 10;
    private static final int END_COUNTDOWN_TIME = 0;
    private static final int TPS = 20;

    /**
     * Creates a new Starting Countdown.
     *
     * @param ultimateGames A reference to the UltimateGames instance.
     * @param arena         The arena of the countdown.
     */
    public StartingCountdown(UltimateGames ultimateGames, Arena arena, int initialSeconds) {
        super(ultimateGames, arena, initialSeconds * TPS, TPS);
        this.initialSeconds = initialSeconds;
        arena.setStatus(ArenaStatus.STARTING);
    }

    public int getSecondsLeft() {
        return ticksLeft / TPS;
    }

    @Override
    public void run() {
        int secondsLeft = getSecondsLeft();
        if (secondsLeft <= TICK_SOUND_THRESHOLD && secondsLeft > END_COUNTDOWN_TIME) {
            for (String playerName : arena.getPlayers()) {
                Player player = Bukkit.getPlayerExact(playerName);
                TICK_SOUND.play(player, player.getLocation());
            }
        }
        if (secondsLeft > END_COUNTDOWN_TIME && initialSeconds == secondsLeft) {
            ultimateGames.getMessageManager().sendMessage(arena, "countdowns.timeleftstart", Integer.toString(secondsLeft));
        } else if (secondsLeft > END_COUNTDOWN_TIME && secondsLeft <= FINAL_COUNTDOWN_THRESHOLD) {
            ultimateGames.getMessageManager().sendMessage(arena, "countdowns.timeleftstart", Integer.toString(secondsLeft));
        } else if (secondsLeft == END_COUNTDOWN_TIME) {
            ultimateGames.getCountdownManager().stopStartingCountdown(arena);
            ultimateGames.getArenaManager().beginArena(arena);
            for (String playerName : arena.getPlayers()) {
                Player player = Bukkit.getPlayerExact(playerName);
                FINAL_SOUND.play(player, player.getLocation());
            }
        }
        ticksLeft -= TPS;
    }
}
