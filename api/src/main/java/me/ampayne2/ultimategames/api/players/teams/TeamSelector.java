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
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A team selector GameItem.
 */
public class TeamSelector extends GameItem {
    private final UltimateGames ultimateGames;
    private static final ItemStack TEAM_SELECTOR;

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
        TEAM_SELECTOR = new ItemStack(397, 1, (short) 3);
        ItemMeta meta = TEAM_SELECTOR.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + "Team Selector");
        TEAM_SELECTOR.setItemMeta(meta);
        EnchantGlow.addGlow(TEAM_SELECTOR);
    }
}
