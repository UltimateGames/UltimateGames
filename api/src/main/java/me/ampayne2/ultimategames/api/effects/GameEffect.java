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
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds multiple The {@link me.ampayne2.ultimategames.api.effects.Effect}s to simplify playing them all at once.
 */
public class GameEffect implements Effect {
    private final Set<Effect> effects = new HashSet<>();

    /**
     * Creates a new GameEffect.
     *
     * @param effects The The {@link me.ampayne2.ultimategames.api.effects.Effect}s of the GameEffect.
     */
    public GameEffect(Effect... effects) {
        this.effects.addAll(Arrays.asList(effects));
    }

    /**
     * Adds an effect to the GameEffect.
     *
     * @param effect The {@link me.ampayne2.ultimategames.api.effects.Effect}.
     * @return The GameEffect.
     */
    public GameEffect addEffect(Effect effect) {
        effects.add(effect);
        return this;
    }

    /**
     * Removes an effect from the GameEffect.
     *
     * @param effect The {@link me.ampayne2.ultimategames.api.effects.Effect}.
     * @return The GameEffect.
     */
    public GameEffect removeEffect(Effect effect) {
        effects.remove(effect);
        return this;
    }

    /**
     * Gets the GameEffect's The {@link me.ampayne2.ultimategames.api.effects.Effect}s.
     *
     * @return The The {@link me.ampayne2.ultimategames.api.effects.Effect}s of the GameEffect.
     */
    public Set<Effect> getEffects() {
        return effects;
    }

    /**
     * Plays all of the {@link me.ampayne2.ultimategames.api.effects.Effect}s at a location.
     *
     * @param location The location.
     * @return The GameEffect.
     */
    @Override
    public Effect play(Location location) {
        for (Effect effect : effects) {
            effect.play(location);
        }
        return this;
    }

    /**
     * Plays all of the {@link me.ampayne2.ultimategames.api.effects.Effect}s to a player at a location.
     *
     * @param player   The player.
     * @param location The location.
     * @return The GameEffect.
     */
    @Override
    public Effect play(Player player, Location location) {
        for (Effect effect : effects) {
            effect.play(player, location);
        }
        return this;
    }
}
