package me.ampayne2.UltimateGames.Listeners;

import me.ampayne2.UltimateGames.LobbySign;
import me.ampayne2.UltimateGames.UltimateGames;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
			if(!ultimateGames.getGameManager().gameExists(gameName) || !ultimateGames.getArenaManager().arenaExists(arenaName)){
				//game or arena doesn't exist, do stuff
				return;
			}
			//adds the sign to the LobbySignManager
			ultimateGames.getLobbySignManager().createLobbySign((Sign) event.getBlock().getState(), ultimateGames.getGameManager().getGame(gameName), ultimateGames.getArenaManager().getArena(arenaName));
		}
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
		//checks to see if right click
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		//checks to see if clicked block is a sign
		}else if (event.getClickedBlock().getType() != Material.WALL_SIGN || event.getClickedBlock().getType() != Material.SIGN_POST) {
			return;
		//checks to see if clicking player has permission
		}else if (!event.getPlayer().hasPermission("permission for clicking signs")) {
			return;
		//checks to see if the clicked sign is a LobbySign
		}else if (!ultimateGames.getLobbySignManager().isLobbySign((Sign) event.getClickedBlock().getState())) {
			return;
		}
		
		
		//check if arena is open
		//save player inventory to yml
		//clear player inventory
		//fire GameJoinEvent
	}
	
	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() != Material.WALL_SIGN || event.getBlock().getType() != Material.SIGN_POST) {
			return;
		}
		Sign sign = (Sign) event.getBlock().getState();
		if (!ultimateGames.getLobbySignManager().isLobbySign(sign)) {
			return;
		}else if (!event.getPlayer().hasPermission("permission for breaking lobby signs")) {
			event.setCancelled(true);
			return;
		}
		ultimateGames.getLobbySignManager().removeLobbySign(sign);
	}

}
