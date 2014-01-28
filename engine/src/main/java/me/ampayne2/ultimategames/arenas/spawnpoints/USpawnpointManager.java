/*
 * This file is part of UltimateGames ENGINE.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames ENGINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames ENGINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames ENGINE.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.arenas.spawnpoints;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.config.ConfigType;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class USpawnpointManager implements SpawnpointManager {
    private final UG ultimateGames;
    private Map<Arena, List<PlayerSpawnPoint>> playerSpawnPoints = new HashMap<>();
    private Map<Arena, SpectatorSpawnPoint> spectatorSpawnPoints = new HashMap<>();

    /**
     * Creates a new Spawnpoint Manager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public USpawnpointManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean hasSpawnPointAtIndex(Arena arena, Integer index) {
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
            List<PlayerSpawnPoint> spawn = new ArrayList<>();
            spawn.add(spawnPoint);
            playerSpawnPoints.put(spawnPoint.getArena(), spawn);
        }
    }

    /**
     * Creates a new Spectator spawnpoint and adds it to the manager and config.
     *
     * @param arena    The spawnpoint's arena.
     * @param location The spawnpoint's location.
     */
    public void setSpectatorSpawnPoint(Arena arena, Location location) {
        List<String> newSpawnPoint = new ArrayList<>();
        newSpawnPoint.add(String.valueOf(location.getX()));
        newSpawnPoint.add(String.valueOf(location.getY()));
        newSpawnPoint.add(String.valueOf(location.getZ()));
        newSpawnPoint.add(String.valueOf(location.getPitch()));
        newSpawnPoint.add(String.valueOf(location.getYaw()));
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getConfig(ConfigType.ARENA);
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
        List<String> newSpawnPoint = new ArrayList<>();
        newSpawnPoint.add(String.valueOf(location.getX()));
        newSpawnPoint.add(String.valueOf(location.getY()));
        newSpawnPoint.add(String.valueOf(location.getZ()));
        newSpawnPoint.add(String.valueOf(location.getPitch()));
        newSpawnPoint.add(String.valueOf(location.getYaw()));
        newSpawnPoint.add(String.valueOf(locked));
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getConfig(ConfigType.ARENA);
        String path = "Arenas." + arena.getGame().getName() + "." + arena.getName() + ".SpawnPoints";
        if (arenaConfig.contains(path)) {
            @SuppressWarnings("unchecked") List<List<String>> arenaSpawnPoints = (ArrayList<List<String>>) arenaConfig.getList(path);
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        } else {
            List<List<String>> arenaSpawnPoints = new ArrayList<>();
            arenaSpawnPoints.add(newSpawnPoint);
            arenaConfig.set(path, arenaSpawnPoints);
        }
        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
        PlayerSpawnPoint spawnPoint = new PlayerSpawnPoint(ultimateGames, arena, location, locked);
        addSpawnPoint(spawnPoint);
        return spawnPoint;
    }

    @Override
    public PlayerSpawnPoint getSpawnPoint(Arena arena, Integer index) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= index) {
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public SpectatorSpawnPoint getSpectatorSpawnPoint(Arena arena) {
        if (spectatorSpawnPoints.containsKey(arena)) {
            return spectatorSpawnPoints.get(arena);
        } else {
            return null;
        }
    }

    @Override
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena) {
        if (playerSpawnPoints.containsKey(arena)) {
            Random generator = new Random();
            Integer index = generator.nextInt(playerSpawnPoints.get(arena).size());
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() > minIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(playerSpawnPoints.get(arena).size() - minIndex) + minIndex;
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public PlayerSpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex, Integer maxIndex) {
        if (playerSpawnPoints.containsKey(arena) && minIndex < maxIndex && playerSpawnPoints.get(arena).size() > maxIndex) {
            Random generator = new Random();
            Integer index = generator.nextInt(maxIndex - minIndex + 1) + minIndex;
            return playerSpawnPoints.get(arena).get(index);
        }
        return null;
    }

    @Override
    public List<PlayerSpawnPoint> getDistributedSpawnPoints(Arena arena, Integer amount) {
        if (playerSpawnPoints.containsKey(arena) && playerSpawnPoints.get(arena).size() >= amount) {
            List<PlayerSpawnPoint> distributedSpawnPoints = new ArrayList<>();
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

    @Override
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
            ArrayList<PlayerSpawnPoint> remove = new ArrayList<>();
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
