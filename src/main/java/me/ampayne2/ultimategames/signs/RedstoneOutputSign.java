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

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class RedstoneOutputSign extends UGSign {
    private final String label;
    private List<String> lines = new ArrayList<String>();
    private Boolean powered;

    public RedstoneOutputSign(String label, Sign sign, Arena arena) {
        super(sign, arena, SignType.REDSTONE_OUTPUT);
        this.label = label;
        powered = false;
        arena.getGame().getGamePlugin().handleUGSignCreate(this, getSignType());
        update();
    }

    @Override
    public void onSignTrigger(Event event) {

    }

    @Override
    public List<String> getUpdatedLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        if (lines == null) {
            this.lines = new ArrayList<String>();
        } else {
            this.lines = lines;
        }
    }

    public String getLabel() {
        return label;
    }

    public Boolean isPowered() {
        return powered;
    }

    public void setPowered(Boolean powered) {
        this.powered = powered;
        if (powered) {
            getSign().getLocation().getBlock().setType(Material.REDSTONE_BLOCK);
        } else {
            getSign().getLocation().getBlock().setType(getSign().getType());
            getSign().getLocation().getBlock().getState().setData(getSign().getData());
            getSign().update();
        }
    }
}
