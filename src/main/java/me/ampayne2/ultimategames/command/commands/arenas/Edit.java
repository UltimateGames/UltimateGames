package me.ampayne2.ultimategames.command.commands.arenas;

import org.bukkit.command.CommandSender;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;
import me.ampayne2.ultimategames.players.ArenaPlayer;

public class Edit implements UGCommand {
    
    private UltimateGames ultimateGames;
    
    public Edit(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 0) {
            ultimateGames.getMessageManager().sendMessage(sender.getName(), "commandusages.arena.edit");
            return;
        }
        String senderName = sender.getName();
        if (ultimateGames.getPlayerManager().isPlayerInArena(senderName)) {
            ArenaPlayer player = ultimateGames.getPlayerManager().getArenaPlayer(senderName);
            if (player.isEditing()) {
                player.setEditing(false);
                ultimateGames.getMessageManager().sendMessage(sender.getName(), "arenas.editoff");
            } else {
                player.setEditing(true);
                ultimateGames.getMessageManager().sendMessage(sender.getName(), "arenas.editon");
            }
        } else {
            ultimateGames.getMessageManager().sendMessage(sender.getName(), "arenas.notinarena");
        }
    }

}
