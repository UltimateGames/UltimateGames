package me.ampayne2.UltimateGames.Listeners;

import java.util.logging.Level;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Events.GameJoinEvent;
import me.ampayne2.UltimateGames.LobbySigns.LobbySign;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

	private UltimateGames ultimateGames;

	public SignListener(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		if (!event.getPlayer().hasPermission("permission for setting signs")) {
			//player doesn't have permission, do stuff
			return;
		}
		//if first line is prefix, and 2nd/3rd line aren't empty
		if (event.getLine(0).equals(ultimateGames.getConfig().getString("SignPrefix")) && !event.getLine(1).isEmpty() && !event.getLine(2).isEmpty() && event.getLine(3).isEmpty()) {
			String gameName = event.getLine(1);
			String arenaName = event.getLine(2);
			if(!ultimateGames.getGameManager().gameExists(gameName) || !ultimateGames.getArenaManager().arenaExists(arenaName, gameName)){
				//game or arena doesn't exist, do stuff
				return;
			}
			//adds the sign to the LobbySignManager
			LobbySign lobbySign = ultimateGames.getLobbySignManager().createLobbySign((Sign) event.getBlock().getState(), ultimateGames.getArenaManager().getArena(arenaName, gameName));
			String[] lines = lobbySign.getUpdatedLines();
			for (int i = 0; i < 4; i++) {
				event.setLine(i, lines[i]);
			}
		}
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			//not right click
			return;
		}else if (event.getClickedBlock().getType() != Material.WALL_SIGN && event.getClickedBlock().getType() != Material.SIGN_POST) {
			//not a sign
			return;
		}else if (!event.getPlayer().hasPermission("permission for right clicking lobby signs")) {
			//no permission
			return;
		}else if (!ultimateGames.getLobbySignManager().isLobbySign((Sign) event.getClickedBlock().getState())) {
			//not a lobby sign
			return;
		}
		//gets the lobby sign clicked
		LobbySign lobbySign = ultimateGames.getLobbySignManager().getLobbySign((Sign) event.getClickedBlock().getState());
		if (lobbySign.getArena().getStatus() != ArenaStatus.OPEN) {
			//arena not open
			return;
		}
		
		//TODO: Save and clear player data (inventory, armor, levels, gamemode, effects)
		
		//fires a GameJoinEvent
		GameJoinEvent gameJoinEvent = new GameJoinEvent(event.getPlayer(), lobbySign.getArena());
		Bukkit.getServer().getPluginManager().callEvent(gameJoinEvent);
		ultimateGames.getMessageManager().log(Level.INFO, "gameJoinEvent fired for game '"+gameJoinEvent.getArena().getGame().getGameDescription().getName()+"' and arena '"+gameJoinEvent.getArena().getName()+"'.");
	}
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.WALL_SIGN && event.getBlock().getType() != Material.SIGN_POST) {
			//not a sign
			return;
		}
		Sign sign = (Sign) event.getBlock().getState();
		if (!ultimateGames.getLobbySignManager().isLobbySign(sign)) {
			//not a lobby sign
			return;
		}else if (!event.getPlayer().hasPermission("permission for breaking lobby signs")) {
			//no permission to break lobby signs
			event.setCancelled(true);
			return;
		}
		//removes sign
		ultimateGames.getLobbySignManager().removeLobbySign(sign);
	}

}
