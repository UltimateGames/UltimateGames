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

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.webapi.WebHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeneralInformationHandler implements WebHandler {

    public UltimateGames plugin;
    public GeneralInformationHandler(UltimateGames ug) {
        this.plugin = ug;
    }

    @Override
    public String sendResult() {
        JSONArray jsonArray = new JSONArray();
        for (Arena arena : plugin.getArenaManager().getArenas()) {
            JSONObject jsonEntry = new JSONObject();
            try {
                jsonEntry.append("arenaName", arena.getName());
                jsonEntry.append("gameName", arena.getGame().getGameDescription().getName());
                jsonEntry.append("currentPlayers", arena.getPlayers().size());
                jsonEntry.append("maxPlayers", arena.getMaxPlayers());
                jsonEntry.append("status", arena.getStatus().name());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonEntry);
        }
        return jsonArray.toString();
    }
}
