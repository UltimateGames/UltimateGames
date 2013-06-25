package me.ampayne2.UltimateGames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import me.ampayne2.UltimateGames.Games.Arena;
import me.ampayne2.UltimateGames.Games.Game;

import org.bukkit.Location;
import org.bukkit.block.Sign;

public class LobbySignManager {

	private UltimateGames ultimateGames;
	private ArrayList<LobbySign> lobbySigns;

	public LobbySignManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	public boolean isLobbySign(Sign sign) {
		Iterator<LobbySign> it = lobbySigns.iterator();
		while (it.hasNext()) {
			if (sign.getLocation().equals(it.next().getSign().getLocation())) {
				return true;
			}
		}
		return false;
	}

	public void createLobbySign(Sign sign, Game game, Arena arena) {
		Iterator<LobbySign> it = lobbySigns.iterator();
		while (it.hasNext()) {
			LobbySign nextLobbySign = it.next();
			if (sign.getLocation().equals(nextLobbySign.getSign().getLocation())) {
				// already a lobby sign here
			}
		}
		lobbySigns.add(new LobbySign(sign, game, arena));
	}

	public void removeLobbySign(Sign sign) {
		Iterator<LobbySign> it = lobbySigns.iterator();
		while (it.hasNext()) {
			LobbySign nextLobbySign = it.next();
			if (sign.getLocation().equals(nextLobbySign.getSign().getLocation())){
				it.remove();
			}
		}
	}
	
	
	/*
	 * public HashMap<Location, LobbySign> getLobbySigns() { return lobbySigns;
	 * } public void updateSigns() { } public void updateSign() { }
	 */

}
