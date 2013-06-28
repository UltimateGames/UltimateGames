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
					InputStream input = zip.getInputStream(zip.getEntry("config.yml"));
					
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
				
				if (!gameExists(name)) {
					FileConfiguration games = ultimateGames.getConfigManager().getGamesConfig().getConfig();
					if (!games.contains("Games." + name)) {
						games.createSection("Games." + name);
						games.createSection("Games." + name + ".enabled");
						games.set("Games." + name + ".enabled", true);
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
