package me.ampayne2.UltimateGames.Command.Commands;

import org.bukkit.command.CommandSender;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;

public class Start implements UGCommand{

	private UltimateGames ultimateGames;
	
	public Start(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 2) {
			return;
		}
		String gameName = args[1];
		String arenaName = args[0];
		if (ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
			Arena arena = ultimateGames.getArenaManager().getArena(arenaName, gameName);
			if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
				ultimateGames.getCountdownManager().createStartingCountdown(arena, 5);
			}
		}
	}

}
