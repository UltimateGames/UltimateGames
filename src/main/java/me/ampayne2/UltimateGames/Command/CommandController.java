package me.ampayne2.UltimateGames.Command;

import me.ampayne2.UltimateGames.Command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin{

	private final SubCommand mainCommand = new SubCommand();
	
	public CommandController() {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!cmd.getName().equalsIgnoreCase("ultimategames")){
			return false;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		}

		if (args.length == 0) {
			if (mainCommand.commandExist("")) {
				if (mainCommand.hasCommandPermission(sender, "")) {
					mainCommand.execute("", sender, args);
				} else {
					//No Permission for command /ultimategames
				}
				return true;
			} else {
				//Invalid Argument. Valid arguments are mainCommand.getSubCommandList());
				return true;
			}
		}

		if (mainCommand.commandExist(args[0])) {
			if (mainCommand.hasCommandPermission(sender, args[0])) {
				String[] newArgs;
				if (args.length == 1) {
					newArgs = new String[0];
				} else {
					newArgs = new String[args.length - 1];
					System.arraycopy(args, 1, newArgs, 0, args.length - 1);
				}

				mainCommand.execute(args[0], sender, newArgs);
			} else {
				//No Permission for command /ultimategames arg[0]
			}
			return true;
		} else {
			//Invalid Arguments. Valid arguments are mainCommand.getSubCommandList());
			return true;
		}
		
	}
}
