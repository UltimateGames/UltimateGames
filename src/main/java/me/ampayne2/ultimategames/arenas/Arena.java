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
import java.util.List;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.enums.PlayerType;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

/**
 * Represents a UltimateGames Arena.
 */
public class Arena implements Listener {

    private boolean enabled = true;
    private UltimateGames ultimateGames;
    private Game game;
    private String arenaName;
    private ArenaStatus arenaStatus = ArenaStatus.OPEN;
    private List<String> players = new ArrayList<String>();
    private List<String> spectators = new ArrayList<String>();
    private int minPlayers;
    private int maxPlayers;
    private boolean storeInventory;
    private boolean storeArmor;
    private boolean storeExp;
    private boolean storeEffects;
    private boolean storeGamemode;
    private boolean resetAfterMatch;
    private boolean allowExplosionDamage;
    private boolean allowExplosionBlockBreaking;
    private boolean allowMobSpawning;
    private World arenaWorld;
    private Double minX;
    private Double maxX;
    private Double minZ;
    private Double maxZ;
    private int timesPlayed;
    private static final int DEFAULT_MIN_PLAYERS = 4;
    private static final int DEFAULT_MAX_PLAYERS = 8;
    private static final String PATH_SEPARATOR = ".";

    public Arena(UltimateGames ultimateGames, Game game, String arenaName, Location corner1, Location corner2) {
        this.ultimateGames = ultimateGames;
        this.arenaName = arenaName;
        this.game = game;
        FileConfiguration gamesConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        String arenaPath = "Arenas." + game.getName() + "." + arenaName;
        // Get all arena information. Tries to get from arena config, if doesn't exist there then gets from default game settings, if doesn't exist there then is set specifically to true/false
        storeInventory = arenaConfig.getBoolean(arenaPath + ".Players.Store-Inventory", gamesConfig.getBoolean("DefaultSettings.Store-Inventory", true));
        storeArmor = arenaConfig.getBoolean(arenaPath + ".Players.Store-Armor", gamesConfig.getBoolean("DefaultSettings.Store-Armor", true));
        storeExp = arenaConfig.getBoolean(arenaPath + ".Players.Store-Exp", gamesConfig.getBoolean("DefaultSettings.Store-Exp", true));
        storeEffects = arenaConfig.getBoolean(arenaPath + ".Players.Store-Effects", gamesConfig.getBoolean("DefaultSettings.Store-Effects", true));
        storeGamemode = arenaConfig.getBoolean(arenaPath + ".Players.Store-Gamemode", gamesConfig.getBoolean("DefaultSettings.Store-Gamemode", true));
        resetAfterMatch = arenaConfig.getBoolean(arenaPath + ".Reset-After-Match", gamesConfig.getBoolean("DefaultSettings.Reset-After-Match", true));
        allowExplosionDamage = arenaConfig.getBoolean(arenaPath + ".Allow-Explosion-Damage", gamesConfig.getBoolean("DefaultSettings.Allow-Explosion-Damage", false));
        allowExplosionBlockBreaking = arenaConfig.getBoolean(arenaPath + ".Allow-Explosion-Block-Breaking", gamesConfig.getBoolean("DefaultSettings.Allow-Explosion-Block-Breaking", false));
        allowMobSpawning = arenaConfig.getBoolean(arenaPath + ".Allow-Mob-Spawning", gamesConfig.getBoolean("DefaultSettings.Allow-Mob-Spawning", false));
        minPlayers = arenaConfig.getInt(arenaPath + ".Min-Players", gamesConfig.getInt("DefaultSettings.MinPlayers", DEFAULT_MIN_PLAYERS));
        arenaStatus = ArenaStatus.valueOf(arenaConfig.getString(arenaPath + ".Status", "ARENA_STOPPED"));
        if (game.getPlayerType() == PlayerType.SINGLE_PLAYER) {
            maxPlayers = 1;
        } else if (game.getPlayerType() == PlayerType.TWO_PLAYER) {
            maxPlayers = 2;
        } else if (game.getPlayerType() == PlayerType.CONFIGUREABLE) {
            maxPlayers = arenaConfig.getInt(arenaPath + ".Max-Players", gamesConfig.getInt("DefaultSettings.MaxPlayers", DEFAULT_MAX_PLAYERS));
        }
        // takes the 2 corners and turns them into minLocation and maxLocation
        arenaWorld = corner1.getWorld();
        maxX = corner1.getX() > corner2.getX() ? corner1.getX() : corner2.getX();
        minX = corner1.getX() < corner2.getX() ? corner1.getX() : corner2.getX();
        maxZ = corner1.getZ() > corner2.getZ() ? corner1.getZ() : corner2.getZ();
        minZ = corner1.getZ() < corner2.getZ() ? corner1.getZ() : corner2.getZ();

        // create the arena in the config if it doesn't exist
        if (arenaConfig.getConfigurationSection(arenaPath) == null) {
            arenaConfig.set(arenaPath + ".Status", "ARENA_STOPPED");
            arenaConfig.set(arenaPath + ".Max-Players", maxPlayers);
            arenaConfig.set(arenaPath + ".Min-Players", minPlayers);
            arenaConfig.set(arenaPath + ".Players.Store-Inventory", storeInventory);
            arenaConfig.set(arenaPath + ".Players.Store-Armor", storeArmor);
            arenaConfig.set(arenaPath + ".Players.Store-Exp", storeExp);
            arenaConfig.set(arenaPath + ".Players.Store-Effects", storeEffects);
            arenaConfig.set(arenaPath + ".Players.Store-Gamemode", storeGamemode);
            arenaConfig.set(arenaPath + ".Reset-After-Match", resetAfterMatch);
            arenaConfig.set(arenaPath + ".Allow-Explosion-Damage", allowExplosionDamage);
            arenaConfig.set(arenaPath + ".Allow-Explosion-Block-Breaking", allowExplosionBlockBreaking);
            arenaConfig.set(arenaPath + ".Allow-Mob-Spawning", allowMobSpawning);
            arenaConfig.set(arenaPath + ".Arena-Location.world", arenaWorld.getName());
            arenaConfig.set(arenaPath + ".Arena-Location.minx", minX);
            arenaConfig.set(arenaPath + ".Arena-Location.maxx", maxX);
            arenaConfig.set(arenaPath + ".Arena-Location.minz", minZ);
            arenaConfig.set(arenaPath + ".Arena-Location.maxz", maxZ);
        }
        ultimateGames.getConfigManager().getGameConfig(game).saveConfig();
        ultimateGames.getConfigManager().getArenaConfig().saveConfig();
        timesPlayed = 0;
    }

