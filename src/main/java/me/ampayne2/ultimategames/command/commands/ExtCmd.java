package me.ampayne2.ultimategames.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;

public class ExtCmd implements UGCommand {
    
    private UltimateGames ultimateGames;

    public ExtCmd(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || args.length == 0) {
            return;
        }
        Player player = (Player) sender;
        if (ultimateGames.getPlayerManager().isPlayerInArena(player.getName())) {
            String command = args[0];
            if (args.length != 1) {
                for (int i=1; i < args.length; i++) {
                    command = command + " " + args[i];
                }
            }
            ultimateGames.getCommandController().addBlockBypasser(player.getName());
            player.performCommand(command);
            ultimateGames.getCommandController().removeBlockBypasser(player.getName());
        }
    }

}
