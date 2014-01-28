/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.command.commands.arenas;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.arenas.ArenaStatus;
import me.ampayne2.ultimategames.arenas.UArena;
import me.ampayne2.ultimategames.command.UGCommand;
import me.ampayne2.ultimategames.config.ConfigType;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A command that creates an arena.
 */
public class Create extends UGCommand implements Listener {
    private final UG ultimateGames;
    private List<String> playersSelecting = new ArrayList<>();
    private Map<String, Location> corner1 = new HashMap<>();
    private Map<String, Location> corner2 = new HashMap<>();
    private Map<String, Game> game = new HashMap<>();
    private Map<String, String> arena = new HashMap<>();

    /**
     * Creates the Create command.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public Create(UG ultimateGames) {
        super(ultimateGames, "create", "Creates an arena.", "/ug arena create <arena> <game>", new Permission("ultimategames.arena.create", PermissionDefault.OP), 2, true);
        this.ultimateGames = ultimateGames;
        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String arenaName = args[0];
        String gameName = args[1];
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, "games.doesntexist");
            return;
        } else if (ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            ultimateGames.getMessenger().sendMessage(sender, "arenas.alreadyexists");
            return;
        }
        String playerName = sender.getName();
        if (playersSelecting.contains(playerName)) {
            arena.remove(playerName);
            game.remove(playerName);
            playersSelecting.remove(playerName);
            ultimateGames.getMessenger().sendMessage(sender, "arenas.deselect");
        } else {
            arena.put(playerName, arenaName);
            game.put(playerName, ultimateGames.getGameManager().getGame(gameName));
            playersSelecting.add(playerName);
            ultimateGames.getMessenger().sendMessage(sender, "arenas.select", arenaName, gameName);
        }
    }

    @EventHandler
    public void onSelect(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && playersSelecting.contains(playerName)) {
            if (!corner1.containsKey(playerName)) {
                corner1.put(playerName, event.getClickedBlock().getLocation());
                event.setCancelled(true);
            } else if (corner1.containsKey(playerName) && !corner2.containsKey(playerName)) {
                corner2.put(playerName, event.getClickedBlock().getLocation());
                event.setCancelled(true);
                createArena(player);
                playersSelecting.remove(playerName);
                corner1.remove(playerName);
                corner2.remove(playerName);
                game.remove(playerName);
                arena.remove(playerName);
            }
        }
    }

    public void createArena(Player player) {
        String playerName = player.getName();
        String arenaName = arena.get(playerName);
        String gameName = game.get(playerName).getName();
        UArena newArena = new UArena(ultimateGames, game.get(playerName), arenaName, corner1.get(playerName), corner2.get(playerName));
        newArena.setStatus(ArenaStatus.valueOf(ultimateGames.getConfigManager().getConfig(ConfigType.ARENA).getString("Arenas." + gameName + "." + arenaName + ".Status")));
        ultimateGames.getArenaManager().addArena(newArena);
        ultimateGames.getMessenger().sendMessage(player, "arenas.create", arenaName, gameName);
    }
}
