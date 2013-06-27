package me.ampayne2.UltimateGames;

import java.util.ArrayList;

import me.ampayne2.UltimateGames.Games.Arena;
import org.bukkit.block.Sign;

public class LobbySignManager {

	private UltimateGames ultimateGames;
	private ArrayList<LobbySign> lobbySigns;

	public LobbySignManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		lobbySigns = new ArrayList<LobbySign>();
	}

	public boolean isLobbySign(Sign sign) {
		if (getLobbySign(sign) == null) {
			return false;
		} else {
			return true;
		}
	}

	public LobbySign getLobbySign(Sign sign) {
		for (LobbySign ssign : lobbySigns) {
			if (sign.equals(ssign.getSign())) {
				return ssign;
			}
		}
		return null;
	}
	
	/*
	public LobbySign[] getLobbySignsOfArena(Arena arena) {
	
	}
	
	public LobbySign[] getLobbySignsOfGame(Game Game) {
	
	}
	*/

	public LobbySign createLobbySign(Sign sign, Arena arena) {
		if (isLobbySign(sign)) {
			// already a lobby sign here
			return null;
		}
		// lobby sign created
		LobbySign lobbySign = new LobbySign(sign, arena);
		lobbySigns.add(lobbySign);
		return lobbySign;
	}

	public boolean removeLobbySign(Sign sign) {
		if (isLobbySign(sign) && lobbySigns.remove(getLobbySign(sign))) {
			return true;
		} else {
			return false;
			// isn't lobby sign
		}
	}

	/*
	 * public HashMap<Location, LobbySign> getLobbySigns() { return lobbySigns;
	 * } public void updateSigns() { } public void updateSign() { }
	 */

}
