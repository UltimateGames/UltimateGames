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
package me.ampayne2.ultimategames.api.players.teams;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.games.items.GameItem;
import me.ampayne2.ultimategames.api.utils.EnchantGlow;
import net.canarymod.Canary;
import net.canarymod.api.inventory.Item;
import net.canarymod.chat.Colors;
import net.canarymod.chat.TextFormat;

/**
 * A team selector GameItem.
 */
public class TeamSelector extends GameItem {
    private final UltimateGames ultimateGames;
    private static final Item TEAM_SELECTOR;

    /**
     * Creates a new team selector item.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     */
    public TeamSelector(UltimateGames ultimateGames) {
        super(TEAM_SELECTOR, false);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean click(Arena arena, PlayerInteractEvent event) {
        ultimateGames.getTeamManager().getTeamSelector(arena).open(event.getPlayer());
        return true;
    }

    static {
        TEAM_SELECTOR = Canary.factory().getItemFactory().newItem(397,3,1);
        TEAM_SELECTOR.setDisplayName(Colors.BLUE + TextFormat.BOLD + "Team Selector");
        EnchantGlow.addGlow(TEAM_SELECTOR);
    }
}
