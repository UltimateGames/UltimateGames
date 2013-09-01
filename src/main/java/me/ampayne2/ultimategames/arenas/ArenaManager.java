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
package me.ampayne2.ultimategames.arenas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.scoreboards.ArenaScoreboard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaManager implements Manager {
    
    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private Map<Game, List<Arena>> arenas = new HashMap<Game, List<Arena>>();
    private static final int LOCATION_Y = 0;
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;
    private static final int Z_INDEX = 2;
    private static final int PITCH_INDEX = 3;
    private static final int YAW_INDEX = 4;
    private static final int LOCKED_INDEX = 5;

    public ArenaManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    public boolean load() {
        if (reload()) {
            loaded = true;
            return true;
        } else {
            return false;
        }
    }

    public boolean reload() {
        arenas.clear();
        ultimateGames.getConfigManager().getArenaConfig().reloadConfig();
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        if (arenaConfig.getConfigurationSection("Arenas") == null) {
            return true;
        }
        for (String gameKey : arenaConfig.getConfigurationSection("Arenas").getKeys(false)) {
            if (ultimateGames.getGameManager().gameExists(gameKey)) {
                String gamePath = "Arenas." + gameKey;
                for (String arenaKey : arenaConfig.getConfigurationSection(gamePath).getKeys(false)) {
                    if (!arenaExists(arenaKey, gameKey)) {
                        String arenaPath = gamePath + "." + arenaKey;
                        World world = Bukkit.getWorld(arenaConfig.getString(arenaPath + ".Arena-Location.world"));
                        Location minLocation = new Location(world, arenaConfig.getInt(arenaPath + ".Arena-Location.minx"), LOCATION_Y, arenaConfig.getInt(arenaPath + ".Arena-Location.minz"));
                        Location maxLocation = new Location(world, arenaConfig.getInt(arenaPath + ".Arena-Location.maxx"), LOCATION_Y, arenaConfig.getInt(arenaPath + ".Arena-Location.maxz"));
                        Arena arena = new Arena(ultimateGames, ultimateGames.getGameManager().getGame(gameKey), arenaKey, minLocation, maxLocation);
                        arena.setStatus(ArenaStatus.valueOf(arenaConfig.getString(arenaPath + ".Status")));
                        addArena(arena);
                        Bukkit.getServer().getPluginManager().registerEvents(arena, ultimateGames);
                        if (arenaConfig.contains(arenaPath + ".SpawnPoints")) {
                            @SuppressWarnings("unchecked")
                            List<ArrayList<String>> spawnPoints = (ArrayList<ArrayList<String>>) arenaConfig.getList(arenaPath + ".SpawnPoints");
                            if (!spawnPoints.isEmpty()) {
                                for (ArrayList<String> spawnPoint : spawnPoints) {
                                    Double x = Double.valueOf(spawnPoint.get(X_INDEX));
                                    Double y = Double.valueOf(spawnPoint.get(Y_INDEX));
                                    Double z = Double.valueOf(spawnPoint.get(Z_INDEX));
                                    Float pitch = Float.valueOf(spawnPoint.get(PITCH_INDEX));
                                    Float yaw = Float.valueOf(spawnPoint.get(YAW_INDEX));
                                    Location location = new Location(world, x, y, z);
                                    location.setPitch(pitch);
                                    location.setYaw(yaw);
                                    SpawnPoint newSpawnPoint = new SpawnPoint(ultimateGames, getArena(arenaKey, gameKey), location, Boolean.valueOf(spawnPoint.get(LOCKED_INDEX)));
                                    ultimateGames.getSpawnpointManager().addSpawnPoint(newSpawnPoint);
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void unload() {
        for (Arena arena : getArenas()) {
            arena.disable();
        }
        arenas.clear();
        loaded = false;
    }

    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Checks to see if an arena exists.
     * @param arenaName The arena's name.
     * @param gameName The arena's game's name.
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
     * @param location The location.
     * @return If the location is inside an arena or not.
     */
    public boolean isLocationInArena(Location location) {
        return getLocationArena(location) == null;
    }

    /**
     * Gets the arena a location is inside of.
     * @param location The location.
     * @return The arena. Null if location isn't inside arena.
     */
    public Arena getLocationArena(Location location) {
        Iterator<Entry<Game, List<Arena>>> it = arenas.entrySet().iterator();
        while (it.hasNext()) {
            List<Arena> gameArenas = it.next().getValue();
            for (Arena arena : gameArenas) {
                if (arena.locationIsInArena(location)) {
                    return arena;
                }
            }
        }
        return null;
    }

    /**
     * Gets the arena from its name and its game's name.
     * @param arenaName The arena's name.
     * @param gameName The game's name.
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
     * @param arena The arena.
     */
    public void openArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName()) && arena.getGame().getGamePlugin().openArena(arena)) {
            arena.setStatus(ArenaStatus.OPEN);
            for (String playerName : ultimateGames.getQueueManager().getNextPlayers(arena.getMaxPlayers() - arena.getPlayers().size(), arena)) {
                ultimateGames.getPlayerManager().addPlayerToArena(Bukkit.getPlayerExact(playerName), arena, true);
            }
        }
    }

    /**
     * Starts the arena
     * @param arena
     */
    public void startArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName()) && arena.getGame().getGamePlugin().isStartPossible(arena) && arena.getGame().getGamePlugin().startArena(arena)) {
            arena.setStatus(ArenaStatus.STARTING);
        }
    }

    /**
     * Begins an arena.
     * @param arena The arena.
     */
    public void beginArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            for (ArenaScoreboard scoreBoard : new ArrayList<ArenaScoreboard>(ultimateGames.getScoreboardManager().getArenaScoreboards(arena))) {
                ultimateGames.getScoreboardManager().removeArenaScoreboard(arena, scoreBoard.getName());
            }
            if (arena.getGame().getGamePlugin().beginArena(arena)) {
                arena.setStatus(ArenaStatus.RUNNING);
                ultimateGames.getMessageManager().broadcastMessageToArena(arena, "arenas.begin");
            }
        }
    }

    /**
     * Ends an arena.
     * @param arena The arena.
     */
    public void endArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            arena.setStatus(ArenaStatus.ENDING);

            if (ultimateGames.getCountdownManager().isStartingCountdownEnabled(arena)) {
                ultimateGames.getCountdownManager().stopStartingCountdown(arena);
            }
            if (ultimateGames.getCountdownManager().isEndingCountdownEnabled(arena)) {
                ultimateGames.getCountdownManager().stopEndingCountdown(arena);
            }
            arena.getGame().getGamePlugin().endArena(arena);

            for (ArenaScoreboard scoreBoard : new ArrayList<ArenaScoreboard>(ultimateGames.getScoreboardManager().getArenaScoreboards(arena))) {
                ultimateGames.getScoreboardManager().removeArenaScoreboard(arena, scoreBoard.getName());
            }

            ultimateGames.getMessageManager().broadcastMessageToArena(arena, "arenas.end");

            // Teleport everybody out of the arena
            for (String playerName : arena.getPlayers()) {
                ultimateGames.getPlayerManager().removePlayerFromArena(Bukkit.getPlayerExact(playerName), false);
            }
            for (String playerName : arena.getSpectators()) {
                ultimateGames.getPlayerManager().removeSpectatorFromArena(Bukkit.getPlayerExact(playerName));
            }
            if (arena.resetAfterMatch()) {
                arena.setStatus(ArenaStatus.RESETTING);
                ultimateGames.getLogManager().rollbackArena(arena);
                if (!arena.getGame().getGamePlugin().resetArena(arena)) {
                    arena.setStatus(ArenaStatus.RESET_FAILED);
                }
            } else {
                arena.setStatus(ArenaStatus.OPEN);
            }
        }
    }

    /**
     * Stops an arena.
     * @param arena The arena.
     */
    public void stopArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            arena.setStatus(ArenaStatus.ARENA_STOPPED);
            arena.getGame().getGamePlugin().stopArena(arena);
        }
    }

    /**
     * Gets all of the arenas.
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
