package me.ampayne2.ultimategames.command.commands;

import org.bukkit.command.CommandSender;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;

public class Reload implements UGCommand {
    
    private UltimateGames ultimateGames;
    
    public Reload(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ultimateGames.getManager().reloadManagers();
    }

}
