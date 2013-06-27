package me.ampayne2.UltimateGames.LobbySigns;

import java.util.ArrayList;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Games.Game;

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
	
	public ArrayList<LobbySign> getLobbySignsOfArena(Arena arena) {
		ArrayList<LobbySign> arenaSigns = new ArrayList<LobbySign>();
		for (LobbySign lobbySign : lobbySigns) {
			if (arena.equals(lobbySign.getArena())) {
				arenaSigns.add(lobbySign);
			}
		}
		if (arenaSigns.isEmpty()) {
			return null;
		} else {
			return arenaSigns;
		}
	}
	
	public ArrayList<LobbySign> getLobbySignsOfGame(Game game) {
		ArrayList<LobbySign> gameSigns = new ArrayList<LobbySign>();
		for (LobbySign lobbySign : lobbySigns) {
			if (game.equals(lobbySign.getArena().getGame())) {
				gameSigns.add(lobbySign);
			}
		}
		if (gameSigns.isEmpty()) {
			return null;
		} else {
			return gameSigns;
		}
	}
	
	public void updateLobbySignsOfArena(Arena arena) {
		ArrayList<LobbySign> arenaSigns = getLobbySignsOfArena(arena);
		for (LobbySign lobbySign : arenaSigns) {
			lobbySign.update();
		}
	}
	
	public void updateLobbySignsOfGame(Game game) {
		ArrayList<LobbySign> gameSigns = getLobbySignsOfGame(game);
		for (LobbySign lobbySign : gameSigns) {
			lobbySign.update();
		}
	}

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

}
