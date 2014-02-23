/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.api.effects;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * A {@link me.ampayne2.ultimategames.api.effects.GameSound} that chooses a random pitch to play at.
 */
public class RandomPitchGameSound extends GameSound {
    private float minPitch;
    private float maxPitch;
    private static final Random RANDOM = new Random();

    /**
     * Creates a new RandomPitchGameSound.
     *
     * @param sound    The sound.
     * @param volume   The volume of the sound.
     * @param minPitch The minimum pitch of the sound.
     * @param maxPitch The maximum pitch of the sound.
     */
    public RandomPitchGameSound(Sound sound, float volume, float minPitch, float maxPitch) {
        super(sound, volume, 1);
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
    }

    /**
     * Creates a new RandomPitchGameSound with the default minimum and maximum pitch.
     *
     * @param sound  The sound.
     * @param volume The volume of the sound.
     */
    public RandomPitchGameSound(Sound sound, float volume) {
        this(sound, volume, 0, 2);
    }

    /**
     * Sets the RandomPitchGameSound's minimum pitch.
     *
     * @param minPitch The minimum pitch.
     * @return The RandomPitchGameSound.
     */
    public RandomPitchGameSound setMinimumPitch(float minPitch) {
        this.minPitch = minPitch;
        return this;
    }

    /**
     * Sets the RandomPitchGameSound's maximum pitch.
     *
     * @param maxPitch The maximum pitch.
     * @return The RandomPitchGameSound.
     */
    public RandomPitchGameSound setMaximumPitch(float maxPitch) {
        this.maxPitch = maxPitch;
        return this;
    }

    @Override
    public Effect play(Location location) {
        setPitch(RANDOM.nextFloat() * (maxPitch - minPitch + 1) + minPitch);
        return super.play(location);
    }

    @Override
    public Effect play(Player player, Location location) {
        setPitch(RANDOM.nextFloat() * (maxPitch - minPitch + 1) + minPitch);
        return super.play(player, location);
    }
}
