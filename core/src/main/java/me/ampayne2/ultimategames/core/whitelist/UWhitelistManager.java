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
package me.ampayne2.ultimategames.core.whitelist;

import me.ampayne2.ultimategames.api.whitelist.Whitelist;
import me.ampayne2.ultimategames.api.whitelist.WhitelistManager;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Material;

public class UWhitelistManager implements WhitelistManager {
    private final Whitelist<Material> blockPlaceWhitelist;
    private final Whitelist<Material> blockBreakWhitelist;

    /**
     * Creates a new WhitelistManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UWhitelistManager(UG ultimateGames) {
        blockPlaceWhitelist = new BlockPlaceWhitelist(ultimateGames);
        blockBreakWhitelist = new BlockBreakWhitelist(ultimateGames);
    }

    @Override
    public Whitelist<Material> getBlockPlaceWhitelist() {
        return blockPlaceWhitelist;
    }

    @Override
    public Whitelist<Material> getBlockBreakWhitelist() {
        return blockBreakWhitelist;
    }
}
