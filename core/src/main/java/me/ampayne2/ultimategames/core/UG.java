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
package me.ampayne2.ultimategames.core;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.message.RecipientHandler;
import me.ampayne2.ultimategames.api.players.points.PointManager;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.api.rollback.RollbackManager;
import me.ampayne2.ultimategames.api.signs.Sign;
import me.ampayne2.ultimategames.api.signs.SignType;
import me.ampayne2.ultimategames.api.webapi.GeneralInformationHandler;
import me.ampayne2.ultimategames.api.webapi.WebHandler;
import me.ampayne2.ultimategames.core.arenas.ArenaListener;
import me.ampayne2.ultimategames.core.arenas.UArenaManager;
import me.ampayne2.ultimategames.core.arenas.UQueueManager;
import me.ampayne2.ultimategames.core.arenas.countdowns.UCountdownManager;
import me.ampayne2.ultimategames.core.arenas.scoreboards.UScoreboardManager;
import me.ampayne2.ultimategames.core.arenas.spawnpoints.USpawnpointManager;
import me.ampayne2.ultimategames.core.arenas.zones.UZoneManager;
import me.ampayne2.ultimategames.core.chests.UChestManager;
import me.ampayne2.ultimategames.core.command.CommandController;
import me.ampayne2.ultimategames.core.config.UConfigManager;
import me.ampayne2.ultimategames.core.games.UGameManager;
import me.ampayne2.ultimategames.core.games.ULobbyManager;
import me.ampayne2.ultimategames.core.games.blocks.UGameBlockManager;
import me.ampayne2.ultimategames.core.games.items.UGameItemManager;
import me.ampayne2.ultimategames.core.message.UMessenger;
import me.ampayne2.ultimategames.core.misc.MetricsManager;
import me.ampayne2.ultimategames.core.misc.PlayerHeadListener;
import me.ampayne2.ultimategames.core.players.UPlayerManager;
import me.ampayne2.ultimategames.core.players.classes.UGameClassManager;
import me.ampayne2.ultimategames.core.players.points.NullPointManager;
import me.ampayne2.ultimategames.core.players.teams.UTeamManager;
import me.ampayne2.ultimategames.core.rollback.LogBlockRollback;
import me.ampayne2.ultimategames.core.signs.RedstoneOutputSign;
import me.ampayne2.ultimategames.core.signs.SignListener;
import me.ampayne2.ultimategames.core.signs.USignManager;
import me.ampayne2.ultimategames.core.webapi.JettyServer;
import me.ampayne2.ultimategames.core.whitelist.UWhitelistManager;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The UltimateGames plugin's main class.
 */
public class UG extends JavaPlugin implements UltimateGames {
    private static UG instance;
    private UConfigManager configManager;
    private UGameClassManager gameClassManager;
    private UGameItemManager gameItemManager;
    private UGameBlockManager gameBlockManager;
    private UGameManager gameManager;
    private UTeamManager teamManager;
    private UArenaManager arenaManager;
    private USignManager signManager;
    private UChestManager chestManager;
    private UQueueManager queueManager;
    private UMessenger messenger;
    private USpawnpointManager spawnpointManager;
    private UZoneManager zoneManager;
    private UPlayerManager playerManager;
    private UCountdownManager countdownManager;
    private ULobbyManager lobbyManager;
    private UScoreboardManager scoreboardManager;
    private UWhitelistManager whitelistManager;
    private MetricsManager metricsManager;
    private PointManager pointManager;
    private CommandController commandController;
    private RollbackManager rollbackManager;
    private JettyServer jettyServer;

    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        configManager = new UConfigManager(this);
        messenger = new UMessenger(this);
        messenger.registerRecipient(CommandSender.class, new RecipientHandler() {
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
                List<String> players = new ArrayList<>();
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
        playerManager = new UPlayerManager(this);
        metricsManager = new MetricsManager(this);
        gameClassManager = new UGameClassManager(this);
        gameItemManager = new UGameItemManager();
        gameBlockManager = new UGameBlockManager();
        gameManager = new UGameManager(this);
        queueManager = new UQueueManager(this);
        spawnpointManager = new USpawnpointManager(this);
        zoneManager = new UZoneManager(this);
        scoreboardManager = new UScoreboardManager();
        teamManager = new UTeamManager(this);
        arenaManager = new UArenaManager(this);
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
        signManager = new USignManager(this);
        chestManager = new UChestManager(this);
        countdownManager = new UCountdownManager(this);
        lobbyManager = new ULobbyManager(this);
        whitelistManager = new UWhitelistManager(this);
        pointManager = new NullPointManager();
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(this), this);
        if (getServer().getPluginManager().isPluginEnabled("PlayerHeads")) {
            getServer().getPluginManager().registerEvents(new PlayerHeadListener(this), this);
        }
        getServer().getPluginManager().registerEvents(playerManager, this);
        commandController = new CommandController(this);

        //We detect if we have a logging plugin installed
        Plugin plugin = getServer().getPluginManager().getPlugin("LogBlock");
        if (plugin != null) {
            rollbackManager = new LogBlockRollback(plugin);
        }
    }

    public void onDisable() {
        for (Game game : gameManager.getGames()) {
            for (Sign sign : signManager.getSignsOfGame(game, SignType.REDSTONE_OUTPUT)) {
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

    public static UG getInstance() {
        return instance;
    }

    @Override
    public JavaPlugin getPlugin() {
        return instance;
    }

    @Override
    public UConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public UMessenger getMessenger() {
        return messenger;
    }

    @Override
    public UGameManager getGameManager() {
        return gameManager;
    }

    @Override
    public UArenaManager getArenaManager() {
        return arenaManager;
    }

    @Override
    public UPlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public USignManager getSignManager() {
        return signManager;
    }

    // TODO: Finish and add to the API
    public UChestManager getUGChestManager() {
        return chestManager;
    }

    @Override
    public UGameClassManager getGameClassManager() {
        return gameClassManager;
    }

    @Override
    public UGameItemManager getGameItemManager() {
        return gameItemManager;
    }

    @Override
    public UGameBlockManager getGameBlockManager() {
        return gameBlockManager;
    }

    @Override
    public UTeamManager getTeamManager() {
        return teamManager;
    }

    @Override
    public UQueueManager getQueueManager() {
        return queueManager;
    }

    @Override
    public USpawnpointManager getSpawnpointManager() {
        return spawnpointManager;
    }

    @Override
    public UZoneManager getZoneManager() {
        return zoneManager;
    }

    @Override
    public UCountdownManager getCountdownManager() {
        return countdownManager;
    }

    @Override
    public ULobbyManager getLobbyManager() {
        return lobbyManager;
    }

    @Override
    public UScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public UWhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    public MetricsManager getMetricsManager() {
        return metricsManager;
    }

    public CommandController getCommandController() {
        return commandController;
    }

    @Override
    public void addAPIHandler(String path, WebHandler handler) {
        if (jettyServer != null) {
            jettyServer.getHandler().addHandler(path, handler);
        }
    }

    @Override
    public PointManager getPointManager() {
        return pointManager;
    }

    @Override
    public void setPointManager(PointManager pointManager) {
        this.pointManager = pointManager;
    }

    @Override
    public RollbackManager getRollbackManager() {
        return rollbackManager;
    }
}
