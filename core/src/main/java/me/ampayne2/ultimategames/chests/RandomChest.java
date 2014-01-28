/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.chests;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.block.Chest;

/**
 * A type of UGChest that contains random contents.
 */
public class RandomChest extends UGChest {
    @SuppressWarnings("unused")
    private final UG ultimateGames;

    /**
     * Creates a new RandomChest.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     * @param chest         The chest.
     * @param arena         The arena.
     * @param label         The label.
     */
    public RandomChest(UG ultimateGames, Chest chest, Arena arena, String label) {
        super(chest, arena, ChestType.RANDOM, label);
        this.ultimateGames = ultimateGames;
    }

    /**
     * Resets the contents of the chest.
     */
    @Override
    public void reset() {
        // TODO: Resetting random chests
    }
}
