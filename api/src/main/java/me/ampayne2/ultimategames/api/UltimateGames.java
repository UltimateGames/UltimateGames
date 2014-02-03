/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.api;

import me.ampayne2.ultimategames.api.arenas.ArenaManager;
import me.ampayne2.ultimategames.api.arenas.QueueManager;
import me.ampayne2.ultimategames.api.arenas.countdowns.CountdownManager;
import me.ampayne2.ultimategames.api.arenas.scoreboards.ScoreboardManager;
import me.ampayne2.ultimategames.api.arenas.spawnpoints.SpawnpointManager;
import me.ampayne2.ultimategames.api.config.ConfigManager;
import me.ampayne2.ultimategames.api.games.GameManager;
import me.ampayne2.ultimategames.api.games.LobbyManager;
import me.ampayne2.ultimategames.api.games.blocks.GameBlockManager;
import me.ampayne2.ultimategames.api.games.items.GameItemManager;
import me.ampayne2.ultimategames.api.message.Messenger;
import me.ampayne2.ultimategames.api.players.PlayerManager;
import me.ampayne2.ultimategames.api.players.classes.GameClassManager;
import me.ampayne2.ultimategames.api.players.points.PointManager;
import me.ampayne2.ultimategames.api.players.teams.TeamManager;
import me.ampayne2.ultimategames.api.signs.SignManager;
import me.ampayne2.ultimategames.api.webapi.WebHandler;
import me.ampayne2.ultimategames.api.whitelist.WhitelistManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main UltimateGames API.
 */
public interface UltimateGames {

    /**
     * Gets the JavaPlugin instance of UltimateGames.
     *
     * @return The JavaPlugin.
     */
    JavaPlugin getPlugin();

    /**
     * Gets the ConfigManager.
     *
     * @return The ConfigManager.
     */
    ConfigManager getConfigManager();

    /**
     * Gets the Messenger.
     *
     * @return The Messenger.
     */
    Messenger getMessenger();

    /**
     * Gets the GameManager.
     *
     * @return The GameManager.
     */
    GameManager getGameManager();

    /**
     * Gets the ArenaManager.
     *
     * @return The ArenaManager.
     */
    ArenaManager getArenaManager();

    /**
     * Gets the PlayerManager.
     *
     * @return The PlayerManager.
     */
    PlayerManager getPlayerManager();

    /**
     * Gets the SignManager.
     *
     * @return The SignManager.
     */
    SignManager getSignManager();

    /**
     * Gets the GameClassManager.
     *
     * @return The GameClassManager.
     */
    GameClassManager getGameClassManager();

    /**
     * Gets the GameItemManager.
     *
     * @return The GameItemManager.
     */
    GameItemManager getGameItemManager();

    /**
     * Gets the GameBlockManager.
     *
     * @return The GameBlockManager.
     */
    GameBlockManager getGameBlockManager();

    /**
     * Gets the TeamManager.
     *
     * @return The TeamManager.
     */
    TeamManager getTeamManager();

    /**
     * Gets the QueueManager.
     *
     * @return The QueueManager.
     */
    QueueManager getQueueManager();

    /**
     * Gets the SpawnpointManager.
     *
     * @return The SpawnpointManager.
     */
    SpawnpointManager getSpawnpointManager();

    /**
     * Gets the CountdownManager.
     *
     * @return The CountdownManager.
     */
    CountdownManager getCountdownManager();

    /**
     * Gets the LobbyManager.
     *
     * @return The LobbyManager.
     */
    LobbyManager getLobbyManager();

    /**
     * Gets the ScoreboardManager.
     *
     * @return The ScoreboardManager.
     */
    ScoreboardManager getScoreboardManager();

    /**
     * Gets the WhitelistManager.
     *
     * @return The WhitelistManager.
     */
    WhitelistManager getWhitelistManager();

    /**
     * Gets the PointManager.
     *
     * @return The PointManager.
     */
    PointManager getPointManager();

    /**
     * Sets the PointManager.
     */
    void setPointManager(PointManager pointManager);

    /**
     * Adds a live stats API handler.
     *
     * @param path    The path of the stats.
     * @param handler The handler.
     */
    void addAPIHandler(String path, WebHandler handler);
}
