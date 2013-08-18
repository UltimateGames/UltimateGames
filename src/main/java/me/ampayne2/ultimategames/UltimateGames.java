/*
 * This file is part of UltimateGames.
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 * UltimateGames is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * UltimateGames is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames. If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames;

import me.ampayne2.ultimategames.api.PointManager;
import me.ampayne2.ultimategames.arenas.ArenaListener;
import me.ampayne2.ultimategames.arenas.ArenaManager;
import me.ampayne2.ultimategames.arenas.LogManager;
import me.ampayne2.ultimategames.arenas.SpawnpointManager;
import me.ampayne2.ultimategames.command.CommandController;
import me.ampayne2.ultimategames.countdowns.CountdownManager;
import me.ampayne2.ultimategames.database.DatabaseManager;
import me.ampayne2.ultimategames.files.ConfigManager;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.games.GameManager;
import me.ampayne2.ultimategames.players.LobbyManager;
import me.ampayne2.ultimategames.players.PlayerManager;
import me.ampayne2.ultimategames.players.QueueManager;
import me.ampayne2.ultimategames.scoreboards.ScoreboardManager;
import me.ampayne2.ultimategames.signs.RedstoneOutputSign;
import me.ampayne2.ultimategames.signs.SignListener;
import me.ampayne2.ultimategames.signs.UGSignManager;
import me.ampayne2.ultimategames.utils.Utils;
import me.ampayne2.ultimategames.webapi.JettyServer;
import me.ampayne2.ultimategames.webapi.WebHandler;
import me.ampayne2.ultimategames.webapi.handlers.GeneralInformationHandler;
import me.ampayne2.ultimategames.whitelist.WhitelistManager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;

import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;

public class UltimateGames extends JavaPlugin {
    private JavaPlugin plugin;
    private ConfigManager configManager;
    private GameManager gameManager;
    private ArenaManager arenaManager;
    private UGSignManager ugSignManager;
    private QueueManager queueManager;
    private Message messageManager;
    private SpawnpointManager spawnpointManager;
    private PlayerManager playerManager;
    private CountdownManager countdownManager;
    private LobbyManager lobbyManager;
    private ScoreboardManager scoreboardManager;
    private WhitelistManager whitelistManager;
    private DatabaseManager databaseManager;
    private LogManager logManager;
    private JettyServer jettyServer;
    private Utils utils;
    private MetricsManager metricsManager;
    private PointManager pointManager;
    private CommandController commandController;

    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        if (getConfig().getBoolean("enableAPI")) {
            try {
                getLogger().info("Enabling API link");
                jettyServer = new JettyServer(this);
                jettyServer.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        configManager = new ConfigManager(this);
        messageManager = new Message(this);
        playerManager = new PlayerManager(this);
        metricsManager = new MetricsManager(this);
        gameManager = new GameManager(this);
        messageManager.loadGameMessages();
        queueManager = new QueueManager(this);
        spawnpointManager = new SpawnpointManager(this);
        scoreboardManager = new ScoreboardManager();
        arenaManager = new ArenaManager(this);
        ugSignManager = new UGSignManager(this);
        countdownManager = new CountdownManager(this);
        lobbyManager = new LobbyManager(this);
        whitelistManager = new WhitelistManager(this);
        try {
            databaseManager = new DatabaseManager(this);
        } catch (TableRegistrationException e) {
            getLogger().severe("A error occured while connecting to the database!");
            messageManager.debug(e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        } catch (ConnectionException e) {
            getLogger().severe("A error occured while connecting to the database!");
            messageManager.debug(e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        logManager = new LogManager(this);
        pointManager = new PointManager();
        utils = new Utils(this);
        jettyServer.getHandler().addHandler("/general", new GeneralInformationHandler(this));
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(this), this);
        getServer().getPluginManager().registerEvents(playerManager, this);
        commandController = new CommandController(this);
        getServer().getPluginManager().registerEvents(commandController, this);
        getCommand("ultimategames").setExecutor(commandController);
        metricsManager.addTotalPlayersGraph();
    }

    public void onDisable() {
        for (Game game : gameManager.getGames()) {
            for (RedstoneOutputSign sign : ugSignManager.getRedstoneOutputSignsOfGame(game)) {
                sign.setPowered(false);
            }
        }
        if (jettyServer != null) {
            try {
                jettyServer.stopServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Message getMessageManager() {
        return messageManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public UGSignManager getUGSignManager() {
        return ugSignManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public SpawnpointManager getSpawnpointManager() {
        return spawnpointManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public CountdownManager getCountdownManager() {
        return countdownManager;
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    public LogManager getLogManager() {
        return logManager;
    }

    public Utils getUtils() {
        return utils;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
    }
    
    public CommandController getCommandController() {
        return commandController;
    }

    public PluginClassLoader getPluginClassLoader() {
        return (PluginClassLoader) getClassLoader();
    }

    public void addAPIHandler(String path, WebHandler handler) {
        if (jettyServer != null) {
            jettyServer.getHandler().addHandler(path, handler);
        }
    }
    
    public PointManager getPointManager() {
        return pointManager;
    }

    public void setPointManager(PointManager pointManager) {
        this.pointManager = pointManager;
    }
}
