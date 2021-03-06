/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.games;

import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.GameManager;
import me.ampayne2.ultimategames.api.games.GamePlugin;
import me.ampayne2.ultimategames.api.games.PlayerType;
import me.ampayne2.ultimategames.api.message.Messenger;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.zip.ZipFile;

/**
 * Manages all of the ultimate games games.
 */
public class UGameManager implements GameManager {
    private final UG ultimateGames;
    private Set<Game> games = new HashSet<>();
    private static final int BUFFER_SIZE = 1024;

    /**
     * Creates a new GameManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    @SuppressWarnings("unchecked")
    public UGameManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        File gameFolder = new File(ultimateGames.getDataFolder(), "Games");
        if (!gameFolder.exists() && !gameFolder.mkdirs()) {
            return;
        }
        JarFile jarFile = null;
        PlayerType playerType;
        String name, description, author, version;
        List<String> instructionPages;
        Messenger messenger = ultimateGames.getMessenger();
        GameClassLoader pluginClassLoader = new GameClassLoader(ultimateGames.getClass().getClassLoader());
        PluginManager pluginManager = ultimateGames.getServer().getPluginManager();
        for (File file : gameFolder.listFiles(new GameFileFilter())) {
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

                YamlConfiguration gamePlugin = YamlConfiguration.loadConfiguration(configFile);

                //Is the configuration a valid one?
                if (!gamePlugin.contains("main-class") || !gamePlugin.contains("description") || !gamePlugin.contains("version") || !gamePlugin.contains("author") || !gamePlugin.contains("playerType")) {
                    messenger.log(Level.SEVERE, "Game " + file.getAbsolutePath() + " contains an invalid gameplugin.yml file!");
                    jarFile.close();
                    continue;
                }

                name = file.getName().replace(".jar", "");
                description = gamePlugin.getString("description");
                version = gamePlugin.getString("version");
                author = gamePlugin.getString("author");
                playerType = PlayerType.valueOf(gamePlugin.getString("playerType").toUpperCase());
                instructionPages = gamePlugin.getStringList("Instructions");
                List<String> depend = gamePlugin.contains("depend") ? gamePlugin.getStringList("depend") : new ArrayList<String>();

                //We try to load the main class..
                pluginClassLoader.addURL(file.toURI().toURL());
                Class<?> aclass = pluginClassLoader.loadClass(gamePlugin.getString("main-class"));

                Object object = aclass.newInstance();

                //Is the class a valid game plugin?
                if (object instanceof GamePlugin) {
                    messenger.log(Level.INFO, "Loading game " + name);
                    GamePlugin plugin = (GamePlugin) object;

                    Game game = new UGame(plugin, name, description, version, author, depend, playerType, instructionPages);

                    // Does the game already exist?
                    if (gameExists(game)) {
                        messenger.log(Level.SEVERE, "The game " + name + " already exists!");
                        jarFile.close();
                        continue;
                    }

                    // Check if dependencies are loaded
                    List<String> dependenciesNotLoaded = new ArrayList<>();
                    for (String dependency : game.getDepend()) {
                        if (!pluginManager.isPluginEnabled(dependency)) {
                            dependenciesNotLoaded.add(dependency);
                        }
                    }

                    //We load the game
                    if (!dependenciesNotLoaded.isEmpty()) {
                        for (String dependency : dependenciesNotLoaded) {
                            messenger.log(Level.SEVERE, name + " is missing the dependency " + dependency);
                        }
                    } else if (plugin.loadGame(ultimateGames, game)) {
                        addGame(game);
                        pluginManager.registerEvents(plugin, ultimateGames);
                    }
                } else {
                    messenger.log(Level.SEVERE, name + " has an invalid main class!");
                }
            } catch (Exception e) {
                messenger.log(Level.WARNING, "An error occurred whilst loading the game " + file.getName() + ".");
                messenger.debug(e);
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (IOException e1) {
                        messenger.debug(e1);
                    }
                }
            }
        }
    }

    @Override
    public Set<Game> getGames() {
        return games;
    }

    @Override
    public boolean gameExists(String gameName) {
        for (Game game : games) {
            if (game.getName().equalsIgnoreCase(gameName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean gameExists(Game game) {
        return games.contains(game);
    }

    @Override
    public Game getGame(String gameName) {
        for (Game game : games) {
            if (gameName.equalsIgnoreCase(game.getName())) {
                return game;
            }
        }
        return null;
    }

    /**
     * Adds a game to the manager.
     *
     * @param game The game to add.
     * @return True if the game wasn't already loaded, else false.
     */
    public boolean addGame(Game game) {
        if (gameExists(game)) {
            return false;
        } else {
            games.add(game);
            ultimateGames.getConfigManager().addGameConfig(game);
            if (game.getMessages() != null) {
                ultimateGames.getMessenger().loadGameMessages(game, game.getMessages());
            }
            ultimateGames.getMetricsManager().addGame(game);
            ultimateGames.getMessenger().log(Level.INFO, "Added game " + game.getName());
            return true;
        }
    }

    /**
     * Disables all games.
     */
    public void disableAll() {
        //TODO: The logic
    }

    /**
     * The filter for game jar files.
     */
    private class GameFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
        }
    }
}
