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
package me.ampayne2.ultimategames.signs;

import java.util.List;

import me.ampayne2.ultimategames.arenas.Arena;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;

public abstract class UGSign {
    private Sign sign;
    private Arena arena;

    /**
     * Creates a new sign
     * @param sign Sign to be turned into UGSign.
     * @param arena Arena of the sign.
     */
    public UGSign(Sign sign, Arena arena) {
        this.sign = sign;
        this.arena = arena;
    }

    /**
     * Called when the UGSign is triggered.
     */
    public abstract void onSignTrigger(Event event);

    /**
     * Gets the UGSign's updated lines.
     * @return sign The UGSign's updated lines.
     */
    public abstract List<String> getUpdatedLines();

    /**
     * Gets the UGSign's Sign.
     * @return sign The UGSign's Sign.
     */
    public Sign getSign() {
        return sign;
    }

    /**
     * Gets the UGSign's Arena.
     * @return arena The UGSign's Arena.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Updates the UGSign.
     */
    public void update() {
        Material material = sign.getLocation().getBlock().getType();
        if (material == Material.WALL_SIGN || material == Material.SIGN_POST) {
            List<String> lines = getUpdatedLines();
            for (int i = 0; i < 4; i++) {
                if (lines.size() > i) {
                    sign.setLine(i, lines.get(i));
                }
            }
            sign.update();
        }
    }
}
