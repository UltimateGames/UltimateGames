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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.players.SpawnPoint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ArenaManager {
    private UltimateGames ultimateGames;
    private HashMap<Game, ArrayList<Arena>> arenas;

    public ArenaManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        arenas = new HashMap<Game, ArrayList<Arena>>();
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        for (String gameKey : arenaConfig.getConfigurationSection("Arenas").getKeys(false)) {
            if (ultimateGames.getGameManager().gameExists(gameKey)) {
                String gamePath = "Arenas." + gameKey;
                for (String arenaKey : arenaConfig.getConfigurationSection(gamePath).getKeys(false)) {
                    if (!arenaExists(arenaKey, gameKey)) {
                        String arenaPath = gamePath + "." + arenaKey;
                        World world = Bukkit.getWorld(arenaConfig.getString(arenaPath + ".Arena-Location.world"));
                        Location minLocation = new Location(world, arenaConfig.getInt(arenaPath + ".Arena-Location.minx"), arenaConfig.getInt(arenaPath + ".Arena-Location.miny"), arenaConfig
                                .getInt(arenaPath + ".Arena-Location.minz"));
                        Location maxLocation = new Location(world, arenaConfig.getInt(arenaPath + ".Arena-Location.maxx"), arenaConfig.getInt(arenaPath + ".Arena-Location.maxy"), arenaConfig
                                .getInt(arenaPath + ".Arena-Location.maxz"));
                        Arena arena = new Arena(ultimateGames, ultimateGames.getGameManager().getGame(gameKey), arenaKey, minLocation, maxLocation);
                        arena.setStatus(ArenaStatus.valueOf(arenaConfig.getString(arenaPath + ".Status")));
                        addArena(arena);
                        Bukkit.getServer().getPluginManager().registerEvents(arena, ultimateGames);
                        if (arenaConfig.contains(arenaPath + ".SpawnPoints")) {
                            @SuppressWarnings("unchecked")
                            ArrayList<ArrayList<String>> spawnPoints = (ArrayList<ArrayList<String>>) arenaConfig.getList(arenaPath + ".SpawnPoints");
                            if (!spawnPoints.isEmpty()) {
                                for (ArrayList<String> spawnPoint : spawnPoints) {
                                    if (!spawnPoints.isEmpty()) {
                                        Double x = Double.valueOf(spawnPoint.get(0));
                                        Double y = Double.valueOf(spawnPoint.get(1));
                                        Double z = Double.valueOf(spawnPoint.get(2));
                                        Float pitch = Float.valueOf(spawnPoint.get(3));
                                        Float yaw = Float.valueOf(spawnPoint.get(4));
                                        Location location = new Location(world, x, y, z);
                                        location.setPitch(pitch);
                                        location.setYaw(yaw);
                                        SpawnPoint newSpawnPoint = new SpawnPoint(ultimateGames, getArena(arenaKey, gameKey), location, Boolean.valueOf(spawnPoint.get(5)));
                                        ultimateGames.getSpawnpointManager().addSpawnPoint(newSpawnPoint);
                                    }
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
     * @param arenaName The arena's name.
     * @param gameName The arena's game's name.
     * @return If the arena exists or not.
     */
    public boolean arenaExists(String arenaName, String gameName) {
        if (ultimateGames.getGameManager().gameExists(gameName) && arenas.containsKey(ultimateGames.getGameManager().getGame(gameName))) {
            ArrayList<Arena> gameArenas = arenas.get(ultimateGames.getGameManager().getGame(gameName));
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
    public Boolean isLocationInArena(Location location) {
        if (getLocationArena(location) == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the arena a location is inside of.
     * @param location The location.
     * @return The arena. Null if location isn't inside arena.
     */
    public Arena getLocationArena(Location location) {
        Iterator<Entry<Game, ArrayList<Arena>>> it = arenas.entrySet().iterator();
        while (it.hasNext()) {
            ArrayList<Arena> gameArenas = it.next().getValue();
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
    public ArrayList<Arena> getArenasOfGame(String gameName) {
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
                ArrayList<Arena> gameArenas = new ArrayList<Arena>();
                gameArenas.add(arena);
                arenas.put(arena.getGame(), gameArenas);
            }
        }
    }

    /**
     * Opens an arena.
     * @param arena The arena.
     */
    public void openArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getGameDescription().getName())) {
            if (arena.getGame().getGamePlugin().openArena(arena)) {
                arena.setStatus(ArenaStatus.OPEN);
                ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
            }
        }
    }

    /**
     * Starts the arena
     * @param arena
     */
    public void startArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getGameDescription().getName())) {
            if (arena.getGame().getGamePlugin().isStartPossible(arena) && arena.getGame().getGamePlugin().startArena(arena)) {
                arena.setStatus(ArenaStatus.STARTING);
                ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
            }
        }
    }

    /**
     * Begins an arena.
     * @param arena The arena.
     */
    public void beginArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getGameDescription().getName())) {
            arena.setStatus(ArenaStatus.RUNNING);
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
            if (arena.getGame().getGamePlugin().beginArena(arena)) {
                ultimateGames.getMessageManager().broadcastMessageToArena(arena, "arenas.begin");
            }
        }
    }

    /**
     * Ends an arena.
     * @param arena The arena.
     */
    public void endArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getGameDescription().getName())) {
            arena.setStatus(ArenaStatus.ENDING);
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
            if (arena.getGame().getGamePlugin().endArena(arena)) {
                ultimateGames.getMessageManager().broadcastMessageToArena(arena, "arenas.end");
            }
        }
    }

    /**
     * Stops an arena.
     * @param arena The arena.
     */
    public void stopArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getGameDescription().getName())) {
            arena.setStatus(ArenaStatus.ARENA_STOPPED);
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
            arena.getGame().getGamePlugin().stopArena(arena);
        }
    }
}
