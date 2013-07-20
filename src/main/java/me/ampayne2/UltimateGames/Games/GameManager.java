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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.YamlConfiguration;

import me.ampayne2.UltimateGames.API.GamePlugin;
import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Enums.PlayerType;
import me.ampayne2.UltimateGames.Enums.ScoreType;

public class GameManager {
	
	private UltimateGames ultimateGames;
	private ArrayList<Game> games = new ArrayList<Game>();
	private File gameFolder;

	@SuppressWarnings("unchecked")
	public GameManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;


		gameFolder = new File(ultimateGames.getPlugin().getDataFolder(), "Games");
		if (!gameFolder.exists()) {
			gameFolder.mkdirs();
		}

		ScoreType scoreType = null, secondaryScoreType = null;
		PlayerType playerType = null;
		JarFile jarFile = null;
		String description, author, version, secondaryScoreTypeName, scoreTypeName;
		ArrayList<String> instructionPages;
		for (File file : gameFolder.listFiles(new GameFileFilter())) {
			scoreType = null;
			secondaryScoreType = null;
			playerType = null;
			description = null;
			author = null;
			version = null;
			secondaryScoreTypeName = null;
			scoreTypeName = null;
			instructionPages = null;
			try {

				jarFile = new JarFile(file);

				//We load the basic game Plugin configuration
				File configFile = new File(ultimateGames.getPlugin().getDataFolder() + "/Games", file.getName().replace(".jar", ".yml"));
				if (!configFile.exists()){
					ZipFile zip = new ZipFile(file);
					byte[] buffer = new byte[1024];
					
					FileOutputStream output = new FileOutputStream(configFile);
					InputStream input = zip.getInputStream(zip.getEntry("gameplugin.yml"));
					
					int realLength;

			        while ((realLength = input.read(buffer)) > 0) {
			            output.write(buffer, 0, realLength);
			        }
			 
			        output.flush();
			        output.close();
			        zip.close();
				}

				//Does the config file exist?
				if (configFile != null) {
					YamlConfiguration gamePlugin = YamlConfiguration.loadConfiguration(configFile);

					//Is the configuration a valid one?
					if (!gamePlugin.contains("main-class") || !gamePlugin.contains("description") || !gamePlugin.contains("version") || !gamePlugin.contains("author") || !gamePlugin.contains("scoreTypeName") || !gamePlugin.contains("playerType")) {
						ultimateGames.getMessageManager().log(Level.SEVERE, "Game " + file.getAbsolutePath() + " contains an invalid gameplugin.yml file!");
						jarFile.close();
						continue;
					}

					//We do have a main score type. Let's see if it's a valid one..
					if (gamePlugin.contains("scoreType")) {
						scoreType = ScoreType.valueOf(gamePlugin.getString("scoreType").toUpperCase());
						scoreTypeName = gamePlugin.getString("scoreTypeName");
					} else {
						ultimateGames.getMessageManager().log(Level.SEVERE, "Game " + file.getAbsolutePath() + " contains an invalid gameplugin.yml file!");
						jarFile.close();
						continue;
					}

					//We check if we have a secondary score
					if (gamePlugin.contains("secondaryScoreTypeName") && gamePlugin.contains("secondaryScoreType")) {
						secondaryScoreType = ScoreType.valueOf(gamePlugin.getString("secondaryScoreType").toUpperCase());
						secondaryScoreTypeName = gamePlugin.getString("secondaryScoreTypeName");
					}

					//We retrieve the PlayerType
					playerType = PlayerType.valueOf(gamePlugin.getString("playerType").toUpperCase());
					
					//We retrieve the instruction pages
					instructionPages = (ArrayList<String>) gamePlugin.getList("Instructions");
					
					version = gamePlugin.getString("version");
					String gameName = file.getName().replace(".jar", "");
					
					//Is the game already loaded?
					if (gameExists(gameName)) {
						ultimateGames.getMessageManager().log(Level.SEVERE, "The game " + gameName + " already exists!");
						jarFile.close();
						continue;
					}
					
					//We try to load the main class..
					URLClassLoader loader = new URLClassLoader(new URL[] {file.toURI().toURL()}, this.getClass().getClassLoader());
					Class<?> aclass = loader.loadClass(gamePlugin.getString("main-class"));
					Object object = aclass.newInstance();
					loader.close();
					//Is the class a valid game plugin?
					if (object instanceof GamePlugin) {
						ultimateGames.getMessageManager().log(Level.INFO, "Loading " + gameName);
						GamePlugin plugin = (GamePlugin) object;

						//Well, everything loaded.
						GameDescription gameDescription = new GameDescription(gameName, description, version, author, scoreTypeName, secondaryScoreTypeName, scoreType, secondaryScoreType, playerType, instructionPages);
						Game game = new Game(plugin, gameDescription);
						//We load the game
						plugin.loadGame(ultimateGames, game);
						addGame(game);
						ultimateGames.getPlugin().getServer().getPluginManager().registerEvents(plugin, ultimateGames);
					} else {
						ultimateGames.getMessageManager().log(Level.SEVERE, "The game " + gameName + " has an invalid main class!");
					}
				}
			} catch (Exception e) {
				ultimateGames.getMessageManager().log(Level.WARNING, "An error occurred whilst loading the game " + file.getName() + ".");
				e.printStackTrace();
				if (jarFile != null) {
					try {
						jarFile.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	public ArrayList<Game> getGames() {
		return games;
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
		ultimateGames.getMessageManager().log(Level.INFO, "Added game " + game.getGameDescription().getName());
	}

	public void disableAll() {
		//TODO: The logic
		//for ()
	}
	
	private class GameFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
		}

	}
}
