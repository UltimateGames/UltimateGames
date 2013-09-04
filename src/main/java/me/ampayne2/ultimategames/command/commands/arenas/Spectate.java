package me.ampayne2.ultimategames.command.commands.arenas;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.command.interfaces.UGCommand;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.players.QueueManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spectate implements UGCommand {
    
    private UltimateGames ultimateGames;

    public Spectate(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String arenaName = args[0];
        String gameName = args[1];
        if (ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            Arena arena = ultimateGames.getArenaManager().getArena(arenaName, gameName);
            Player player = (Player) sender;
            String playerName = player.getName();
            if (!ultimateGames.getPlayerManager().isPlayerInArena(playerName) && !ultimateGames.getPlayerManager().isPlayerSpectatingArena(playerName)) {
                ArenaStatus arenaStatus = arena.getStatus();
                if (arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING || arenaStatus == ArenaStatus.RUNNING) {
                    // TODO: Save and clear player data (inventory, armor, levels, gamemode, effects)
                    ultimateGames.getPlayerManager().addSpectatorToArena(player, arena);
                    return;
                } else if (arenaStatus == ArenaStatus.ENDING || arenaStatus == ArenaStatus.RESETTING || arena.getPlayers().size() >= arena.getMaxPlayers()) {
                    QueueManager queue = ultimateGames.getQueueManager();
                    if (!queue.isPlayerInQueue(playerName, arena)) {
                        queue.addPlayerToQueue(player, arena);
                    }
                }
            }
        }
    }

}
