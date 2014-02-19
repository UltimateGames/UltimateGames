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
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.config.ConfigType;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.games.PlayerType;
import me.ampayne2.ultimategames.api.signs.SignType;
import me.ampayne2.ultimategames.api.utils.UGUtils;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: Make a fancy system to store properties of the arena without a wall of code.
 */
public class UArena implements Listener, Arena {
    private final UG ultimateGames;
    private final Game game;
    private String arenaName;
    private ArenaStatus arenaStatus;
    private List<String> players = new ArrayList<>();
    private List<String> spectators = new ArrayList<>();
    private int minPlayers;
    private int maxPlayers;
    private boolean allowExplosionDamage;
    private boolean allowExplosionBlockBreaking;
    private boolean allowMobSpawning;
    private URegion region;
    private int timesPlayed = 0;
    private Map<String, Location> lastLocations = new HashMap<>();
    private static final int DEFAULT_MIN_PLAYERS = 4;
    private static final int DEFAULT_MAX_PLAYERS = 8;

    /**
     * Creates an arena from default settings.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     * @param game          The game of the arena.
     * @param arenaName     The name of the arena.
     * @param corner1       A corner of the arena.
     * @param corner2       The opposite corner of the arena.
     */
    public UArena(UG ultimateGames, Game game, String arenaName, Location corner1, Location corner2) {
        this.ultimateGames = ultimateGames;
        this.arenaName = arenaName;
        this.game = game;

        FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game);
        allowExplosionDamage = gameConfig.getBoolean("DefaultSettings.Allow-Explosion-Damage", false);
        allowExplosionBlockBreaking = gameConfig.getBoolean("DefaultSettings.Allow-Explosion-Block-Breaking", false);
        allowMobSpawning = gameConfig.getBoolean("DefaultSettings.Allow-Mob-Spawning", false);
        minPlayers = gameConfig.getInt("DefaultSettings.MinPlayers", DEFAULT_MIN_PLAYERS);
        arenaStatus = ArenaStatus.ARENA_STOPPED;

        if (game.getPlayerType() == PlayerType.SINGLE_PLAYER) {
            maxPlayers = 1;
        } else if (game.getPlayerType() == PlayerType.TWO_PLAYER) {
            maxPlayers = 2;
        } else if (game.getPlayerType() == PlayerType.CONFIGUREABLE) {
            maxPlayers = gameConfig.getInt("DefaultSettings.MaxPlayers", DEFAULT_MAX_PLAYERS);
        }

