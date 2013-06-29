package me.ampayne2.UltimateGames.LobbySigns;

import java.util.ArrayList;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Enums.ArenaStatus;
import me.ampayne2.UltimateGames.Games.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

public class LobbySignManager {

	private UltimateGames ultimateGames;
	private ArrayList<LobbySign> lobbySigns;

	public LobbySignManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		lobbySigns = new ArrayList<LobbySign>();
		FileConfiguration lobbySignConfig = ultimateGames.getConfigManager().getLobbySignConfig().getConfig();
		if (lobbySignConfig.getConfigurationSection("LobbySigns") == null) {
			return;
		}
		for (String gameKey : lobbySignConfig.getConfigurationSection("LobbySigns").getKeys(false)) {
			if (ultimateGames.getGameManager().gameExists(gameKey)) {
				for (String arenaKey : lobbySignConfig.getConfigurationSection("LobbySigns." + gameKey).getKeys(false)) {
					if (ultimateGames.getArenaManager().arenaExists(arenaKey, gameKey)) {
						for (String lobbySignKey : lobbySignConfig.getConfigurationSection("LobbySigns."+gameKey+"."+arenaKey).getKeys(false)) {
							String world = lobbySignConfig.getString("LobbySigns."+gameKey+"."+arenaKey+"."+lobbySignKey+".world");
							Integer x = lobbySignConfig.getInt("LobbySigns."+gameKey+"."+arenaKey+"."+lobbySignKey+".x");
							Integer y = lobbySignConfig.getInt("LobbySigns."+gameKey+"."+arenaKey+"."+lobbySignKey+".y");
							Integer z = lobbySignConfig.getInt("LobbySigns."+gameKey+"."+arenaKey+"."+lobbySignKey+".z");
							if (world != null && x != null && y != null && z != null) {
								World signWorld;
								if ((signWorld = Bukkit.getWorld(world)) != null) {
									Block locBlock = new Location(signWorld, x, y, z).getBlock();
									if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
										addLobbySign(new LobbySign((Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
									}
								}
							}
						}
					}
				}
			}
		}
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
	
	public void addLobbySign(LobbySign lobbySign) {
		if (!lobbySigns.contains(lobbySign)) {
			lobbySigns.add(lobbySign);
		}
	}

	public LobbySign createLobbySign(Sign sign, Arena arena) {
		if (isLobbySign(sign)) {
			// already a lobby sign here
			return null;
		}
		// lobby sign created
		LobbySign lobbySign = new LobbySign(sign, arena);
		addLobbySign(lobbySign);
		
		FileConfiguration lobbySignConfig = ultimateGames.getConfigManager().getLobbySignConfig().getConfig();
		Integer signIndex;
		if (lobbySignConfig.contains("LobbySigns."+arena.getGame().getGameDescription().getName()+"."+arena.getName())) {
			signIndex = lobbySignConfig.getConfigurationSection("LobbySigns."+arena.getGame().getGameDescription().getName()+"."+arena.getName()).getKeys(false).size() + 1;
		} else {
			signIndex = 1;
			lobbySignConfig.createSection("LobbySigns."+arena.getGame().getGameDescription().getName()+"."+arena.getName());
		}
		lobbySignConfig.set("LobbySigns."+arena.getGame().getGameDescription().getName()+"."+arena.getName()+"."+signIndex+".world", sign.getWorld().getName());
		lobbySignConfig.set("LobbySigns."+arena.getGame().getGameDescription().getName()+"."+arena.getName()+"."+signIndex+".x", sign.getX());
		lobbySignConfig.set("LobbySigns."+arena.getGame().getGameDescription().getName()+"."+arena.getName()+"."+signIndex+".y", sign.getY());
		lobbySignConfig.set("LobbySigns."+arena.getGame().getGameDescription().getName()+"."+arena.getName()+"."+signIndex+".z", sign.getZ());
		ultimateGames.getConfigManager().getLobbySignConfig().saveConfig();
		
		return lobbySign;
	}

	public boolean removeLobbySign(Sign sign) {
		if (isLobbySign(sign)) {
			FileConfiguration lobbySignConfig = ultimateGames.getConfigManager().getLobbySignConfig().getConfig();
			String world = sign.getWorld().getName();
			Integer x = sign.getX();
			Integer y = sign.getY();
			Integer z = sign.getZ();
			LobbySign lobbySign = getLobbySign(sign);
			if (lobbySignConfig.contains("LobbySigns."+lobbySign.getArena().getGame().getGameDescription().getName()+"."+lobbySign.getArena().getName())) {
				for (String signKey : lobbySignConfig.getConfigurationSection("LobbySigns."+lobbySign.getArena().getGame().getGameDescription().getName()+"."+lobbySign.getArena().getName()).getKeys(false)) {
					String sectionPath = "LobbySigns."+lobbySign.getArena().getGame().getGameDescription().getName()+"."+lobbySign.getArena().getName()+"."+signKey;
					if (lobbySignConfig.getString(sectionPath+".world").equals(world) && lobbySignConfig.getInt(sectionPath+".x") == x && lobbySignConfig.getInt(sectionPath+".y") == y && lobbySignConfig.getInt(sectionPath+".z") == z) {
						lobbySignConfig.set(sectionPath, null);
					}
				}
			}
			
			
			lobbySigns.remove(lobbySign);
			return true;
		} else {
			return false;
			// isn't lobby sign
		}
	}

}
