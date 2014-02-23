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

/**
 * Holds the information needed to play a sound to simplify playing it.
 */
public class GameSound implements Effect {
    private final Sound sound;
    private final float volume;
    private float pitch;

    /**
     * Creates a new GameSound.
     *
     * @param sound  The sound.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     */
    public GameSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Gets the GameSound's sound.
     *
     * @return The sound.
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Gets the GameSound's volume.
     *
     * @return The volume.
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Gets the GameSound's pitch.
     *
     * @return The pitch.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the GameSound's pitch.
     *
     * @param pitch The pitch.
     * @return The GameSound.
     */
    public GameSound setPitch(float pitch) {
        this.pitch = pitch;
        return this;
    }

    @Override
    public Effect play(Location location) {
        location.getWorld().playSound(location, sound, volume, pitch);
        return this;
    }

    @Override
    public Effect play(Player player, Location location) {
        player.playSound(location, sound, volume, pitch);
        return this;
    }
}
