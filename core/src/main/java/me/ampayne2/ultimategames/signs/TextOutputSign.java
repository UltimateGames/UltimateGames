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
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;

public class TextOutputSign extends USign {
    private String label;
    private List<String> lines = new ArrayList<>();

    public TextOutputSign(String label, org.bukkit.block.Sign sign, Arena arena) {
        super(sign, arena, SignType.TEXT_OUTPUT);
        this.label = label;
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
            this.lines = new ArrayList<>();
        } else {
            this.lines = lines;
        }
    }

    public String getLabel() {
        return label;
    }
}
