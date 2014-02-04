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
package me.ampayne2.ultimategames.core.arenas;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaManager;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.arenas.EndType;
import me.ampayne2.ultimategames.api.arenas.spawnpoints.PlayerSpawnPoint;
import me.ampayne2.ultimategames.api.config.ConfigType;
import me.ampayne2.ultimategames.api.events.arenas.*;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.players.teams.Team;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.arenas.zones.UZone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UArenaManager implements ArenaManager {
    private final UG ultimateGames;
    private Map<Game, List<Arena>> arenas = new HashMap<>();
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;
    private static final int Z_INDEX = 2;
    private static final int PITCH_INDEX = 3;
    private static final int YAW_INDEX = 4;
    private static final int LOCKED_INDEX = 5;

    /**
     * Creates a new ArenaManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UArenaManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getConfig(ConfigType.ARENA);
        if (arenaConfig.getConfigurationSection("Arenas") != null) {
            for (String gameKey : arenaConfig.getConfigurationSection("Arenas").getKeys(false)) {
                if (ultimateGames.getGameManager().gameExists(gameKey)) {
                    String gamePath = "Arenas." + gameKey;
                    for (String arenaKey : arenaConfig.getConfigurationSection(gamePath).getKeys(false)) {
                        if (!arenaExists(arenaKey, gameKey)) {
                            String arenaPath = gamePath + "." + arenaKey;
                            UArena arena = new UArena(ultimateGames, ultimateGames.getGameManager().getGame(gameKey), arenaConfig.getConfigurationSection(arenaPath));
                            addArena(arena);
                            if (arenaConfig.contains(arenaPath + ".SpawnPoints")) {
                                @SuppressWarnings("unchecked") List<ArrayList<String>> spawnPoints = (ArrayList<ArrayList<String>>) arenaConfig.getList(arenaPath + ".SpawnPoints");
                                if (!spawnPoints.isEmpty()) {
                                    for (ArrayList<String> spawnPoint : spawnPoints) {
                                        Double x = Double.valueOf(spawnPoint.get(X_INDEX));
                                        Double y = Double.valueOf(spawnPoint.get(Y_INDEX));
                                        Double z = Double.valueOf(spawnPoint.get(Z_INDEX));
                                        Float pitch = Float.valueOf(spawnPoint.get(PITCH_INDEX));
                                        Float yaw = Float.valueOf(spawnPoint.get(YAW_INDEX));
                                        Location location = new Location(arena.getRegion().getWorld(), x, y, z);
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
                                    Location location = new Location(arena.getRegion().getWorld(), x, y, z);
                                    location.setPitch(pitch);
                                    location.setYaw(yaw);
                                    ultimateGames.getSpawnpointManager().setSpectatorSpawnPoint(getArena(arenaKey, gameKey), location);
                                }
                            }
                            String zonesPath = arenaPath + ".Zones";
                            if (arenaConfig.contains(zonesPath)) {
                                for (String zoneKey : arenaConfig.getConfigurationSection(zonesPath).getKeys(false)) {
                                    String zonePath = zonesPath + "." + zoneKey;
                                    ultimateGames.getZoneManager().addZone(new UZone(ultimateGames, arena, arenaConfig.getConfigurationSection(zonePath)));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean arenaExists(String arenaName, String gameName) {
        if (ultimateGames.getGameManager().gameExists(gameName) && arenas.containsKey(ultimateGames.getGameManager().getGame(gameName))) {
            List<Arena> gameArenas = arenas.get(ultimateGames.getGameManager().getGame(gameName));
            for (Arena arena : gameArenas) {
                if (arenaName.equalsIgnoreCase(arena.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isLocationInArena(Location location) {
        return getLocationArena(location) == null;
    }

    @Override
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

    @Override
    public Arena getArena(String arenaName, String gameName) {
        if (ultimateGames.getGameManager().gameExists(gameName)) {
            for (Arena arena : arenas.get(ultimateGames.getGameManager().getGame(gameName))) {
                if (arenaName.equalsIgnoreCase(arena.getName())) {
                    return arena;
                }
            }
        }
        return null;
    }

    @Override
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
    public void addArena(UArena arena) {
        arena.save();
        if (arena.getGame().getGamePlugin().loadArena(arena)) {
            if (arenas.containsKey(arena.getGame())) {
                arenas.get(arena.getGame()).add(arena);
            } else {
                List<Arena> gameArenas = new ArrayList<>();
                gameArenas.add(arena);
                arenas.put(arena.getGame(), gameArenas);
            }
            ultimateGames.getMetricsManager().addArena(arena);
        }
    }

    @Override
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
                    case ARENA_STOPPED:
                        if (arena.getGame().getGamePlugin().openArena(arena)) {
                            ((UArena) arena).setStatus(ArenaStatus.OPEN);
                            for (String playerName : ultimateGames.getQueueManager().getNextPlayers(arena.getMaxPlayers() - arena.getPlayers().size(), arena)) {
                                ultimateGames.getPlayerManager().addPlayerToArena(Bukkit.getPlayerExact(playerName), arena, true);
                            }
                            ultimateGames.getMessenger().debug("Opened arena " + arena.getName() + " of game " + arena.getGame().getName());
                        }
                        break;
                    default:
                }
            }
        }
    }

    @Override
    public void startArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            ArenaStartEvent event = new ArenaStartEvent(arena);
            if (!event.isCancelled()) {
                switch (arena.getStatus()) {
                    case RUNNING:
                        endArena(arena);
                    case ARENA_STOPPED:
                        openArena(arena);
                    case OPEN:
                        if (arena.getGame().getGamePlugin().isStartPossible(arena)) {
                            ((UArena) arena).setStatus(ArenaStatus.STARTING);
                            arena.getGame().getGamePlugin().startArena(arena);
                            ultimateGames.getMessenger().debug("Started arena " + arena.getName() + " of game " + arena.getGame().getName());
                        }
                    default:
                }
            }
        }
    }

    @Override
    public void beginArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            ArenaBeginEvent event = new ArenaBeginEvent(arena);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                switch (arena.getStatus()) {
                    case RUNNING:
                        endArena(arena);
                    case ARENA_STOPPED:
                        openArena(arena);
                    case OPEN:
                        startArena(arena);
                    case STARTING:
                        if (arena.getGame().getGamePlugin().beginArena(arena)) {
                            ((UArena) arena).setStatus(ArenaStatus.RUNNING);
                            ultimateGames.getMessenger().sendMessage(arena, "arenas.begin");
                            ultimateGames.getMessenger().debug("Began arena " + arena.getName() + " of game " + arena.getGame().getName());
                        }
                    default:
                }
            }
        }
    }

    @Override
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
                    ((UArena) arena).setStatus(ArenaStatus.ENDING);

                    if (ultimateGames.getCountdownManager().hasStartingCountdown(arena)) {
                        ultimateGames.getCountdownManager().stopStartingCountdown(arena);
                    }
                    if (ultimateGames.getCountdownManager().hasEndingCountdown(arena)) {
                        ultimateGames.getCountdownManager().stopEndingCountdown(arena);
                    }
                    arena.getGame().getGamePlugin().endArena(arena);

                    ultimateGames.getScoreboardManager().removeScoreboard(arena);

                    for (Team team : ultimateGames.getTeamManager().getTeamsOfArena(arena)) {
                        team.removePlayers();
                    }

                    ultimateGames.getMessenger().sendMessage(arena, "arenas.end");

                    // Teleport everybody out of the arena
                    for (String playerName : arena.getSpectators()) {
                        ultimateGames.getPlayerManager().removeSpectatorFromArena(Bukkit.getPlayerExact(playerName));
                    }
                    for (String playerName : arena.getPlayers()) {
                        ultimateGames.getPlayerManager().removePlayerFromArena(Bukkit.getPlayerExact(playerName), false);
                    }

                    ultimateGames.getMessenger().debug("Ended arena " + arena.getName() + " of game " + arena.getGame().getName());
                    Bukkit.getPluginManager().callEvent(event);
                    ((UArena) arena).setStatus(ArenaStatus.ARENA_STOPPED);
                    openArena(arena);
                default:
            }
        }
    }

    @Override
    public void stopArena(Arena arena) {
        if (arenaExists(arena.getName(), arena.getGame().getName())) {
            switch (arena.getStatus()) {
                case STARTING:
                case RUNNING:
                    endArena(arena);
                case OPEN:
                    ((UArena) arena).setStatus(ArenaStatus.ARENA_STOPPED);
                    arena.getGame().getGamePlugin().stopArena(arena);
                    ultimateGames.getMessenger().debug("Stopped arena " + arena.getName() + " of game " + arena.getGame().getName());
                    Bukkit.getPluginManager().callEvent(new ArenaStopEvent(arena));
                default:
            }
        }
    }

    @Override
    public List<Arena> getArenas() {
        List<Arena> arenaList = new ArrayList<>();
        for (Entry<Game, List<Arena>> entry : arenas.entrySet()) {
            arenaList.addAll(entry.getValue());
        }
        return arenaList;
    }
}
