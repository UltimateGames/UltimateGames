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
package me.ampayne2.ultimategames.command;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.command.commands.ExtCmd;
import me.ampayne2.ultimategames.command.commands.Reload;
import me.ampayne2.ultimategames.command.commands.SetLobby;
import me.ampayne2.ultimategames.command.commands.arenas.AddSpawn;
import me.ampayne2.ultimategames.command.commands.arenas.Begin;
import me.ampayne2.ultimategames.command.commands.arenas.Create;
import me.ampayne2.ultimategames.command.commands.arenas.Edit;
import me.ampayne2.ultimategames.command.commands.arenas.End;
import me.ampayne2.ultimategames.command.commands.arenas.Join;
import me.ampayne2.ultimategames.command.commands.arenas.Leave;
import me.ampayne2.ultimategames.command.commands.arenas.Open;
import me.ampayne2.ultimategames.command.commands.arenas.Spectate;
import me.ampayne2.ultimategames.command.commands.arenas.Stop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin implements Listener {
    
    private UltimateGames ultimateGames;
    private final SubCommand mainCommand = new SubCommand();
    private List<String> bypassers = new ArrayList<String>();

    public CommandController(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;

        SubCommand arena = new SubCommand();
        Create create = new Create(ultimateGames);
        ultimateGames.getServer().getPluginManager().registerEvents(create, ultimateGames);
        arena.addCommand(ultimateGames, "create", "ultimategames.arena.create", create, 2);
        arena.addCommand(ultimateGames, "addspawn", "ultimategames.arena.addspawn", new AddSpawn(ultimateGames), 3);
        arena.addCommand(ultimateGames, "open", "ultimategames.arena.open", new Open(ultimateGames), 2);
        arena.addCommand(ultimateGames, "begin", "ultimategames.arena.begin", new Begin(ultimateGames), 2);
        arena.addCommand(ultimateGames, "end", "ultimategames.arena.end", new End(ultimateGames), 2);
        arena.addCommand(ultimateGames, "stop", "ultimategames.arena.stop", new Stop(ultimateGames), 2);
        arena.addCommand(ultimateGames, "join", "ultimategames.arena.join", new Join(ultimateGames), 2);
        arena.addCommand(ultimateGames, "edit", "ultimategames.arena.edit", new Edit(ultimateGames), 0);
        arena.addCommand(ultimateGames, "spectate", "ultimategames.arena.spectate", new Spectate(ultimateGames), 2);
        
        mainCommand.addCommand(ultimateGames, "arena", null, arena, null);
        
        mainCommand.addCommand(ultimateGames, "leave", "ultimategames.arena.leave", new Leave(ultimateGames), 0);

        mainCommand.addCommand(ultimateGames, "setlobby", "ultimategames.setlobby", new SetLobby(ultimateGames), 0);
        
        mainCommand.addCommand(ultimateGames, "cmd", "ultimategames.extcmd", new ExtCmd(ultimateGames), -1);
        
        mainCommand.addCommand(ultimateGames, "reload", "ultimategames.reload", new Reload(ultimateGames), 0);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!cmd.getName().equalsIgnoreCase("ultimategames")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        if (args.length == 0) {
            if (mainCommand.commandExist("")) {
                mainCommand.execute("", sender, args);
                return true;
            } else {
                return true;
            }
        }

        if (mainCommand.commandExist(args[0])) {
            String[] newArgs;
            if (args.length == 1) {
                newArgs = new String[0];
            } else {
                newArgs = new String[args.length - 1];
                System.arraycopy(args, 1, newArgs, 0, args.length - 1);
            }

            mainCommand.execute(args[0], sender, newArgs);
            return true;
        } else {
            return true;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            String[] command = event.getMessage().split(" ");
            if (command.length > 0 && !command[0].equalsIgnoreCase("/ug") && !command[0].equalsIgnoreCase("/ultimategames") && !bypassers.contains(player.getName())) {
                event.setCancelled(true);
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                String[] args;
                if (command.length == 1) {
                    args = new String[0];
                } else {
                    args = new String[command.length - 1];
                    System.arraycopy(command, 1, args, 0, command.length - 1);
                }
                arena.getGame().getGamePlugin().onArenaCommand(arena, command[0].replace("/", ""), (CommandSender) player, args);
                event.setCancelled(true);
            }
        }
    }
    
    public void addBlockBypasser(String playerName) {
        bypassers.add(playerName);
    }
    
    public void removeBlockBypasser(String playerName) {
        if (bypassers.contains(playerName)) {
            bypassers.remove(playerName);
        }
    }
}
