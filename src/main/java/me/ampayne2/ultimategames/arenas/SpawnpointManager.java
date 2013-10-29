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
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * Manages arena spawnpoints for players and spectators.
 */
public class SpawnpointManager {
    private final UltimateGames ultimateGames;
    private Map<Arena, List<PlayerSpawnPoint>> playerSpawnPoints = new HashMap<Arena, List<PlayerSpawnPoint>>();
    private Map<Arena, SpectatorSpawnPoint> spectatorSpawnPoints = new HashMap<Arena, SpectatorSpawnPoint>();

    /**
     * Creates a new Spawnpoint Manager.
     *
     * @param ultimateGames A reference to the UltimateGames instance.
     */
    public SpawnpointManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Checks if an arena has a spawnpoint at the index.
     *
     * @param arena The arena.
     * @param index The index.
     * @return If the arena has a spawnpoint at the index or not.
     */
    public Boolean hasSpawnPointAtIndex(Arena arena, Integer index) {
        return playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= index;
    }

    /**
     * Adds a spawnpoint to the manager.
     *
     * @param spawnPoint The spawnpoint.
     */
    public void addSpawnPoint(PlayerSpawnPoint spawnPoint) {
        if (playerSpawnPoints.containsKey(spawnPoint.getArena())) {
            playerSpawnPoints.get(spawnPoint.getArena()).add(spawnPoint);
        } else {
            List<PlayerSpawnPoint> spawn = new ArrayList<PlayerSpawnPoint>();
            spawn.add(spawnPoint);
            playerSpawnPoints.put(spawnPoint.getArena(), spawn);
        }
        ultimateGames.getServer().getPluginManager().registerEvents(spawnPoint, ultimateGames);
    }

    /**
     * Creates a new Spectator spawnpoint and adds it to the manager and config.
     *
     * @param arena    The spawnpoint's arena.
     * @param location The spawnpoint's location.
     */
    public void setSpectatorSpawnPoint(Arena arena, Location location) {
        List<String> newSpawnPoint = new ArrayList<String>();
        newSpawnPoint.add(String.valueOf(location.getX()));
        newSpawnPoint.add(String.valueOf(location.getY()));
        newSpawnPoint.add(String.valueOf(location.getZ()));
        newSpawnPoint.add(String.valueOf(location.getPitch()));
        newSpawnPoint.add(String.valueOf(location.getYaw()));
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        String path = "Arenas." + arena.getGame().getName() + "." + arena.getName() + ".SpectatorSpawnpoint";
        arenaConfig.set(path, newSpawnPoint);
        spectatorSpawnPoints.put(arena, new SpectatorSpawnPoint(arena, location));
    }

    /**
     * Creates a spawnpoint.
     * The spawnpoint is added to the manager and config.
     *
     * @param arena    The Arena.
     * @param location The location.
     * @param locked   If the spawnpoint prevents the player from moving off of it.
     * @return The spawnpoint created.
     */
    public PlayerSpawnPoint createSpawnPoint(Arena arena, Location location, Boolean locked) {
        List<String> newSpawnPoint = new ArrayList<String>();
        newSpawnPoint.add(String.valueOf(location.getX()));
        newSpawnPoint.add(String.valueOf(location.getY()));
        newSpawnPoint.add(String.valueOf(location.getZ()));
        newSpawnPoint.add(String.valueOf(location.getPitch()));
        newSpawnPoint.add(String.valueOf(location.getYaw()));
        newSpawnPoint.add(String.valueOf(locked));
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        String path = "Arenas." + arena.getGame().getName() + "." + arena.getName() + ".SpawnPoints";
        if (ultimateGames.getConfigManager().getArenaConfig().getConfig().contains(path)) {
            @SuppressWarnings("unchecked") List<List<String>> arenaSpawnPoints = (ArrayList<List<String>>) arenaConfig.getList(path);
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        } else {
            List<List<String>> arenaSpawnPoints = new ArrayList<List<String>>();
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        }
        ultimateGames.getConfigManager().getArenaConfig().saveConfig();
        PlayerSpawnPoint spawnPoint = new PlayerSpawnPoint(ultimateGames, arena, location, locked);
        addSpawnPoint(spawnPoint);
        return spawnPoint;
    }

