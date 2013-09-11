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
package me.ampayne2.ultimategames;

import java.util.logging.Level;

import com.alta189.simplesave.exceptions.ConnectionException;
import com.alta189.simplesave.exceptions.TableRegistrationException;

import me.ampayne2.ultimategames.api.PointManager;
import me.ampayne2.ultimategames.arenas.ArenaListener;
import me.ampayne2.ultimategames.arenas.ArenaManager;
import me.ampayne2.ultimategames.arenas.LogManager;
import me.ampayne2.ultimategames.arenas.SpawnpointManager;
import me.ampayne2.ultimategames.chests.UGChestManager;
import me.ampayne2.ultimategames.classes.ClassManager;
import me.ampayne2.ultimategames.command.CommandController;
import me.ampayne2.ultimategames.countdowns.CountdownManager;
import me.ampayne2.ultimategames.database.DatabaseManager;
import me.ampayne2.ultimategames.files.ConfigManager;
import me.ampayne2.ultimategames.games.GameManager;
import me.ampayne2.ultimategames.misc.MetricsManager;
import me.ampayne2.ultimategames.misc.PlayerHeadListener;
import me.ampayne2.ultimategames.players.LobbyManager;
import me.ampayne2.ultimategames.players.PlayerManager;
import me.ampayne2.ultimategames.players.QueueManager;
import me.ampayne2.ultimategames.scoreboards.ScoreboardManager;
import me.ampayne2.ultimategames.signs.SignListener;
import me.ampayne2.ultimategames.signs.UGSignManager;
import me.ampayne2.ultimategames.teams.TeamManager;
import me.ampayne2.ultimategames.utils.Utils;
import me.ampayne2.ultimategames.webapi.JettyServer;
import me.ampayne2.ultimategames.webapi.handlers.GeneralInformationHandler;
import me.ampayne2.ultimategames.whitelist.WhitelistManager;

public class ManagerController {

    private UltimateGames ultimateGames;
    public ConfigManager configManager;
    public ClassManager classManager;
    public GameManager gameManager;
    public TeamManager teamManager;
    public ArenaManager arenaManager;
    public UGSignManager ugSignManager;
    public UGChestManager ugChestManager;
    public QueueManager queueManager;
    public Message messageManager;
    public SpawnpointManager spawnpointManager;
    public PlayerManager playerManager;
    public CountdownManager countdownManager;
    public LobbyManager lobbyManager;
    public ScoreboardManager scoreboardManager;
    public WhitelistManager whitelistManager;
    public DatabaseManager databaseManager;
    public LogManager logManager;
    public Utils utils;
    public MetricsManager metricsManager;
    public PointManager pointManager;
    public CommandController commandController;
    public JettyServer jettyServer;

