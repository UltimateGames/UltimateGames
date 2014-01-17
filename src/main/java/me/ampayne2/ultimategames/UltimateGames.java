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

import me.ampayne2.ultimategames.api.PointManager;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.ArenaListener;
import me.ampayne2.ultimategames.arenas.ArenaManager;
import me.ampayne2.ultimategames.arenas.countdowns.CountdownManager;
import me.ampayne2.ultimategames.arenas.scoreboards.ScoreboardManager;
import me.ampayne2.ultimategames.arenas.spawnpoints.SpawnpointManager;
import me.ampayne2.ultimategames.chests.UGChestManager;
import me.ampayne2.ultimategames.command.CommandController;
import me.ampayne2.ultimategames.config.ConfigManager;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.games.GameManager;
import me.ampayne2.ultimategames.games.items.GameItemManager;
import me.ampayne2.ultimategames.message.Messenger;
import me.ampayne2.ultimategames.message.RecipientHandler;
import me.ampayne2.ultimategames.misc.MetricsManager;
import me.ampayne2.ultimategames.misc.PlayerHeadListener;
import me.ampayne2.ultimategames.players.LobbyManager;
import me.ampayne2.ultimategames.players.PlayerManager;
import me.ampayne2.ultimategames.players.QueueManager;
import me.ampayne2.ultimategames.players.classes.GameClassManager;
import me.ampayne2.ultimategames.players.teams.Team;
import me.ampayne2.ultimategames.players.teams.TeamManager;
import me.ampayne2.ultimategames.signs.*;
import me.ampayne2.ultimategames.utils.NullPointManager;
import me.ampayne2.ultimategames.webapi.JettyServer;
import me.ampayne2.ultimategames.webapi.WebHandler;
import me.ampayne2.ultimategames.webapi.handlers.GeneralInformationHandler;
import me.ampayne2.ultimategames.whitelist.WhitelistManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The UltimateGames plugin's main class.
 */
public class UltimateGames extends JavaPlugin {
    private static UltimateGames instance;
    private ConfigManager configManager;
    private GameClassManager gameClassManager;
    private GameItemManager gameItemManager;
    private GameManager gameManager;
    private TeamManager teamManager;
    private ArenaManager arenaManager;
    private UGSignManager ugSignManager;
    private UGChestManager ugChestManager;
    private QueueManager queueManager;
    private Messenger messenger;
    private SpawnpointManager spawnpointManager;
    private PlayerManager playerManager;
    private CountdownManager countdownManager;
    private LobbyManager lobbyManager;
    private ScoreboardManager scoreboardManager;
    private WhitelistManager whitelistManager;
    private MetricsManager metricsManager;
    private PointManager pointManager;
    private CommandController commandController;
    private JettyServer jettyServer;

    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        configManager = new ConfigManager(this);
        messenger = new Messenger(this).registerRecipient(CommandSender.class, new RecipientHandler() {
            @Override
            public void sendMessage(Object recipient, String message) {
                ((CommandSender) recipient).sendMessage(message);
            }
        }).registerRecipient(Team.class, new RecipientHandler() {
            @Override
            public void sendMessage(Object recipient, String message) {
                for (String playerName : ((Team) recipient).getPlayers()) {
                    Player player = Bukkit.getPlayerExact(playerName);
                    if (player != null) {
                        player.sendMessage(message);
                    }
                }
            }
        }).registerRecipient(Arena.class, new RecipientHandler() {
            @Override
            public void sendMessage(Object recipient, String message) {
                Arena arena = (Arena) recipient;
                List<String> players = new ArrayList<String>();
                players.addAll(arena.getPlayers());
                players.addAll(arena.getSpectators());
                for (String playerName : players) {
                    Player player = Bukkit.getPlayerExact(playerName);
                    if (player != null) {
                        player.sendMessage(message);
                    }
                }
            }
        }).registerRecipient(Server.class, new RecipientHandler() {
            @Override
            public void sendMessage(Object recipient, String message) {
                ((Server) recipient).broadcastMessage(message);
            }
        });
        playerManager = new PlayerManager(this);
        metricsManager = new MetricsManager(this);
        gameClassManager = new GameClassManager(this);
        gameItemManager = new GameItemManager();
        gameManager = new GameManager(this);
        queueManager = new QueueManager(this);
        spawnpointManager = new SpawnpointManager(this);
        scoreboardManager = new ScoreboardManager();
        teamManager = new TeamManager(this);
        arenaManager = new ArenaManager(this);
        metricsManager.addTotalPlayersGraph();
        if (getConfig().getBoolean("enableAPI")) {
            try {
                getLogger().info("Enabling live stats API link");
                jettyServer = new JettyServer(this);
                jettyServer.startServer();
            } catch (Exception e) {
                getLogger().info("Failed to enable live stats API link");
                messenger.debug(e);
            }
            jettyServer.getHandler().addHandler("/general", new GeneralInformationHandler(this));
        }
        ugSignManager = new UGSignManager(this);
        ugChestManager = new UGChestManager(this);
        countdownManager = new CountdownManager(this);
        lobbyManager = new LobbyManager(this);
        whitelistManager = new WhitelistManager(this);
        pointManager = new NullPointManager();
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(this), this);
        if (getServer().getPluginManager().isPluginEnabled("PlayerHeads")) {
            getServer().getPluginManager().registerEvents(new PlayerHeadListener(this), this);
        }
        getServer().getPluginManager().registerEvents(playerManager, this);
        commandController = new CommandController(this);
    }

    public void onDisable() {
        for (Game game : gameManager.getGames()) {
            for (UGSign sign : ugSignManager.getUGSignsOfGame(game, SignType.REDSTONE_OUTPUT)) {
                ((RedstoneOutputSign) sign).setPowered(false);
            }
        }
        if (jettyServer != null) {
            try {
                jettyServer.stopServer();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "An error occured in unloading the Web API", e);
            }
        }
        instance = null;
    }

    public static UltimateGames getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public UGSignManager getUGSignManager() {
        return ugSignManager;
    }

    public UGChestManager getUGChestManager() {
        return ugChestManager;
    }

    public GameClassManager getGameClassManager() {
        return gameClassManager;
    }

    public GameItemManager getGameItemManager() {
        return gameItemManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public SpawnpointManager getSpawnpointManager() {
        return spawnpointManager;
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
