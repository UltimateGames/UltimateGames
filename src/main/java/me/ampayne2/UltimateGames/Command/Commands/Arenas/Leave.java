package me.ampayne2.UltimateGames.Command.Commands.Arenas;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;

public class Leave implements UGCommand{
	
	private UltimateGames ultimateGames;
	
	public Leave(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String playerName = player.getName();
		if (ultimateGames.getPlayerManager().isPlayerInArena(playerName)) {
			ultimateGames.getPlayerManager().removePlayerFromArena(playerName, ultimateGames.getPlayerManager().getPlayerArena(playerName), true);
		}
	}

}