    /**
     * Creates a new ManagerController that controlls all of UltimateGames.
     * @param ultimateGames A reference to the UltimateGames instance.
     */
    public ManagerController(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Loads all of the managers.
     * @return True if all of the managers loaded successfully, else false.
     */
    public boolean loadManagers() {
        // Initializes all the managers, registers a few events, and sets the UG command executor.
        configManager = new ConfigManager(ultimateGames);
        messageManager = new Message(ultimateGames);
        playerManager = new PlayerManager(ultimateGames);
        metricsManager = new MetricsManager(ultimateGames);
        classManager = new ClassManager();
        gameManager = new GameManager(ultimateGames);
        queueManager = new QueueManager(ultimateGames);
        spawnpointManager = new SpawnpointManager(ultimateGames);
        scoreboardManager = new ScoreboardManager();
        teamManager = new TeamManager(ultimateGames);
        arenaManager = new ArenaManager(ultimateGames);
        ugSignManager = new UGSignManager(ultimateGames);
        ugChestManager = new UGChestManager(ultimateGames);
        countdownManager = new CountdownManager(ultimateGames);
        lobbyManager = new LobbyManager(ultimateGames);
        whitelistManager = new WhitelistManager(ultimateGames);
        logManager = new LogManager(ultimateGames);
        pointManager = new PointManager();
        utils = new Utils(ultimateGames);
        ultimateGames.getServer().getPluginManager().registerEvents(new SignListener(ultimateGames), ultimateGames);
        ultimateGames.getServer().getPluginManager().registerEvents(new ArenaListener(ultimateGames), ultimateGames);
        if (ultimateGames.getServer().getPluginManager().isPluginEnabled("PlayerHeads")) {
            ultimateGames.getServer().getPluginManager().registerEvents(new PlayerHeadListener(ultimateGames), ultimateGames);
        }
        ultimateGames.getServer().getPluginManager().registerEvents(playerManager, ultimateGames);
        commandController = new CommandController(ultimateGames);
        ultimateGames.getServer().getPluginManager().registerEvents(commandController, ultimateGames);
        ultimateGames.getCommand("ultimategames").setExecutor(commandController);
        
        
        // Tells each manager to load what it needs to function. Has a specific order based on the manager's dependencies on each other.
        if (!(configManager.load() && messageManager.load() && playerManager.load() && metricsManager.load() && classManager.load() && gameManager.load())) {
            return false;
        }
        messageManager.loadGameMessages();
        if (!(queueManager.load() && spawnpointManager.load() && scoreboardManager.load() && teamManager.load() && arenaManager.load())) {
            return false;
        }
        metricsManager.addTotalPlayersGraph();
        if (ultimateGames.getConfig().getBoolean("enableAPI")) {
            try {
                ultimateGames.getLogger().info("Enabling live stats API link");
                jettyServer = new JettyServer(ultimateGames);
                jettyServer.startServer();
            } catch (Exception e) {
                ultimateGames.getLogger().info("Failed to enable live stats API link");
                messageManager.debug(e);
            }
        }
        jettyServer.getHandler().addHandler("/general", new GeneralInformationHandler(ultimateGames));
        if (!(ugSignManager.load() && ugChestManager.load() && countdownManager.load() && lobbyManager.load() && whitelistManager.load())) {
            return false;
        }
        try {
            databaseManager = new DatabaseManager(ultimateGames);
        } catch (TableRegistrationException e) {
            ultimateGames.getLogger().severe("A error occured while connecting to the database!");
            messageManager.debug(e);
            ultimateGames.getServer().getPluginManager().disablePlugin(ultimateGames);
            return false;
        } catch (ConnectionException e) {
            ultimateGames.getLogger().severe("A error occured while connecting to the database!");
            messageManager.debug(e);
            ultimateGames.getServer().getPluginManager().disablePlugin(ultimateGames);
            return false;
        }
        if (!logManager.load()) {
            return false;
        }
        metricsManager.addTotalPlayersGraph();
        return true;
    }
    
    /**
     * Reloads all of the managers.
     * @return True if everything reloaded successfully, else false.
     */
    public boolean reloadManagers() {
        if (!(configManager.reload() && messageManager.reload() && playerManager.reload() && metricsManager.reload() && classManager.reload() && gameManager.reload())) {
            return false;
        }
        messageManager.loadGameMessages();
        if (!(queueManager.reload() && spawnpointManager.reload() && scoreboardManager.reload() && teamManager.reload() && arenaManager.reload())) {
            return false;
        }
        if (ultimateGames.getConfig().getBoolean("enableAPI")) {
            try {
                ultimateGames.getLogger().info("Reloading live stats API link");
                jettyServer.stopServer();
                jettyServer = new JettyServer(ultimateGames);
                jettyServer.startServer();
            } catch (Exception e) {
                ultimateGames.getLogger().info("Failed to reload live stats API link");
                messageManager.debug(e);
            }
        }
        jettyServer.getHandler().addHandler("/general", new GeneralInformationHandler(ultimateGames));
        if (!(ugSignManager.reload() && countdownManager.reload() && lobbyManager.reload() && whitelistManager.reload() && logManager.reload())) {
            return false;
        }
        
        pointManager = new PointManager();
        utils = new Utils(ultimateGames);
        
        try {
            databaseManager = new DatabaseManager(ultimateGames);
        } catch (TableRegistrationException e) {
            ultimateGames.getLogger().severe("A error occured while connecting to the database!");
            messageManager.debug(e);
            ultimateGames.getServer().getPluginManager().disablePlugin(ultimateGames);
            return false;
        } catch (ConnectionException e) {
            ultimateGames.getLogger().severe("A error occured while connecting to the database!");
            messageManager.debug(e);
            ultimateGames.getServer().getPluginManager().disablePlugin(ultimateGames);
            return false;
        }
        return true;
    }
    
    /**
     * Unloads all of the managers.
     */
    public void unloadManagers() {
        configManager.unload();
        messageManager.unload();
        playerManager.unload();
        metricsManager.unload();
        classManager.unload();
        gameManager.unload();
        queueManager.unload();
        spawnpointManager.unload();
        scoreboardManager.unload();
        teamManager.unload();
        arenaManager.unload();
        try {
            jettyServer.stopServer();
        } catch (Exception e) {
            ultimateGames.getLogger().log(Level.SEVERE, "An error occured in unloading the Web API", e);
        }
        ugSignManager.unload();
        ugChestManager.unload();
        countdownManager.unload();
        lobbyManager.unload();
        whitelistManager.unload();
        logManager.unload();
    }

}
