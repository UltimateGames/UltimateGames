package me.ampayne2.UltimateGames.Command.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.ampayne2.UltimateGames.Command.interfaces.UGCommand;
import me.ampayne2.UltimateGames.Utils.ParticleEffect;

public class SpawnParticle implements UGCommand{

	@Override
	public void execute(CommandSender sender, String[] args) {
		ParticleEffect particleEffect = ParticleEffect.valueOf(args[0]);
		Player player = (Player) sender;
		//offset x offset y offset z speed amount
		particleEffect.play(player.getLocation().add(new Vector(1, 1, 1)), Float.valueOf(args[1]), Float.valueOf(args[2]), Float.valueOf(args[3]), Float.valueOf(args[4]), Integer.valueOf(args[5]));
	}

}
