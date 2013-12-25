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
package me.ampayne2.ultimategames.utils;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MenuIcon implements Listener {

    private final UltimateGames ultimateGames;
    private final ItemStack icon;
    private final Game game;
    public MenuIcon(UltimateGames ultimateGames, ItemStack icon, Game game) {
        this.ultimateGames = ultimateGames;
        this.icon = icon;
        this.game = game;
        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().isSimilar(icon)) {
                ultimateGames.getGameClassManager().getMenu(ultimateGames, game, event.getPlayer()).open(event.getPlayer());
            }
        }
    }
}
