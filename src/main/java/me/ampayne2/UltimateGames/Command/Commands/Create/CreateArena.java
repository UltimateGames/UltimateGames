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
package me.ampayne2.UltimateGames.Command.Commands.Create;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;
import me.ampayne2.UltimateGames.Games.Game;

public class CreateArena implements UGCommand, Listener {

	private UltimateGames ultimateGames;
	private ArrayList<String> playersSelecting = new ArrayList<String>();
	private HashMap<String, Location> corner1 = new HashMap<String, Location>();
	private HashMap<String, Location> corner2 = new HashMap<String, Location>();
	private HashMap<String, Game> game = new HashMap<String, Game>();
	private HashMap<String, String> arenaName =  new HashMap<String, String>();
	
	public CreateArena(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	
	// TO BE FINISHED
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 2) {
			return;
		}
		String gameName = args[1];
		String arena = args[0];
		if (!ultimateGames.getGameManager().gameExists(gameName)) {
			return; //game doesn't exist
		}
		arenaName.put(sender.getName(), arena);
		game.put(sender.getName(), ultimateGames.getGameManager().getGame(gameName));
		playersSelecting.add(sender.getName());
		HashMap<String, String> replace = new HashMap<String, String>();
		replace.put("%ArenaName%", arena);
		replace.put("%GameName%", gameName);
		ultimateGames.getMessageManager().send(sender.getName(), replace, "arenas.create");
	}

	@EventHandler
	public void onPunch(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && playersSelecting.contains(event.getPlayer().getName())) {
			
		}
	}

}
