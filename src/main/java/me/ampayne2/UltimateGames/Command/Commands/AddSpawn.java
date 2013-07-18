package me.ampayne2.UltimateGames.Command.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;
import me.ampayne2.UltimateGames.Players.SpawnPoint;

public class AddSpawn implements UGCommand{

	private UltimateGames ultimateGames;
	
	public AddSpawn(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 3 || !(args[2].equals("true") || args[2].equals("false"))) {
			return;
		}
		String arenaName = args[0];
		String gameName = args[1];
		Boolean locked = Boolean.valueOf(args[2]);
		if (!ultimateGames.getGameManager().gameExists(gameName) || !ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
			return; //game or arena doesn't exist
		}
		Player player = (Player) sender;
		SpawnPoint spawnPoint = ultimateGames.getSpawnpointManager().createSpawnPoint(ultimateGames.getArenaManager().getArena(arenaName, gameName), player.getLocation(), locked);
		ultimateGames.getSpawnpointManager().addSpawnPoint(spawnPoint);
	}

}
