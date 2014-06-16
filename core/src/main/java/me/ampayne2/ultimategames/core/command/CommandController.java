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
package me.ampayne2.ultimategames.core.command;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.message.PageList;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.command.commands.*;
import me.ampayne2.ultimategames.core.command.commands.arenas.*;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The ultimategames command executor.
 */
public class CommandController implements TabExecutor, Listener {
    private final UG ultimateGames;
    private final Command mainCommand;
    private final PageList pageList;
    private final Set<String> bypassers = new HashSet<>();

    /**
     * Creates a new command controller.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public CommandController(UG ultimateGames) {
        this.ultimateGames = ultimateGames;

        mainCommand = new Command(ultimateGames, "ultimategames", new Permission("ultimategames.all", PermissionDefault.OP), false)
                .addChildCommand(new About(ultimateGames))
                .addChildCommand(new Help(ultimateGames))
                .addChildCommand(new Leave(ultimateGames))
                .addChildCommand(new Lobby(ultimateGames))
                .addChildCommand(new SetLobby(ultimateGames))
                .addChildCommand(new ExtCmd(ultimateGames))
                .addChildCommand(new Command(ultimateGames, "arena", new Permission("ultimategames.arena.all", PermissionDefault.OP), false)
                        .addChildCommand(new Create(ultimateGames))
                        .addChildCommand(new Join(ultimateGames))
                        .addChildCommand(new Spectate(ultimateGames))
                        .addChildCommand(new Edit(ultimateGames))
                        .addChildCommand(new Open(ultimateGames))
                        .addChildCommand(new Begin(ultimateGames))
                        .addChildCommand(new End(ultimateGames))
                        .addChildCommand(new Stop(ultimateGames))
                        .addChildCommand(new AddSpawn(ultimateGames))
                        .addChildCommand(new AddZone(ultimateGames))
                        .addChildCommand(new SetSpectatorSpawn(ultimateGames)))
                        .addChildCommand(new SetArenaLobby(ultimateGames))
                        .addChildCommand(new SetArenaLobbySpawnPoint(ultimateGames));

        ultimateGames.getCommand(mainCommand.getName()).setExecutor(this);
        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);

        pageList = new CommandPageList(ultimateGames, mainCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ultimategames")) {
            String subCommand = args.length > 0 ? args[0] : "";
            if (mainCommand.hasChildCommand(subCommand)) {
                if (subCommand.equals("")) {
                    mainCommand.execute(subCommand, sender, args);
                } else {
                    String[] newArgs;
                    if (args.length == 1) {
                        newArgs = new String[0];
                    } else {
                        newArgs = new String[args.length - 1];
                        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    }
                    mainCommand.execute(subCommand, sender, newArgs);
                }
            } else {
                ultimateGames.getMessenger().sendMessage(sender, UGMessage.COMMAND_INVALIDSUBCOMMAND, "\"" + subCommand + "\"", "\"ultimategames\"");
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ultimategames")) {
            Command command = mainCommand;
            if (args.length > 0) {
                int commandAmount = 1;
                for (String arg : args) {
                    if (command.hasChildCommand(arg)) {
                        command = command.getChildCommand(arg);
                        commandAmount++;
                    }
                }
                String[] newArgs;
                if (args.length == 1) {
                    newArgs = new String[0];
                } else {
                    newArgs = new String[args.length - commandAmount];
                    System.arraycopy(args, commandAmount, newArgs, 0, args.length - commandAmount);
                }
                return command.getTabCompleteList(newArgs);
            }
            return command.getTabCompleteList(args);
        }
        return new ArrayList<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
            String[] command = event.getMessage().split(" ");
            if (command.length > 0 && !command[0].equalsIgnoreCase("/ug") && !command[0].equalsIgnoreCase("/ultimategames") && !bypassers.contains(playerName)) {
                event.setCancelled(true);
                Arena arena = ultimateGames.getPlayerManager().getPlayerArena(playerName);
                String[] args;
                if (command.length == 1) {
                    args = new String[0];
                } else {
                    args = new String[command.length - 1];
                    System.arraycopy(command, 1, args, 0, command.length - 1);
                }
                arena.getGame().getGamePlugin().onArenaCommand(arena, command[0].replace("/", ""), player, args);
                event.setCancelled(true);
            }
        }
    }

    /**
     * Adds a bypasser to the arena command blocker.
     *
     * @param playerName The name of the player to add a bypass for.
     */
    public void addBlockBypasser(String playerName) {
        bypassers.add(playerName);
    }

    /**
     * Removes a bypasser from the arena command blocker.
     *
     * @param playerName The name of the player to remove the bypass from.
     */
    public void removeBlockBypasser(String playerName) {
        if (bypassers.contains(playerName)) {
            bypassers.remove(playerName);
        }
    }

    /**
     * Gets the main ultimate games command.
     *
     * @return The main command.
     */
    public Command getMainCommand() {
        return mainCommand;
    }

    /**
     * Gets the PageList of the ultimate games commands.
     *
     * @return The PageList.
     */
    public PageList getPageList() {
        return pageList;
    }
}
