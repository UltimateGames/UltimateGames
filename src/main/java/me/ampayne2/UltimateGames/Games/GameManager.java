/*
 * This file is part of UltimateGames.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.UltimateGames.Games;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Enums.PlayerType;
import me.ampayne2.UltimateGames.Enums.ScoreType;

public class GameManager {

	private UltimateGames ultimateGames;
	private ArrayList<Game> games;
	private File gameFolder;

	public GameManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		games = new ArrayList<Game>();
		gameFolder = new File(ultimateGames.getPlugin().getDataFolder(), "Games");
		if (!gameFolder.exists()) {
			gameFolder.mkdirs();
		}

		for (File file : gameFolder.listFiles(new GameFileFilter())) {
			try {
				ZipFile zip = new ZipFile(file);
				File config = new File(ultimateGames.getPlugin().getDataFolder() + "/Games", file.getName().replace(".jar", "") + ".yml");
				if (!config.exists()){
					byte[] buffer = new byte[1024];
					
					FileOutputStream output = new FileOutputStream(config);
					InputStream input = zip.getInputStream(zip.getEntry("gameconfig.yml"));
					
					int realLength;

			        while ((realLength = input.read(buffer)) > 0) {
			            output.write(buffer, 0, realLength);
			        }
			 
			        output.flush();
			        output.close();
				}

				YamlConfiguration gameConfig = YamlConfiguration.loadConfiguration(config);
				
				String name = gameConfig.getString("Name");
				String description = gameConfig.getString("Description");
				String version = gameConfig.getString("Version");
				String author = gameConfig.getString("Author");
				String pack = gameConfig.getString("Package");
				String scoreTypeName = gameConfig.getString("ScoreTypeName");
				String secondaryScoreTypeName = gameConfig.getString("SecondaryScoreTypeName");
				ScoreType scoreType = ScoreType.valueOf(gameConfig.getString("ScoreType"));
				ScoreType secondaryScoreType = ScoreType.valueOf(gameConfig.getString("SecondaryScoreType"));
				PlayerType playerType = PlayerType.valueOf(gameConfig.getString("PlayerType"));
				Integer maxPlayers = gameConfig.getInt("DefaultSettings.MaxPlayers", 8);
				Boolean storeInventory = gameConfig.getBoolean("DefaultSettings.Store-Inventory", true);
				Boolean storeArmor = gameConfig.getBoolean("DefaultSettings.Store-Armor", true);
				Boolean storeExp = gameConfig.getBoolean("DefaultSettings.Store-Exp", true);
				Boolean storeEffects = gameConfig.getBoolean("DefaultSettings.Store-Effects", true);
				Boolean storeGamemode = gameConfig.getBoolean("DefaultSettings.Store-Gamemode", true);
				Boolean resetAfterMatch = gameConfig.getBoolean("DefaultSettings.Reset-After-Match", true);
				Boolean allowExplosionDamage = gameConfig.getBoolean("DefaultSettings.Allow-Explosion-Damage", true);
				Boolean allowExplosionBlockBreaking = gameConfig.getBoolean("DefaultSettings.Allow-Explosion-Block-Breaking", false);
				Boolean allowBuilding = gameConfig.getBoolean("DefaultSettings.Allow-Building", false);
				Boolean allowBreaking = gameConfig.getBoolean("DefaultSettings.Allow-Breaking", false);
				
				if (!gameExists(name)) {
					FileConfiguration games = ultimateGames.getConfigManager().getGamesConfig().getConfig();
					String gamePath = "Games."+name;
					if (!games.contains(gamePath)) {
						games.set(gamePath + ".enabled", true);
						games.set(gamePath + ".DefaultSettings.MaxPlayers", maxPlayers);
						games.set(gamePath + ".DefaultSettings.Store-Inventory", storeInventory);
						games.set(gamePath + ".DefaultSettings.Store-Armor", storeArmor);
						games.set(gamePath + ".DefaultSettings.Store-Exp", storeExp);
						games.set(gamePath + ".DefaultSettings.Store-Effects", storeEffects);
						games.set(gamePath + ".DefaultSettings.Store-Gamemode", storeGamemode);
						games.set(gamePath + ".DefaultSettings.Reset-After-Match", resetAfterMatch);
						games.set(gamePath + ".DefaultSettings.Allow-Explosion-Damage", allowExplosionDamage);
						games.set(gamePath + ".DefaultSettings.Allow-Explosion-Block-Breaking", allowExplosionBlockBreaking);
						games.set(gamePath + ".DefaultSettings.Allow-Building", allowBuilding);
						games.set(gamePath + ".DefaultSettings.Allow-Breaking", allowBreaking);
						ultimateGames.getConfigManager().getGamesConfig().saveConfig();
					}
					if (games.getBoolean("Games." + name + ".enabled", false)) {
						GameDescription gameDescription = new GameDescription(name, description, version, author, pack, scoreTypeName, secondaryScoreTypeName, scoreType, secondaryScoreType, playerType);
						Game game = new Game(file, gameDescription);
						addGame(game);
					}
				} else {
					ultimateGames.getMessageManager().log(Level.WARNING, "The game " + name + " failed to load. A game with the same name is already loaded.");
				}
				
				
				

				zip.close();
			} catch (Exception e) {
				ultimateGames.getMessageManager().log(Level.WARNING, "An error occurred whilst loading the game " + file.getName() + ".");
				ultimateGames.getMessageManager().debug(e);
			}
		}
	}

	public boolean gameExists(String gameName) {
		for (Game game : games) {
			if (gameName.equals(game.getGameDescription().getName())) {
				return true;
			}
		}
		return false;
	}

	public Game getGame(String gameName) {
		for (Game game : games) {
			if (gameName.equals(game.getGameDescription().getName())) {
				return game;
			}
		}
		return null;
	}

	public void addGame(Game game) {
		for (Game aGame : games) {
			if (game.getGameDescription().getName().equals(aGame.getGameDescription().getName())) {
				return;
			}
		}
		games.add(game);
		ultimateGames.getConfigManager().addGameConfig(game);
		ultimateGames.getMessageManager().log(Level.INFO, "added game " + game.getGameDescription().getName());
	}

	private class GameFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
		}

	}
}
