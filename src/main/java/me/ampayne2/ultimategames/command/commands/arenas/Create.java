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
package me.ampayne2.ultimategames.command.commands.arenas;

import java.util.ArrayList;
import java.util.HashMap;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Create implements UGCommand, Listener {
    private UltimateGames ultimateGames;
    private ArrayList<String> playersSelecting = new ArrayList<String>();
    private HashMap<String, Location> corner1 = new HashMap<String, Location>();
    private HashMap<String, Location> corner2 = new HashMap<String, Location>();
    private HashMap<String, Game> game = new HashMap<String, Game>();
    private HashMap<String, String> arenaName = new HashMap<String, String>();

    public Create(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            return;
        }
        String gameName = args[1];
        String arena = args[0];
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            ultimateGames.getMessageManager().sendMessage(sender.getName(), "arenas.gamenotexist");
            return; //game doesn't exist
        }
        arenaName.put(sender.getName(), arena);
        game.put(sender.getName(), ultimateGames.getGameManager().getGame(gameName));
        playersSelecting.add(sender.getName());
        ultimateGames.getMessageManager().sendReplacedMessage(sender.getName(), "arenas.select", arena, gameName);
    }

    @EventHandler
    public void onSelect(PlayerInteractEvent event) {
        String name = event.getPlayer().getName();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && playersSelecting.contains(name)) {
            if (!corner1.containsKey(name)) {
                corner1.put(name, event.getClickedBlock().getLocation());
                event.setCancelled(true);
            } else if (corner1.containsKey(name) && !corner2.containsKey(name)) {
                corner2.put(name, event.getClickedBlock().getLocation());
                event.setCancelled(true);
                createArena(name);
            }
        }
    }

    public void createArena(String playerName) {
        String arena = arenaName.get(playerName);
        String gameName = game.get(playerName).getGameDescription().getName();
        if (ultimateGames.getArenaManager().arenaExists(arena, gameName)) {
            ultimateGames.getMessageManager().sendReplacedMessage(playerName, "arenas.failedtocreate", arena, gameName, ultimateGames.getMessageManager().getMessage("arenas.alreadyexists"));
            return;
        } else {
            Arena newArena = new Arena(ultimateGames, game.get(playerName), arenaName.get(playerName), corner1.get(playerName), corner2.get(playerName));
            newArena.setStatus(ArenaStatus.valueOf(ultimateGames.getConfigManager().getArenaConfig().getConfig().getString("Arenas." + newArena.getGame().getGameDescription().getName() + "." + newArena.getName() + ".Status")));
            ultimateGames.getArenaManager().addArena(newArena);
            playersSelecting.remove(playerName);
            corner1.remove(playerName);
            corner2.remove(playerName);
            game.remove(playerName);
            arenaName.remove(playerName);
            ultimateGames.getMessageManager().sendReplacedMessage(playerName, "arenas.create", newArena.getName(), newArena.getGame().getGameDescription().getName());
        }
    }
}
