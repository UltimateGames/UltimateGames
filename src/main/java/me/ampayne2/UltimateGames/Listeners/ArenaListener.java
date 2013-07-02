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
package me.ampayne2.UltimateGames.Listeners;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;

public class ArenaListener implements Listener{

	private UltimateGames ultimateGames;
	
	public ArenaListener(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
		if (arena != null) {
			if (!arena.getArenaSetting("allowBreaking")) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getBlock().getLocation());
		if (arena != null) {
			if (!arena.getArenaSetting("allowBuilding")) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onTntExplode(EntityExplodeEvent event) {
		if (event.getEntity() instanceof TNTPrimed) {
			Arena arena = ultimateGames.getArenaManager().getLocationArena(event.getLocation());
			if (arena != null) {
				if (!arena.getArenaSetting("allowExplosionDamage") && !arena.getArenaSetting("allowExplosionBlockBreaking")) {
					event.setCancelled(true);
				} else if (!arena.getArenaSetting("allowExplosionDamage")) {
					event.blockList().clear();
				} else if (!arena.getArenaSetting("allowExplosionBlockBreaking")) {
					event.getLocation().getWorld().createExplosion(event.getLocation().getX(), event.getLocation().getY(), event.getLocation().getZ(), 4F, false, false);
					event.setCancelled(true);
				}
			}
		}
	}
}