    /**
     * Get a specific spawnpoint of an arena.
     *
     * @param arena The arena.
     * @param index The spawnpoint index.
     * @return The spawnpoint.
     */
    public PlayerSpawnPoint getSpawnPoint(Arena arena, Integer index) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= index) {
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Gets an arena's spectator spawnpoint.
     *
     * @param arena The arena.
     * @return The spawnpoint.
     */
    public SpectatorSpawnPoint getSpectatorSpawnPoint(Arena arena) {
        if (spectatorSpawnPoints.containsKey(arena)) {
            return spectatorSpawnPoints.get(arena);
        } else {
            return null;
        }
    }

    /**
     * Get a random spawnpoint of an arena.
     *
     * @param arena The arena.
     * @return The spawnpoint.
     */
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena) {
        if (playerSpawnPoints.containsKey(arena)) {
            Random generator = new Random();
            Integer index = generator.nextInt(playerSpawnPoints.get(arena).size());
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Get a random spawnpoint of an arena at or above the minIndex.
     * Useful if there are certain spawnpoints only used for specific things.
     *
     * @param arena    The arena.
     * @param minIndex The minimum index.
     * @return The spawnpoint.
     */
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() > minIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(playerSpawnPoints.get(arena).size() - minIndex) + minIndex;
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Get a random spawnpoint of an arena within the minIndex and maxIndex.
     * Useful if there are certain spawnpoints only used for specific things.
     *
     * @param arena    The arena.
     * @param minIndex The minimum index.
     * @param maxIndex The maximum index.
     * @return The spawnpoint.
     */
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex, Integer maxIndex) {
        if (playerSpawnPoints.containsKey(arena) && minIndex < maxIndex && playerSpawnPoints.get(arena).size() > maxIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(maxIndex - minIndex + 1) + minIndex;
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    /**
     * Get a certain amount of spawnpoints distributed evenly among the available spawnpoints.
     * Example: You have 8 spawn points. You ask for 4. It gives you the spawnpoints at indexes 0, 2, 4, and 6.
     * Example 2: You have 16 spawn points. You ask for 2. It gives you the spawnpoints at indexes 0 and 8.
     * Useful for spawnpoint setups similar to survival games where players need to be spaced out.
     *
     * @param arena  The arena.
     * @param amount The amount of spawnpoints to get.
     * @return The spawnpoints.
     */
    public List<PlayerSpawnPoint> getDistributedSpawnPoints(Arena arena, Integer amount) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= amount) {
            List<PlayerSpawnPoint> distributedSpawnPoints = new ArrayList<PlayerSpawnPoint>();
            Integer size = playerSpawnPoints.get(arena).size();
            Double multiple = (double) size / (double) amount;
            for (int i = 0; i < amount; i++) {
                Integer index = (int) Math.round(i * multiple);
                distributedSpawnPoints.add(playerSpawnPoints.get(arena).get(index));
            }
            return distributedSpawnPoints;
        }
        return null;
    }

    /**
     * Gets all the spawnpoints of an arena.
     *
     * @param arena The arena.
     * @return The spawnpoints.
     */
    public List<PlayerSpawnPoint> getSpawnPointsOfArena(Arena arena) {
        if (playerSpawnPoints.containsKey(arena)) {
            return playerSpawnPoints.get(arena);
        } else {
            return null;
        }
    }

    /**
     * Removes the spawnpoint at a certain index.
     *
     * @param arena The arena.
     * @param index The index.
     */
    public void removeSpawnPoint(Arena arena, Integer index) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= index) {
            playerSpawnPoints.get(arena).remove(playerSpawnPoints.get(arena).get(index));
            // TODO: Remove spawnpoint from arena config.
        }
    }

    /**
     * Removes the spawnpoints at the indexes.
     *
     * @param arena   The arena.
     * @param indexes The indexes.
     */
    public void removeSpawnPoints(Arena arena, Integer... indexes) {
        if (playerSpawnPoints.containsKey(arena)) {
            ArrayList<PlayerSpawnPoint> remove = new ArrayList<PlayerSpawnPoint>();
            for (Integer index : indexes) {
                if (playerSpawnPoints.get(arena).size() >= index) {
                    remove.add(playerSpawnPoints.get(arena).get(index));
                }
            }
            playerSpawnPoints.get(arena).removeAll(remove);
            // TODO: Remove spawnpoints from arena config.
        }
    }

    /**
     * Removes all the spawnpoints of an arena.
     *
     * @param arena The arena.
     */
    public void removeAllSpawnPoints(Arena arena) {
        if (playerSpawnPoints.containsKey(arena)) {
            playerSpawnPoints.remove(arena);
            // TODO: Remove spawnpoints from arena config.
        }
    }
}
