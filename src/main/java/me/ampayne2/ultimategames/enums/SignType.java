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
package me.ampayne2.ultimategames.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import me.ampayne2.ultimategames.signs.ClickInputSign;
import me.ampayne2.ultimategames.signs.LobbySign;
import me.ampayne2.ultimategames.signs.RedstoneInputSign;
import me.ampayne2.ultimategames.signs.RedstoneOutputSign;
import me.ampayne2.ultimategames.signs.TextOutputSign;

public enum SignType {
    
    LOBBY(LobbySign.class, false), 
    CLICK_INPUT(ClickInputSign.class, true), 
    REDSTONE_INPUT(RedstoneInputSign.class, true), 
    TEXT_OUTPUT(TextOutputSign.class, true), 
    REDSTONE_OUTPUT(RedstoneOutputSign.class, true);

    private static final Map<Class<?>, SignType> CLASS_TYPES = new HashMap<Class<?>, SignType>();

    static {
        for (SignType signType : EnumSet.allOf(SignType.class)) {
            CLASS_TYPES.put(signType.getClassType(), signType);
        }
    }

    private Class<?> signClass;
    private Boolean hasLabel;

    private SignType(Class<?> signClass, Boolean hasLabel) {
        this.hasLabel = hasLabel;
        this.signClass = signClass;
    }

    public Boolean hasLabel() {
        return hasLabel;
    }

    public Class<?> getClassType() {
        return signClass;
    }

    public static SignType getSignTypeFromClass(Class<?> signClass) {
        return CLASS_TYPES.get(signClass);
    }
    
}
