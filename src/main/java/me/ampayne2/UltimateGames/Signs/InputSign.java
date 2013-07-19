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
package me.ampayne2.UltimateGames.Signs;

import me.ampayne2.UltimateGames.Arenas.Arena;

import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;

public class InputSign extends UGSign{

	private String label;
	private Sign sign;
	private Arena arena;
	private String[] lines;
	
	public InputSign(String label, Sign sign, Arena arena) {
		super(sign, arena);
		this.label = label;
		this.sign = sign;
		this.arena = arena;
		arena.getGame().getGamePlugin().handleInputSignCreate(arena, sign, label);
		update();
	}
	
	@Override
	public void onSignClick(PlayerInteractEvent event) {
		//TODO: Permission Check
		arena.getGame().getGamePlugin().handleInputSignClick(arena, sign, label, event);
	}

	@Override
	public String[] getUpdatedLines() {
		return lines;
	}
	
	public void setLines(String[] lines) {
		this.lines = lines;
	}

}
