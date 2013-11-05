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
package me.ampayne2.ultimategames.arenas;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.enums.EndType;
import me.ampayne2.ultimategames.events.*;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ArenaManager {
    private final UltimateGames ultimateGames;
    private Map<Game, List<Arena>> arenas = new HashMap<Game, List<Arena>>();
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;
    private static final int Z_INDEX = 2;
    private static final int PITCH_INDEX = 3;
    private static final int YAW_INDEX = 4;
    private static final int LOCKED_INDEX = 5;

    public ArenaManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        if (arenaConfig.getConfigurationSection("Arenas") != null) {
            for (String gameKey : arenaConfig.getConfigurationSection("Arenas").getKeys(false)) {
                if (ultimateGames.getGameManager().gameExists(gameKey)) {
                    String gamePath = "Arenas." + gameKey;
                    for (String arenaKey : arenaConfig.getConfigurationSection(gamePath).getKeys(false)) {
                        if (!arenaExists(arenaKey, gameKey)) {
                            String arenaPath = gamePath + "." + arenaKey;
                            Region region = Region.fromList(arenaConfig.getStringList(arenaPath + ".Arena-Region"));
                            Arena arena = new Arena(ultimateGames, ultimateGames.getGameManager().getGame(gameKey), arenaKey, region.getMinimumLocation(), region.getMaximumLocation());
                            addArena(arena);
                            Bukkit.getServer().getPluginManager().registerEvents(arena, ultimateGames);
                            if (arenaConfig.contains(arenaPath + ".SpawnPoints")) {
                                @SuppressWarnings("unchecked") List<ArrayList<String>> spawnPoints = (ArrayList<ArrayList<String>>) arenaConfig.getList(arenaPath + ".SpawnPoints");
                                if (!spawnPoints.isEmpty()) {
                                    for (ArrayList<String> spawnPoint : spawnPoints) {
                                        Double x = Double.valueOf(spawnPoint.get(X_INDEX));
                                        Double y = Double.valueOf(spawnPoint.get(Y_INDEX));
                                        Double z = Double.valueOf(spawnPoint.get(Z_INDEX));
                                        Float pitch = Float.valueOf(spawnPoint.get(PITCH_INDEX));
                                        Float yaw = Float.valueOf(spawnPoint.get(YAW_INDEX));
                                        Location location = new Location(region.world, x, y, z);
                                        location.setPitch(pitch);
                                        location.setYaw(yaw);
                                        PlayerSpawnPoint newSpawnPoint = new PlayerSpawnPoint(ultimateGames, getArena(arenaKey, gameKey), location, Boolean.valueOf(spawnPoint.get(LOCKED_INDEX)));
                                        ultimateGames.getSpawnpointManager().addSpawnPoint(newSpawnPoint);
                                    }
                                }
                            }
                            if (arenaConfig.contains(arenaPath + ".SpectatorSpawnpoint")) {
                                @SuppressWarnings("unchecked") List<String> spawnPoint = (ArrayList<String>) arenaConfig.getList(arenaPath + ".SpectatorSpawnpoint");
                                if (!spawnPoint.isEmpty()) {
                                    Double x = Double.valueOf(spawnPoint.get(X_INDEX));
                                    Double y = Double.valueOf(spawnPoint.get(Y_INDEX));
                                    Double z = Double.valueOf(spawnPoint.get(Z_INDEX));
                                    Float pitch = Float.valueOf(spawnPoint.get(PITCH_INDEX));
                                    Float yaw = Float.valueOf(spawnPoint.get(YAW_INDEX));
                                    Location location = new Location(region.world, x, y, z);
                                    location.setPitch(pitch);
                                    location.setYaw(yaw);
                                    ultimateGames.getSpawnpointManager().setSpectatorSpawnPoint(getArena(arenaKey, gameKey), location);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks to see if an arena exists.
     *
     * @param arenaName The arena's name.
     * @param gameName  The arena's game's name.
     * @return If the arena exists or not.
     */
    public boolean arenaExists(String arenaName, String gameName) {
        if (ultimateGames.getGameManager().gameExists(gameName) && arenas.containsKey(ultimateGames.getGameManager().getGame(gameName))) {
            List<Arena> gameArenas = arenas.get(ultimateGames.getGameManager().getGame(gameName));
            for (Arena arena : gameArenas) {
                if (arenaName.equals(arena.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks to see if a location is inside an arena.
     *
     * @param location The location.
     * @return If the location is inside an arena or not.
     */
    public boolean isLocationInArena(Location location) {
        return getLocationArena(location) == null;
    }

    /**
     * Gets the arena a location is inside of.
     *
     * @param location The location.
     * @return The arena. Null if location isn't inside arena.
     */
    public Arena getLocationArena(Location location) {
        for (Entry<Game, List<Arena>> entry : arenas.entrySet()) {
            for (Arena arena : entry.getValue()) {
                if (arena.locationIsInArena(location)) {
                    return arena;
                }
            }
        }
        return null;
    }

    /**
     * Gets the arena from its name and its game's name.
     *
     * @param arenaName The arena's name.
     * @param gameName  The game's name.
     * @return The arena. Null if the arena doesn't exist.
     */
    public Arena getArena(String arenaName, String gameName) {
        if (ultimateGames.getGameManager().gameExists(gameName)) {
            for (Arena arena : arenas.get(ultimateGames.getGameManager().getGame(gameName))) {
                if (arenaName.equals(arena.getName())) {
                    return arena;
                }
            }
        }
        return null;
    }

    /**
     * Gets the arenas of a specific game.
     *
     * @param gameName The game's name.
     * @return The arenas. Null if the game doesn't exist or if the game has no arenas.
     */
    public List<Arena> getArenasOfGame(String gameName) {
        if (ultimateGames.getGameManager().gameExists(gameName) && arenas.containsKey(ultimateGames.getGameManager().getGame(gameName))) {
            return arenas.get(ultimateGames.getGameManager().getGame(gameName));
        } else {
            return null;
        }
    }

    /**
     * Adds an arena to the manager.
     *
     * @param arena The arena.
     */
    public void addArena(Arena arena) {
        if (arena.getGame().getGamePlugin().loadArena(arena)) {
            if (arenas.containsKey(arena.getGame())) {
                arenas.get(arena.getGame()).add(arena);
            } else {
                List<Arena> gameArenas = new ArrayList<Arena>();
                gameArenas.add(arena);
                arenas.put(arena.getGame(), gameArenas);
            }
            ultimateGames.getMetricsManager().addArena(arena);
        }
    }

    /**
     * Opens an arena.
     *
     * @param arena The arena.
     */
    public void openArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            ArenaOpenEvent event = new ArenaOpenEvent(arena);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                switch (arena.getStatus()) {
                    case STARTING:
                    case RUNNING:
                        endArena(arena);
                    case ENDING:
                    case RESET_FAILED:
                    case ARENA_STOPPED:
                        if (arena.getGame().getGamePlugin().openArena(arena)) {
                            arena.setStatus(ArenaStatus.OPEN);
                            for (String playerName : ultimateGames.getQueueManager().getNextPlayers(arena.getMaxPlayers() - arena.getPlayers().size(), arena)) {
                                ultimateGames.getPlayerManager().addPlayerToArena(Bukkit.getPlayerExact(playerName), arena, true);
                            }
                            ultimateGames.getMessageManager().debug("Opened arena " + arena.getName() + " of game " + arena.getGame().getName());
                        }
                        break;
                    default:
                }
            }
        }
    }

    /**
     * Starts the arena
     *
     * @param arena The arena.
     */
    public void startArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            ArenaStartEvent event = new ArenaStartEvent(arena);
            if (!event.isCancelled()) {
                switch (arena.getStatus()) {
                    case RUNNING:
                        endArena(arena);
                    case RESET_FAILED:
                    case ARENA_STOPPED:
                        openArena(arena);
                    case OPEN:
                        if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
                            arena.setStatus(ArenaStatus.STARTING);
                            arena.getGame().getGamePlugin().startArena(arena);
                            ultimateGames.getMessageManager().debug("Started arena " + arena.getName() + " of game " + arena.getGame().getName());
                        }
                    default:
                }
            }
        }
    }

    /**
     * Begins an arena.
     *
     * @param arena The arena.
     */
    public void beginArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            ArenaBeginEvent event = new ArenaBeginEvent(arena);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                switch (arena.getStatus()) {
                    case RUNNING:
                        endArena(arena);
                    case RESET_FAILED:
                    case ARENA_STOPPED:
                        openArena(arena);
                    case OPEN:
                        startArena(arena);
                    case STARTING:
                        if (arena.getGame().getGamePlugin().beginArena(arena)) {
                            arena.setStatus(ArenaStatus.RUNNING);
                            ultimateGames.getMessageManager().sendMessage(arena, "arenas.begin");
                            ultimateGames.getMessageManager().debug("Began arena " + arena.getName() + " of game " + arena.getGame().getName());
                        }
                    default:
                }
            }
        }
    }

    /**
     * Ends an arena.
     *
     * @param arena The arena.
     */
    public void endArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            ArenaEndEvent event = null;
            switch (arena.getStatus()) {
                case STARTING:
                    event = new ArenaEndEvent(arena, EndType.FORCED);
                case RUNNING:
                    if (event == null) {
                        event = new ArenaEndEvent(arena, EndType.UNFORCED);
                    }
                    arena.setStatus(ArenaStatus.ENDING);

                    if (ultimateGames.getCountdownManager().hasStartingCountdown(arena)) {
                        ultimateGames.getCountdownManager().stopStartingCountdown(arena);
                    }
                    if (ultimateGames.getCountdownManager().hasEndingCountdown(arena)) {
                        ultimateGames.getCountdownManager().stopEndingCountdown(arena);
                    }
                    arena.getGame().getGamePlugin().endArena(arena);

                    ultimateGames.getScoreboardManager().removeArenaScoreboard(arena);

                    for (Team team : ultimateGames.getTeamManager().getTeamsOfArena(arena)) {
                        team.removePlayers();
                    }

                    ultimateGames.getMessageManager().sendMessage(arena, "arenas.end");

                    // Teleport everybody out of the arena
                    for (String playerName : arena.getSpectators()) {
                        ultimateGames.getPlayerManager().removeSpectatorFromArena(Bukkit.getPlayerExact(playerName));
                    }
                    for (String playerName : arena.getPlayers()) {
                        ultimateGames.getPlayerManager().removePlayerFromArena(Bukkit.getPlayerExact(playerName), false);
                    }

                    ultimateGames.getMessageManager().debug("Ended arena " + arena.getName() + " of game " + arena.getGame().getName());
                    Bukkit.getPluginManager().callEvent(event);
                    if (arena.resetAfterMatch()) {
                        arena.setStatus(ArenaStatus.RESETTING);
                        // TODO: Reset arena world
                        if (!arena.getGame().getGamePlugin().resetArena(arena)) {
                            arena.setStatus(ArenaStatus.RESET_FAILED);
                        }
                        Bukkit.getPluginManager().callEvent(new ArenaResetEvent(arena));
                    } else {
                        arena.setStatus(ArenaStatus.ARENA_STOPPED);
                        openArena(arena);
                    }
                default:
            }
        }
    }

    /**
     * Stops an arena.
     *
     * @param arena The arena.
     */
    public void stopArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            switch (arena.getStatus()) {
                case STARTING:
                case RUNNING:
                    endArena(arena);
                case OPEN:
                case RESET_FAILED:
                    arena.setStatus(ArenaStatus.ARENA_STOPPED);
                    arena.getGame().getGamePlugin().stopArena(arena);
                    ultimateGames.getMessageManager().debug("Stopped arena " + arena.getName() + " of game " + arena.getGame().getName());
                    Bukkit.getPluginManager().callEvent(new ArenaStopEvent(arena));
                default:
            }
        }
    }

    /**
     * Gets all of the arenas.
     *
     * @return All of the existing arenas.
     */
    public List<Arena> getArenas() {
        List<Arena> arenaList = new ArrayList<Arena>();
        for (Entry<Game, List<Arena>> entry : arenas.entrySet()) {
            arenaList.addAll(entry.getValue());
        }
        return arenaList;
    }
}
