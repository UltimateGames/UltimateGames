/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.arenas.countdowns;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.arenas.countdowns.Countdown;
import me.ampayne2.ultimategames.api.effects.GameSound;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.arenas.UArena;
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
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     * @param arena         The arena of the countdown.
     */
    public StartingCountdown(UG ultimateGames, Arena arena, int initialSeconds) {
        super(ultimateGames, arena, initialSeconds * TPS, TPS);
        this.initialSeconds = initialSeconds;
        ((UArena) arena).setStatus(ArenaStatus.STARTING);
    }

    /**
     * Gets the seconds left on the starting countdown.
     *
     * @return The seconds left.
     */
    public int getSecondsLeft() {
        return ticksLeft / TPS;
    }

    @Override
    public void run() {
        int secondsLeft = getSecondsLeft();
        if (secondsLeft > END_COUNTDOWN_TIME) {
            if (secondsLeft <= TICK_SOUND_THRESHOLD) {
                for (String playerName : arena.getPlayers()) {
                    Player player = Bukkit.getPlayerExact(playerName);
                    TICK_SOUND.play(player, player.getLocation());
                }
            }
            if (secondsLeft == initialSeconds || secondsLeft <= FINAL_COUNTDOWN_THRESHOLD) {
                ultimateGames.getMessenger().sendMessage(arena, UGMessage.COUNTDOWN_TIMELEFT_START, Integer.toString(secondsLeft));
            }
        } else {
            ((UCountdownManager) ultimateGames.getCountdownManager()).stopStartingCountdown(arena, false);
            ultimateGames.getArenaManager().beginArena(arena);
            for (String playerName : arena.getPlayers()) {
                Player player = Bukkit.getPlayerExact(playerName);
                FINAL_SOUND.play(player, player.getLocation());
            }
        }
        ticksLeft -= TPS;
    }
}
