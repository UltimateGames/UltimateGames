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
/*
 * This file is part of UltimateGames ENGINE.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames ENGINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames ENGINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames ENGINE.  If not, see <http://www.gnu.org/licenses/>.
 */
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
package me.ampayne2.ultimategames.players.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.entity.Player;

/**
 * A tiered GameClass. Resets inventories based on the player's highest unlocked tier.
 */
public abstract class TieredClass extends GameClass {
    private final UltimateGames ultimateGames;

    /**
     * Creates a new GameClass.
     *
     * @param ultimateGames           The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param game                    The game of the GameClass.
     * @param name                    The name of the GameClass.
     * @param canSwitchToWithoutDeath If a player can join the GameClass without having to die first.
     */
    public TieredClass(UltimateGames ultimateGames, Game game, String name, boolean canSwitchToWithoutDeath) {
        super(ultimateGames, game, name, canSwitchToWithoutDeath);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean hasAccess(Player player) {
        return !(isUnlockable() && getUnlockableString() != null) || ultimateGames.getPointManager().hasPerk(getGame(), player.getName(), getUnlockableString() + "1");
    }

    /**
     * Gets the highest tier of this class the player has unlocked.
     *
     * @param player The player.
     * @return The highest tier unlocked.
     */
    public int getTier(Player player) {
        int tier = 1;
        while (ultimateGames.getPointManager().hasPerk(getGame(), player.getName(), getName().toLowerCase() + (tier + 1))) {
            tier++;
        }
        return tier;
    }

    @Override
    public void resetInventory(Player player) {
        resetInventory(player, getTier(player));
    }

    /**
     * Resets a player's inventory.
     *
     * @param player The player whose inventory you want to reset.
     * @param tier   The tier of the player's class.
     */
    public abstract void resetInventory(Player player, int tier);
}
