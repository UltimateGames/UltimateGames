/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.core.command.commands.arenas;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.message.Messenger;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.arenas.UArena;
import me.ampayne2.ultimategames.core.command.UGCommand;
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
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
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
        Messenger messenger = ultimateGames.getMessenger();
        if (!ultimateGames.getGameManager().gameExists(gameName)) {
            messenger.sendMessage(sender, UGMessage.GAME_DOESNTEXIST);
            return;
        } else if (ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            messenger.sendMessage(sender, UGMessage.ARENA_ALREADYEXISTS);
            return;
        }
        String playerName = sender.getName();
        if (playersSelecting.contains(playerName)) {
            arena.remove(playerName);
            game.remove(playerName);
            playersSelecting.remove(playerName);
            messenger.sendMessage(sender, UGMessage.ARENA_DESELECT);
        } else {
            arena.put(playerName, arenaName);
            game.put(playerName, ultimateGames.getGameManager().getGame(gameName));
            playersSelecting.add(playerName);
            messenger.sendMessage(sender, UGMessage.ARENA_SELECT, arenaName, gameName);
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
        ultimateGames.getArenaManager().addArena(new UArena(ultimateGames, game.get(playerName), arenaName, corner1.get(playerName), corner2.get(playerName)));
        ultimateGames.getMessenger().sendMessage(player, UGMessage.ARENA_CREATE, arenaName, gameName);
    }
}
