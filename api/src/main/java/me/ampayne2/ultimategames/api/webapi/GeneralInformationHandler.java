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
package me.ampayne2.ultimategames.api.webapi;

import com.google.gson.Gson;
import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralInformationHandler implements WebHandler {
    private UltimateGames ultimateGames;

    public GeneralInformationHandler(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public String sendResult() {
        Gson gson = new Gson();
        List<Map<String, String>> list = new ArrayList<>();
        for (Arena arena : ultimateGames.getArenaManager().getArenas()) {
            Map<String, String> map = new HashMap<>();
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
