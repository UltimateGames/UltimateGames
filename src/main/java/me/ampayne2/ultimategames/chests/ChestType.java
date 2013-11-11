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
package me.ampayne2.ultimategames.chests;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ChestType {
    RANDOM(RandomChest.class),
    STATIC(StaticChest.class),
    SPECIAL(SpecialChest.class);

    private static final Map<Class<?>, ChestType> CLASS_TYPES = new HashMap<Class<?>, ChestType>();

    static {
        for (ChestType chestType : EnumSet.allOf(ChestType.class)) {
            CLASS_TYPES.put(chestType.getClassType(), chestType);
        }
    }

    private final Class<?> chestClass;

    private ChestType(Class<?> chestClass) {
        this.chestClass = chestClass;
    }

    public Class<?> getClassType() {
        return chestClass;
    }

    public static ChestType getChestTypeFromClass(Class<?> signClass) {
        return CLASS_TYPES.get(signClass);
    }
}
