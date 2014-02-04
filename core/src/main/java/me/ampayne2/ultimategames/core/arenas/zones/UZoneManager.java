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
package me.ampayne2.ultimategames.core.arenas.zones;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.zones.Zone;
import me.ampayne2.ultimategames.api.arenas.zones.ZoneManager;
import me.ampayne2.ultimategames.core.UG;

import java.util.HashMap;
import java.util.Map;

public class UZoneManager implements ZoneManager {
    private final UG ultimateGames;
    private Map<Arena, Map<String, Zone>> zones = new HashMap<>();

    /**
     * Creates a new Zone Manager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UZoneManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Adds a zone to the Zone Manager.
     *
     * @param zone The zone to add.
     */
    public void addZone(UZone zone) {
        if (zones.containsKey(zone.getArena())) {
            zones.get(zone.getArena()).put(zone.getName(), zone);
        } else {
            Map<String, Zone> arenaZones = new HashMap<>();
            arenaZones.put(zone.getName(), zone);
            zones.put(zone.getArena(), arenaZones);
        }
        zone.save();
    }

    @Override
    public boolean hasZone(Arena arena, String name) {
        return zones.containsKey(arena) && zones.get(arena).containsKey(name);
    }

    @Override
    public Zone getZone(Arena arena, String name) {
        return hasZone(arena, name) ? zones.get(arena).get(name) : null;
    }

    @Override
    public Map<String, Zone> getZonesOfArena(Arena arena) {
        return zones.containsKey(arena) ? zones.get(arena) : new HashMap<String, Zone>();
    }
}
