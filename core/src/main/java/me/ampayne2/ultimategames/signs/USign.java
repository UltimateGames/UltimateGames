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
package me.ampayne2.ultimategames.signs;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Material;
import org.bukkit.event.Event;

import java.util.List;

public abstract class USign implements Sign {
    private final org.bukkit.block.Sign sign;
    private final Arena arena;
    private final SignType signType;

    /**
     * Creates a new sign
     *
     * @param sign  Sign to be turned into UGSign.
     * @param arena Arena of the sign.
     */
    public USign(org.bukkit.block.Sign sign, Arena arena, SignType signType) {
        this.sign = sign;
        this.arena = arena;
        this.signType = signType;
    }

    /**
     * Called when the UGSign is triggered.
     */
    public abstract void onSignTrigger(Event event);

    @Override
    public abstract List<String> getUpdatedLines();

    @Override
    public org.bukkit.block.Sign getSign() {
        return sign;
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    @Override
    public SignType getSignType() {
        return signType;
    }

    @Override
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