    /**
     * Checks to see if an arena is enabled.
     * @return True if the arena is enabled.
     */
    public boolean isEnabled() {
        return enabled;
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
        enabled = false;
    }

    /**
     * Gets the name of an arena.
     * @return The name.
     */
    public String getName() {
        return arenaName;
    }

    /**
     * Gets the game of an arena.
     * @return The game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Adds a player to the arena's player list.
     * @param playerName The player's name.
     * @return If it was successful.
     */
    public boolean addPlayer(String playerName) {
        if (enabled) {
            if (game.getPlayerType() != PlayerType.INFINITE && players.size() >= maxPlayers) {
                return false;
            } else if (players.contains(playerName)) {
                return false;
            } else {
                players.add(playerName);
                ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Adds a spectator to the arena.
     * @param playerName The name of the spectator to add.
     * @return True if the spectator was added, else false.
     */
    public boolean addSpectator(String playerName) {
        return enabled && !spectators.contains(playerName) && spectators.add(playerName);
    }

    /**
     * Removes a player from the arena's player list.
     * @param playerName The player's name.
     */
    public void removePlayer(String playerName) {
        if (enabled && players.contains(playerName)) {
            players.remove(playerName);
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
        }
    }

    /**
     * Removes a spectator from the arena's spectator list.
     * @param playerName The spectator's name.
     */
    public void removeSpectator(String playerName) {
        if (enabled && spectators.contains(playerName)) {
            spectators.remove(playerName);
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
        }
    }

    /**
     * Removes all of the arena's players.
     */
    public void removePlayers() {
        if (enabled && !players.isEmpty()) {
            players.clear();
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
        }
    }

    /**
     * Removes all of the arena's spectators.
     */
    public void removeSpectators() {
        if (enabled && !spectators.isEmpty()) {
            spectators.clear();
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
        }
    }

    /**
     * Checks to see if the arena has a certain player.
     * @param playerName The player's name.
     * @return True if the arena has the player, else false.
     */
    public boolean hasPlayer(String playerName) {
        return enabled && !players.isEmpty() && players.contains(playerName);
    }

    /**
     * Checks to see if the arena has a certain spectator.
     * @param playerName The spectator's name.
     * @return True if the arena has the spectator, else false.
     */
    public boolean hasSpectator(String playerName) {
        return enabled && !spectators.isEmpty() && spectators.contains(playerName);
    }

    /**
     * Gets the players in the arena.
     * @return The players.
     */
    public List<String> getPlayers() {
        return enabled ? new ArrayList<String>(players) : new ArrayList<String>();
    }

    /**
     * Gets the spectators in the arena.
     * @return The spectators.
     */
    public List<String> getSpectators() {
        return enabled ? new ArrayList<String>(spectators) : new ArrayList<String>();
    }

    /**
     * Gets the min players of the arena.
     * @return The minimum amount of players.
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Gets the max players of the arena.
     * @return The maximum amount of players.
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Gets the status of the arena.
     * @return The status.
     */
    public ArenaStatus getStatus() {
        return arenaStatus;
    }

    /**
     * Gets the storeInventory setting.
     * @return True if the game stores your inventory else false.
     */
    public boolean storeInventory() {
        return storeInventory;
    }

    /**
     * Gets the storeArmor setting.
     * @return True if the game stores your armor else false.
     */
    public boolean storeArmor() {
        return storeArmor;
    }

    /**
     * Gets the storeExp setting.
     * @return True if the game stores your exp else false.
     */
    public boolean storeExp() {
        return storeExp;
    }

    /**
     * Gets the storeEffects setting.
     * @return True if the game stores your effects else false.
     */
    public boolean storeEffects() {
        return storeEffects;
    }

    /**
     * Gets the storeGamemode setting.
     * @return True if the game stores your gamemode else false.
     */
    public boolean storeGamemode() {
        return storeGamemode;
    }

    /**
     * Gets the resetAfterMatch setting.
     * @return True if the game resets after each match else false.
     */
    public boolean resetAfterMatch() {
        return resetAfterMatch;
    }

    /**
     * Gets the allowExplosionDamage setting.
     * @return True if the game allows explosion damage else false.
     */
    public boolean allowExplosionDamage() {
        return allowExplosionDamage;
    }

    /**
     * Gets the allowExplosionBlockBreaking setting.
     * @return True if the game allows explosion block breaking else false.
     */
    public boolean allowExplosionBlockBreaking() {
        return allowExplosionBlockBreaking;
    }

    /**
     * Gets the allowMobSpawning setting.
     * @return True if the game allows mob spawning else false.
     */
    public boolean allowMobSpawning() {
        return allowMobSpawning;
    }

    /**
     * Gets the world of the arena.
     * @return The world.
     */
    public World getWorld() {
        return arenaWorld;
    }

    /**
     * Gets the amount of times played.
     * @return The amount of times played.
     */
    public int getTimesPlayed() {
        return timesPlayed;
    }

    /**
     * Sets the status of the arena.
     * @param status The status.
     */
    public void setStatus(ArenaStatus status) {
        if (enabled) {
            arenaStatus = status;
            FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
            arenaConfig.set("Arenas." + game.getName() + PATH_SEPARATOR + arenaName + ".Status", status.toString());
            ultimateGames.getConfigManager().getArenaConfig().saveConfig();
            if (ultimateGames.getUGSignManager() != null) {
                ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
            }
            if (status == ArenaStatus.RUNNING) {
                timesPlayed += 1;
            }
        }
    }

    /**
     * Checks to see if a location is inside the arena.
     * @param location The location.
     * @return If the location is inside the arena or not.
     */
    public boolean locationIsInArena(Location location) {
        return enabled && location.getWorld().equals(arenaWorld) && location.getX() >= minX && location.getX() <= maxX && location.getZ() >= minZ && location.getZ() <= maxZ;
    }

    /**
     * Handles player movement to keep players or spectators from leaving the arena.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();
        if (!(from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) && players.contains(player.getName())) {
            boolean left = false;
            int x = 0;
            int z = 0;
            if (to.getX() < minX) {
                x = (int) (minX - to.getX());
                left = true;
            } else if (to.getX() > maxX) {
                x = (int) (maxX - to.getX());
                left = true;
            }
            if (to.getZ() < minZ) {
                z = (int) (minZ - to.getZ());
                left = true;
            } else if (to.getZ() > maxZ) {
                z = (int) (maxZ - to.getZ());
                left = true;
            }
            if (left) {
                Vector vector = new Vector(x, 0, z);
                player.setVelocity(vector);
                ultimateGames.getMessageManager().sendMessage(player, "protections.leave");
            }
        }
    }

    /**
     * Stops players and spectators from teleporting out of an arena.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (enabled) {
            Player player = event.getPlayer();
            String playerName = player.getName();
            if ((players.contains(playerName) || spectators.contains(playerName)) && !locationIsInArena(event.getTo())) {
                event.setCancelled(true);
                ultimateGames.getMessageManager().sendMessage(player, "protections.leave");
            }
        }
    }
}
