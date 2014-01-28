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
package me.ampayne2.ultimategames.core.signs;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.signs.SignType;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class ClickInputSign extends USign {
    private String label;
    private List<String> lines = new ArrayList<>();

    public ClickInputSign(String label, org.bukkit.block.Sign sign, Arena arena) {
        super(sign, arena, SignType.CLICK_INPUT);
        this.label = label;
        arena.getGame().getGamePlugin().handleUGSignCreate(this, getSignType());
        update();
    }

    @Override
    public void onSignTrigger(Event event) {
        getArena().getGame().getGamePlugin().handleInputSignTrigger(this, getSignType(), event);
    }

    @Override
    public List<String> getUpdatedLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        if (lines == null) {
            this.lines = new ArrayList<>();
        } else {
            this.lines = lines;
        }
    }

    public String getLabel() {
        return label;
    }
}
