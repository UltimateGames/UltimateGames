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
package me.ampayne2.ultimategames.api.games.items;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * A GameItem that can only be used once every given amount of ticks.<br>
 * Must be registered with the {@link GameItemManager}.
 */
public class RepeatingGameItem extends GameItem {
    private final UltimateGames ultimateGames;
    private final long repeatingPeriod;
    private Set<String> repeating = new HashSet<>();

    /**
     * Creates a new RepeatingGameItem.
     *
     * @param ultimateGames   The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param item            The ItemStack of the RepeatingGameItem.
     * @param repeatingPeriod The minimum amount of ticks before the GameItem can be used again.
     */
    public RepeatingGameItem(UltimateGames ultimateGames, ItemStack item, long repeatingPeriod) {
        super(item, false);
        this.ultimateGames = ultimateGames;
        this.repeatingPeriod = repeatingPeriod;
    }

    @Override
    public boolean click(Arena arena, PlayerInteractEvent event) {
        final String playerName = event.getPlayer().getName();
        if (!repeating.contains(playerName)) {
            repeating.add(playerName);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ultimateGames.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    if (repeating.contains(playerName)) {
                        repeating.remove(playerName);
                    }
                }
            }, repeatingPeriod);
        }
        return true;
    }

    /**
     * Checks if the RepeatingGameItem can be clicked.
     *
     * @param arena The arena.
     * @param playerName The name of the player clicking.
     * @return True if the RepeatingGameItem can be used again, else false.
     */
    public boolean canClick(Arena arena, String playerName) {
        return arena.getStatus() == ArenaStatus.RUNNING && !repeating.contains(playerName);
    }
}
