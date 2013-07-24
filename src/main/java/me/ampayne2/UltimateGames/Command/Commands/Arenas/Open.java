package me.ampayne2.UltimateGames.Command.Commands.Arenas;

import org.bukkit.command.CommandSender;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;

public class Open implements UGCommand{

	private UltimateGames ultimateGames;
	
	public Open(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 2) {
			return;
		}
		String arenaName = args[0];
		String gameName = args[1];
		if (ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
			Arena arena = ultimateGames.getArenaManager().getArena(arenaName, gameName);
			ultimateGames.getArenaManager().openArena(arena);
		}
	}

}