        region = URegion.fromCorners(corner1, corner2);

        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);
    }

    /**
     * Loads an arena from a ConfigurationSection.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     * @param game          The game of the arena.
     * @param section       The ConfigurationSection.
     */
    public UArena(UG ultimateGames, Game game, ConfigurationSection section) {
        this.ultimateGames = ultimateGames;
        this.arenaName = section.getName();
        this.game = game;

        arenaStatus = ArenaStatus.valueOf(section.getString("Status", "ARENA_STOPPED"));
        maxPlayers = section.getInt("Max-Players", DEFAULT_MAX_PLAYERS);
        minPlayers = section.getInt("Min-Players", DEFAULT_MIN_PLAYERS);
        region = URegion.fromList(section.getStringList("Region"));

        if (section.contains("Allow")) {
            ConfigurationSection allow = section.getConfigurationSection("Allow");
            allowExplosionDamage = allow.getBoolean("Explosion-Damage", false);
            allowExplosionBlockBreaking = allow.getBoolean("Explosion-Block-Breaking", false);
            allowMobSpawning = allow.getBoolean("Mob-Spawning", false);
        } else {
            allowExplosionDamage = section.getBoolean("Allow-Explosion-Damage", false);
            allowExplosionBlockBreaking = section.getBoolean("Allow-Explosion-Block-Breaking", false);
            allowMobSpawning = section.getBoolean("Allow-Mob-Spawning", false);
            section.set("Allow-Explosion-Damage", null);
            section.set("Allow-Explosion-Block-Breaking", null);
            section.set("Allow-Mob-Spawning", null);
            ConfigurationSection allow = section.createSection("Allow");
            allow.set("Explosion-Damage", allowExplosionDamage);
            allow.set("Explosion-Block-Breaking", allowExplosionBlockBreaking);
            allow.set("Mob-Spawning", allowMobSpawning);

            ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
        }

        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);
    }

    /**
     * Disables an arena.
     */
    public void disable() {
        for (String playerName : players) {
            ultimateGames.getPlayerManager().removePlayerFromArena(Bukkit.getPlayerExact(playerName), false);
        }
        for (String playerName : spectators) {
            ultimateGames.getPlayerManager().removeSpectatorFromArena(Bukkit.getPlayerExact(playerName));
        }
        setStatus(ArenaStatus.ARENA_STOPPED);
    }

    @Override
    public String getName() {
        return arenaName;
    }

    @Override
    public Game getGame() {
        return game;
    }

    /**
     * Adds a player to the arena's player list.
     *
     * @param playerName The player's name.
     * @return If it was successful.
     */
    public boolean addPlayer(String playerName) {
        if (game.getPlayerType() != PlayerType.INFINITE && players.size() >= maxPlayers) {
            return false;
        } else if (players.contains(playerName)) {
            return false;
        } else {
            players.add(playerName);
            ultimateGames.getSignManager().updateSignsOfArena(this, SignType.LOBBY);
            return true;
        }
    }

    /**
     * Adds a spectator to the arena.
     *
     * @param playerName The name of the spectator to add.
     * @return True if the spectator was added, else false.
     */
    public boolean addSpectator(String playerName) {
        return !spectators.contains(playerName) && spectators.add(playerName);
    }

    /**
     * Removes a player from the arena's player list.
     *
     * @param playerName The player's name.
     */
    public void removePlayer(String playerName) {
        if (players.contains(playerName)) {
            players.remove(playerName);
            ultimateGames.getSignManager().updateSignsOfArena(this, SignType.LOBBY);
        }
    }

    /**
     * Removes a spectator from the arena's spectator list.
     *
     * @param playerName The spectator's name.
     */
    public void removeSpectator(String playerName) {
        if (spectators.contains(playerName)) {
            spectators.remove(playerName);
            ultimateGames.getSignManager().updateSignsOfArena(this, SignType.LOBBY);
        }
    }

    @Override
    public boolean hasPlayer(String playerName) {
        return !players.isEmpty() && players.contains(playerName);
    }

    @Override
    public boolean hasSpectator(String playerName) {
        return !spectators.isEmpty() && spectators.contains(playerName);
    }

    @Override
    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public List<String> getSpectators() {
        return new ArrayList<>(spectators);
    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public ArenaStatus getStatus() {
        return arenaStatus;
    }

    @Override
    public boolean allowExplosionDamage() {
        return allowExplosionDamage;
    }

    @Override
    public boolean allowExplosionBlockBreaking() {
        return allowExplosionBlockBreaking;
    }

    @Override
    public boolean allowMobSpawning() {
        return allowMobSpawning;
    }

    @Override
    public URegion getRegion() {
        return region;
    }

    @Override
    public int getTimesPlayed() {
        return timesPlayed;
    }

    @Override
    public boolean locationIsInArena(Location location) {
        return location.getWorld().equals(region.getWorld()) && location.getX() >= region.getMinX() && location.getX() <= region.getMaxX() && location.getZ() >= region.getMinZ() && location.getZ() <= region.getMaxZ();
    }

    /**
     * Sets the status of the arena.
     *
     * @param status The status.
     */
    public void setStatus(ArenaStatus status) {
        if (!status.equals(arenaStatus)) {
            arenaStatus = status;
            getSection().set("Status", status.toString());
            ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
            if (ultimateGames.getSignManager() != null) {
                ultimateGames.getSignManager().updateSignsOfArena(this, SignType.LOBBY);
            }
            if (status == ArenaStatus.RUNNING) {
                timesPlayed += 1;
            }
            ultimateGames.getMessenger().debug("Set status of arena " + arenaName + " of game " + game.getName() + " to " + status.toString());
        }
    }

    /**
     * Handles player movement to keep players or spectators from leaving the arena.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (!(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) && players.contains(playerName)) {
            if ((to.getX() < region.getMinX() || to.getX() > region.getMaxX() || to.getZ() < region.getMinZ() || to.getZ() > region.getMaxZ()) && lastLocations.containsKey(playerName)) {
                Location lastLocation = lastLocations.get(playerName);
                lastLocation.setPitch(to.getPitch());
                lastLocation.setYaw(to.getYaw());
                UGUtils.teleportEntity(player, lastLocation);
                ultimateGames.getMessenger().sendMessage(player, "protections.leave");
            } else {
                lastLocations.put(playerName, to);
                game.getGamePlugin().onPlayerMove(this, event);
            }
        }
    }

    /**
     * Stops players and spectators from teleporting out of an arena.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if ((players.contains(playerName) || spectators.contains(playerName)) && !locationIsInArena(event.getTo())) {
            event.setCancelled(true);
            ultimateGames.getMessenger().sendMessage(player, "protections.leave");
        }
    }

    @Override
    public ConfigurationSection getSection() {
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getConfig(ConfigType.ARENA);
        String arenaPath = "Arenas." + game.getName() + "." + arenaName;
        return arenaConfig.contains(arenaPath) ? arenaConfig.getConfigurationSection(arenaPath) : arenaConfig.createSection(arenaPath);
    }

    /**
     * Saves the arena to its ConfigurationSection.
     */
    public void save() {
        ConfigurationSection section = getSection();
        section.set("Status", arenaStatus.toString());
        section.set("Max-Players", maxPlayers);
        section.set("Min-Players", minPlayers);
        section.set("Region", region.toList());

        ConfigurationSection allow = section.contains("Allow") ? section.getConfigurationSection("Allow") : section.createSection("Allow");
        allow.set("Explosion-Damage", allowExplosionDamage);
        allow.set("Explosion-Block-Breaking", allowExplosionBlockBreaking);
        allow.set("Mob-Spawning", allowMobSpawning);

        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UArena arena = (UArena) o;

        return arenaName.equalsIgnoreCase(arena.arenaName) && game.equals(arena.game);
    }

    @Override
    public int hashCode() {
        int result = game.hashCode();
        result = 31 * result + arenaName.hashCode();
        return result;
    }
}
