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
package me.ampayne2.ultimategames.webapi.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.webapi.WebHandler;

public class GeneralInformationHandler implements WebHandler {

    public UltimateGames plugin;
    public GeneralInformationHandler(UltimateGames ug) {
        this.plugin = ug;
    }

    @Override
    public String sendResult() {
        Gson gson = new Gson();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Arena arena : plugin.getArenaManager().getArenas()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("arenaName", arena.getName());
            map.put("gameName", arena.getGame().getName());
            map.put("currentPlayers", arena.getPlayers().size() + "");
            map.put("maxPlayers", arena.getMaxPlayers() + "");
            map.put("status", arena.getStatus().name());
            list.add(map);
        }
        return gson.toJson(list);
    }
}
