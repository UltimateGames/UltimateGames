package me.ampayne2.UltimateGames.Command.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;

public class SetLobby implements UGCommand{

	private UltimateGames ultimateGames;
	
	public SetLobby(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			return;
		}
		Player player = (Player) sender;
		ultimateGames.getLobbyManager().setLobby(player.getLocation());
	}

}
