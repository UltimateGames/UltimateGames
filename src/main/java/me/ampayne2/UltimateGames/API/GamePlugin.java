package me.ampayne2.UltimateGames.API;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;

public abstract class GamePlugin implements Listener{

	private UltimateGames ultimateGames;
	
	public GamePlugin(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	public abstract Boolean loadGame();
	public abstract Boolean unloadGame();
	public abstract Boolean stopGame();
	public abstract Boolean loadArena(Arena arena);
	public abstract Boolean unloadArena(Arena arena);
	public abstract Boolean changeArenaStatus(ArenaStatus status);
	public abstract Boolean onPlayerJoin(String playerName);
	public abstract Boolean onPlayerLeave(String playerName);
	public abstract void onGameCommand(String command, CommandSender sender, String[] args);
	public abstract void onArenaCommand(String command, CommandSender sender, String[] args);
}
