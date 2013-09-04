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

import java.util.logging.Level;

import me.ampayne2.ultimategames.api.PointManager;
import me.ampayne2.ultimategames.arenas.ArenaManager;
import me.ampayne2.ultimategames.arenas.LogManager;
import me.ampayne2.ultimategames.arenas.SpawnpointManager;
import me.ampayne2.ultimategames.chests.UGChestManager;
import me.ampayne2.ultimategames.classes.ClassManager;
import me.ampayne2.ultimategames.command.CommandController;
import me.ampayne2.ultimategames.countdowns.CountdownManager;
import me.ampayne2.ultimategames.database.DatabaseManager;
import me.ampayne2.ultimategames.files.ConfigManager;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.games.GameManager;
import me.ampayne2.ultimategames.misc.MetricsManager;
import me.ampayne2.ultimategames.players.LobbyManager;
import me.ampayne2.ultimategames.players.PlayerManager;
import me.ampayne2.ultimategames.players.QueueManager;
import me.ampayne2.ultimategames.scoreboards.ScoreboardManager;
import me.ampayne2.ultimategames.signs.RedstoneOutputSign;
import me.ampayne2.ultimategames.signs.UGSignManager;
import me.ampayne2.ultimategames.teams.TeamManager;
import me.ampayne2.ultimategames.utils.Utils;
import me.ampayne2.ultimategames.webapi.WebHandler;
import me.ampayne2.ultimategames.whitelist.WhitelistManager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;

public class UltimateGames extends JavaPlugin {
    
    private JavaPlugin plugin;
    private ManagerController managerController;

    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        managerController = new ManagerController(this);
        if (!managerController.loadManagers()) {
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public void onDisable() {
        managerController.unloadManagers();
        for (Game game : managerController.gameManager.getGames()) {
            for (RedstoneOutputSign sign : managerController.ugSignManager.getRedstoneOutputSignsOfGame(game)) {
                sign.setPowered(false);
            }
        }
        if (managerController.jettyServer != null) {
            try {
                managerController.jettyServer.stopServer();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "An error occured in unloading the Web API", e);
            }
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
    
    public ManagerController getManager() {
        return managerController;
    }

    public ConfigManager getConfigManager() {
        return managerController.configManager;
    }

    public Message getMessageManager() {
        return managerController.messageManager;
    }
    
    public ClassManager getClassManager() {
        return managerController.classManager;
    }

    public GameManager getGameManager() {
        return managerController.gameManager;
    }
    
    public TeamManager getTeamManager() {
        return managerController.teamManager;
    }

    public ArenaManager getArenaManager() {
        return managerController.arenaManager;
    }

    public UGSignManager getUGSignManager() {
        return managerController.ugSignManager;
    }
    
    public UGChestManager getUGChestManager() {
        return managerController.ugChestManager;
    }

    public QueueManager getQueueManager() {
        return managerController.queueManager;
    }

    public SpawnpointManager getSpawnpointManager() {
        return managerController.spawnpointManager;
    }

    public PlayerManager getPlayerManager() {
        return managerController.playerManager;
    }

    public CountdownManager getCountdownManager() {
        return managerController.countdownManager;
    }

    public LobbyManager getLobbyManager() {
        return managerController.lobbyManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return managerController.scoreboardManager;
    }

    public WhitelistManager getWhitelistManager() {
        return managerController.whitelistManager;
    }

    public DatabaseManager getDatabaseManager() {
        return managerController.databaseManager;
    }
    
    public LogManager getLogManager() {
        return managerController.logManager;
    }

    public Utils getUtils() {
        return managerController.utils;
    }

    public MetricsManager getMetricsManager() {
        return managerController.metricsManager;
    }
    
    public CommandController getCommandController() {
        return managerController.commandController;
    }

    public PluginClassLoader getPluginClassLoader() {
        return (PluginClassLoader) getClassLoader();
    }

    public void addAPIHandler(String path, WebHandler handler) {
        if (managerController.jettyServer != null) {
            managerController.jettyServer.getHandler().addHandler(path, handler);
        }
    }
    
    public PointManager getPointManager() {
        return managerController.pointManager;
    }

    public void setPointManager(PointManager pointManager) {
        managerController.pointManager = pointManager;
    }
}
