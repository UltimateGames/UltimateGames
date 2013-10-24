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
package me.ampayne2.ultimategames.games;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.api.GamePlugin;
import me.ampayne2.ultimategames.enums.PlayerType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipFile;

public class GameManager {
	private UltimateGames ultimateGames;
	private Set<Game> games = new HashSet<Game>();
	private static final int BUFFER_SIZE = 1024;

	@SuppressWarnings("unchecked")
	public GameManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
		File gameFolder = new File(ultimateGames.getDataFolder(), "Games");
		if (!gameFolder.exists()) {
			gameFolder.mkdirs();
		}
		JarFile jarFile = null;
		PlayerType playerType = null;
		String name, description, author, version;
		List<String> instructionPages;
		for (File file : gameFolder.listFiles(new GameFileFilter())) {
			playerType = null;
			description = null;
			author = null;
			version = null;
			instructionPages = null;
			try {
				jarFile = new JarFile(file);

				//We load the basic game Plugin configuration
				File configFile = new File(ultimateGames.getDataFolder() + "/Games", file.getName().replace(".jar", ".yml"));
				if (!configFile.exists()) {
					ZipFile zip = new ZipFile(file);
					byte[] buffer = new byte[BUFFER_SIZE];

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
					if (!gamePlugin.contains("main-class") || !gamePlugin.contains("description") || !gamePlugin.contains("version") || !gamePlugin.contains("author") || !gamePlugin.contains("playerType")) {
						ultimateGames.getMessageManager().log(Level.SEVERE, "Game " + file.getAbsolutePath() + " contains an invalid gameplugin.yml file!");
						jarFile.close();
						continue;
					}

					name = file.getName().replace(".jar", "");
					version = gamePlugin.getString("version");
					author = gamePlugin.getString("author");
					playerType = PlayerType.valueOf(gamePlugin.getString("playerType").toUpperCase());
					instructionPages = (ArrayList<String>) gamePlugin.getList("Instructions");


					//Is the game already loaded?
					if (gameExists(name)) {
						ultimateGames.getMessageManager().log(Level.SEVERE, "The game " + name + " already exists!");
						jarFile.close();
						continue;
					}

					//We try to load the main class..
					ultimateGames.getPluginClassLoader().addURL(file.toURI().toURL());
					Class<?> aclass = ultimateGames.getPluginClassLoader().loadClass(gamePlugin.getString("main-class"));

					Object object = aclass.newInstance();
					//Is the class a valid game plugin?
					if (object instanceof GamePlugin) {
						ultimateGames.getMessageManager().log(Level.INFO, "Loading " + name);
						GamePlugin plugin = (GamePlugin) object;

						//Well, everything loaded.
						Game game = new Game(plugin, name, description, version, author, playerType, instructionPages);
						//We load the game
						plugin.loadGame(ultimateGames, game);
						addGame(game);
						ultimateGames.getServer().getPluginManager().registerEvents(plugin, ultimateGames);
					} else {
						ultimateGames.getMessageManager().log(Level.SEVERE, "The game " + name + " has an invalid main class!");
					}
				}
			} catch (Exception e) {
				ultimateGames.getMessageManager().log(Level.WARNING, "An error occurred whilst loading the game " + file.getName() + ".");
				ultimateGames.getMessageManager().debug(e);
				if (jarFile != null) {
					try {
						jarFile.close();
					} catch (IOException e1) {
						ultimateGames.getMessageManager().debug(e1);
					}
				}
			}
		}
	}

	public Set<Game> getGames() {
		return games;
	}

	public boolean gameExists(String gameName) {
		for (Game game : games) {
			if (gameName.equals(game.getName())) {
				return true;
			}
		}
		return false;
	}

	public Game getGame(String gameName) {
		for (Game game : games) {
			if (gameName.equals(game.getName())) {
				return game;
			}
		}
		return null;
	}

	public boolean addGame(Game game) {
		for (Game aGame : games) {
			if (game.getName().equals(aGame.getName())) {
				return false;
			}
		}
		games.add(game);
		ultimateGames.getConfigManager().addGameConfig(game);
		ultimateGames.getMessageManager().loadGameMessages(game);
		ultimateGames.getMetricsManager().addGame(game);
		ultimateGames.getMessageManager().log(Level.INFO, "Added game " + game.getName());
		return true;
	}

	public void disableAll() {
		//TODO: The logic
	}

	private class GameFileFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
		}
	}
}
