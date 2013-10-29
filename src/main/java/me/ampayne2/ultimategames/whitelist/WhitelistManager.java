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
package me.ampayne2.ultimategames.whitelist;

import me.ampayne2.ultimategames.UltimateGames;

public class WhitelistManager {
    private final BlockPlaceWhitelist blockPlaceWhitelist;
    private final BlockBreakWhitelist blockBreakWhitelist;

    /**
     * The manager for all ultimategames whitelists.
     *
     * @param ultimateGames The UltimateGames instance.
     */
    public WhitelistManager(UltimateGames ultimateGames) {
        blockPlaceWhitelist = new BlockPlaceWhitelist(ultimateGames);
        blockPlaceWhitelist.reload();
        blockBreakWhitelist = new BlockBreakWhitelist(ultimateGames);
        blockBreakWhitelist.reload();
    }

    /**
     * Gets the Block Place Whitelist.
     *
     * @return The Block Place Whitelist.
     */
    public BlockPlaceWhitelist getBlockPlaceWhitelist() {
        return blockPlaceWhitelist;
    }

    /**
     * Gets the Block Break Whitelist.
     *
     * @return The Block Break Whitelist.
     */
    public BlockBreakWhitelist getBlockBreakWhitelist() {
        return blockBreakWhitelist;
    }
}
