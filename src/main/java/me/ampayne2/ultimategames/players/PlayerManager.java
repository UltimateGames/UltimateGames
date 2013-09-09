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
package me.ampayne2.ultimategames.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.SpawnPoint;
import me.ampayne2.ultimategames.classes.GameClass;
import me.ampayne2.ultimategames.events.ArenaJoinEvent;
import me.ampayne2.ultimategames.scoreboards.ArenaScoreboard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerManager implements Listener, Manager {

    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private Map<String, ArenaPlayer> players = new HashMap<String, ArenaPlayer>();
    private Map<String, ArenaSpectator> spectators = new HashMap<String, ArenaSpectator>();
    private static final String LIMBO = "limbo";

    public PlayerManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean load() {
        return reload();
    }

    @Override
    public boolean reload() {
        // Remove all spectators from arenas
        Iterator<Entry<String, ArenaPlayer>> playerIterator = players.entrySet().iterator();
        while (playerIterator.hasNext()) {
            Entry<String, ArenaPlayer> entry = (Entry<String, ArenaPlayer>) playerIterator.next();
            removePlayerFromArena(Bukkit.getPlayerExact(entry.getKey()), false);
        }

        // Remove all players from arenas
        Iterator<Entry<String, ArenaSpectator>> spectatorIterator = spectators.entrySet().iterator();
        while (spectatorIterator.hasNext()) {
            Entry<String, ArenaSpectator> entry = (Entry<String, ArenaSpectator>) spectatorIterator.next();
            removeSpectatorFromArena(Bukkit.getPlayerExact(entry.getKey()));
        }
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        // Remove all spectators from arenas
        Iterator<Entry<String, ArenaPlayer>> playerIterator = players.entrySet().iterator();
        while (playerIterator.hasNext()) {
            Entry<String, ArenaPlayer> entry = (Entry<String, ArenaPlayer>) playerIterator.next();
            removePlayerFromArena(Bukkit.getPlayerExact(entry.getKey()), false);
        }

        // Remove all players from arenas
        Iterator<Entry<String, ArenaSpectator>> spectatorIterator = spectators.entrySet().iterator();
        while (spectatorIterator.hasNext()) {
            Entry<String, ArenaSpectator> entry = (Entry<String, ArenaSpectator>) spectatorIterator.next();
            removeSpectatorFromArena(Bukkit.getPlayerExact(entry.getKey()));
        }
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Checks to see if a player is in an arena.
     * @param playerName The player's name.
     * @return If the player is in an arena or not.
     */
    public boolean isPlayerInArena(String playerName) {
        return players.containsKey(playerName);
    }

    /**
     * Checks to see if a player is spectating an arena.
     * @param playerName The player's name.
     * @return If the player is in an arena or not.
     */
    public boolean isPlayerSpectatingArena(String playerName) {
        return spectators.containsKey(playerName);
    }

    /**
     * Gets the arena a player is in or spectating.
     * @param playerName The player's name.
     * @return The arena a player is in or spectating. Null if the player isn't in or spectating an arena.
     */
    public Arena getPlayerArena(String playerName) {
        if (players.containsKey(playerName)) {
            return players.get(playerName).getArena();
        } else if (spectators.containsKey(playerName)) {
            return spectators.get(playerName).getArena();
        } else {
            return null;
        }
    }

    /**
     * Turns an ArenaPlayer into an ArenaSpectator.
     * @param player The player to make into a spectator.
     */
    public void makePlayerSpectator(Player player) {
        String playerName = player.getName();
        if (players.containsKey(playerName)) {
            Arena arena = players.get(playerName).getArena();

            // Remove the player from the arena
            players.remove(playerName);
            arena.removePlayer(playerName);

            // Show all the spectators to the player
            for (String spectator : arena.getSpectators()) {
                player.showPlayer(Bukkit.getPlayerExact(spectator));
            }

            // Make the player a spectator
            spectators.put(playerName, new ArenaSpectator(playerName, arena));
            arena.addSpectator(playerName);
            arena.getGame().getGamePlugin().makePlayerSpectator(player, arena);

            // Hide the spectator from all arena players
            for (String normalPlayer : arena.getPlayers()) {
                Bukkit.getPlayer(normalPlayer).hidePlayer(player);
            }

            // Remove all of the player's potion effects
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            // Give the player flight
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    /**
     * Adds a player to an arena.
     * @param playerName The player's name.
     * @param arena The arena.
     */
    public void addPlayerToArena(Player player, Arena arena, Boolean sendMessage) {
        String playerName = player.getName();
        if (!players.containsKey(playerName) && arena.getPlayers().size() < arena.getMaxPlayers()) {
            // Teleport the player to the lobby
            player.teleport(ultimateGames.getLobbyManager().getLobby());

            // Add the player to the arena and make the player an ArenaPlayer object
            if (arena.addPlayer(player.getName()) && arena.getGame().getGamePlugin().addPlayer(player, arena)) {
                players.put(playerName, new ArenaPlayer(playerName, arena));

                // Fire an arenaJoinEvent
                ArenaJoinEvent arenaJoinEvent = new ArenaJoinEvent(player, arena);
                Bukkit.getServer().getPluginManager().callEvent(arenaJoinEvent);

                // Update the arena's lobby signs
                ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);

                // Send a message that the player joined to the arena
                if (sendMessage) {
                    ultimateGames.getMessageManager().broadcastReplacedMessageToArena(arena, "arenas.join", playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
                }

                // Add the player to all of the arena's scoreboards
                ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
                if (scoreBoard != null) {
                    scoreBoard.addPlayer(player);
                }

                // Remove player from all queues
                ultimateGames.getQueueManager().removePlayerFromQueues(player);

                // Hide all spectators from the player
                for (String spectator : arena.getSpectators()) {
                    Bukkit.getPlayerExact(spectator).hidePlayer(player);
                }

                // Extinguish the player if on fire
                player.setFireTicks(0);

                // Add the player to limbo in case of server crash or disconnect
                addPlayerToLimbo(player);
            }
        }
    }

    /**
     * Adds a spectator to an arean.
     * @param player The spectator's name.
     * @param arena The arena.
     */
    public void addSpectatorToArena(Player player, Arena arena) {
        String playerName = player.getName();
        if (!spectators.containsKey(playerName)) {
            // Teleport the spectator to the lobby
            player.teleport(ultimateGames.getLobbyManager().getLobby());

            // Add the spectator to the arena and make the spectator an ArenaSpectator object
            if (arena.addSpectator(playerName) && arena.getGame().getGamePlugin().addSpectator(player, arena)) {
                spectators.put(playerName, new ArenaSpectator(playerName, arena));

                // Add the spectator to all of the arena's scoreboards
                ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
                if (scoreBoard != null) {
                    scoreBoard.addPlayer(player);
                }

                // Remove player from all queues
                ultimateGames.getQueueManager().removePlayerFromQueues(player);

                // Hide the spectator from all arena players
                for (String normalPlayer : arena.getPlayers()) {
                    Bukkit.getPlayer(normalPlayer).hidePlayer(player);
                }

                // Remove all of the player's potion effects
                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    player.removePotionEffect(potionEffect.getType());
                }

                // Extinguish the spectator if on fire
                player.setFireTicks(0);

                // Give the spectator flight
                player.setAllowFlight(true);
                player.setFlying(true);

                // Add the spectator to limbo in case of server crash or disconnect
                addPlayerToLimbo(player);
            }
        }
    }

    /**
     * Removes a player from an arena.
     * @param playerName The player's name.
     * @param arena The arena.
     */
    public void removePlayerFromArena(Player player, Boolean sendMessage) {
        String playerName = player.getName();
        if (players.containsKey(playerName)) {
            // Removes the player from the arena
            Arena arena = players.get(playerName).getArena();
            arena.removePlayer(playerName);
            arena.getGame().getGamePlugin().removePlayer(player, arena);
            players.remove(playerName);

            // Removes the player from any teams the player is in
            ultimateGames.getTeamManager().removePlayerFromTeam(playerName);

            // Removes the player from any classes the player is in
            GameClass gameClass = ultimateGames.getClassManager().getPlayerClass(arena.getGame(), playerName);
            if (gameClass != null) {
                gameClass.removePlayerFromClass(playerName);
            }

            // Removes the player from any spawnpoints the player is locked in
            for (SpawnPoint spawnPoint : ultimateGames.getSpawnpointManager().getSpawnPointsOfArena(arena)) {
                if (spawnPoint.getPlayer() != null && spawnPoint.getPlayer().equals(playerName)) {
                    spawnPoint.teleportPlayer(null);
                }
            }

            // Removes the player from the arena scoreboard
            ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.removePlayer(player);
            }
            
            ultimateGames.getTeamManager().removePlayerFromTeam(playerName);

            // Extinguish the spectator if on fire
            player.setFireTicks(0);

            // Removes all of the player's potion effects
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            // Teleports a player to the lobby
            Location location = ultimateGames.getLobbyManager().getLobby();
            if (location != null) {
                player.teleport(location);
            }

            // Stops the arena's starting countdown if there is not enough players anymore
            if (arena.getPlayers().size() < arena.getMinPlayers()) {
                if (ultimateGames.getCountdownManager().isStartingCountdownEnabled(arena)) {
                    ultimateGames.getCountdownManager().stopStartingCountdown(arena);
                }
            }

            // Updates the arena's lobby signs
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);

            // Sends a message that the player left the arena
            if (sendMessage) {
                ultimateGames.getMessageManager().broadcastReplacedMessageToArena(arena, "arenas.leave", playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
            }

            // Shows all spectators to the player
            for (String spectator : arena.getSpectators()) {
                player.showPlayer(Bukkit.getPlayerExact(spectator));
            }

            // Removes the player from limbo
            removePlayerFromLimbo(player);
        }
    }

    /**
     * Removes a spectator from an arena.
     * @param player The spectator.
     */
    public void removeSpectatorFromArena(Player player) {
        String playerName = player.getName();
        if (spectators.containsKey(playerName)) {
            // Removes the spectator from the arena
            Arena arena = spectators.get(playerName).getArena();
            arena.removeSpectator(playerName);
            arena.getGame().getGamePlugin().removeSpectator(player, arena);
            spectators.remove(playerName);

            // Removes the spectator from any spawnpoints the spectator is locked in
            for (SpawnPoint spawnPoint : ultimateGames.getSpawnpointManager().getSpawnPointsOfArena(arena)) {
                if (spawnPoint.getPlayer() != null && spawnPoint.getPlayer().equals(playerName)) {
                    spawnPoint.teleportPlayer(null);
                }
            }

            // Removes the spectator from all arena scoreboards
            ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getArenaScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.removePlayer(player);
            }

            // Extinguish the spectator if on fire
            player.setFireTicks(0);

            // Removes all of the spectator's potion effects
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            // Teleports the spectator to the lobby
            Location location = ultimateGames.getLobbyManager().getLobby();
            if (location != null) {
                player.teleport(location);
            }

            // Shows the spectator to all arena players
            for (String normalPlayer : arena.getPlayers()) {
                Bukkit.getPlayerExact(normalPlayer).showPlayer(player);
            }

            // Takes away flight from the spectator
            player.setAllowFlight(false);
            player.setFlying(false);

            // Removes the spectator from limbo
            removePlayerFromLimbo(player);
        }
    }

    /**
     * Gets the ArenaPlayer of a player in an arena.
     * @param playerName The player's name.
     * @return The ArenaPlayer.
     */
    public ArenaPlayer getArenaPlayer(String playerName) {
        return players.containsKey(playerName) ? players.get(playerName) : null;
    }

    /**
     * Gets the ArenaSpectator of a spectator spectating an arena.
     * @param playerName The spectator's name.
     * @return The ArenaSpectator.
     */
    public ArenaSpectator getArenaSpectator(String playerName) {
        return spectators.containsKey(playerName) ? spectators.get(playerName) : null;
    }

    /**
     * Removes a player from limbo.
     * @param player The player to remove from limbo.
     */
    @SuppressWarnings("unchecked")
    public void removePlayerFromLimbo(Player player) {
        String playerName = player.getName();
        List<String> playersInLimbo;
        if (ultimateGames.getConfigManager().getLobbyConfig().getConfig().contains(LIMBO)) {
            playersInLimbo = (ArrayList<String>) ultimateGames.getConfigManager().getLobbyConfig().getConfig().getList(LIMBO);
        } else {
            playersInLimbo = new ArrayList<String>();
        }
        if (playersInLimbo.contains(playerName)) {
            playersInLimbo.remove(playerName);
            ultimateGames.getConfigManager().getLobbyConfig().getConfig().set(LIMBO, playersInLimbo);
            ultimateGames.getConfigManager().getLobbyConfig().saveConfig();
        }
    }

    /**
     * Adds a player to limbo. Prevents people getting stuck in arena world on crashes.
     * @param player The player to add to limbo.
     */
    @SuppressWarnings("unchecked")
    public void addPlayerToLimbo(Player player) {
        String playerName = player.getName();
        List<String> playersInLimbo;
        if (ultimateGames.getConfigManager().getLobbyConfig().getConfig().contains(LIMBO)) {
            playersInLimbo = (ArrayList<String>) ultimateGames.getConfigManager().getLobbyConfig().getConfig().getList(LIMBO);
        } else {
            playersInLimbo = new ArrayList<String>();
        }
        playersInLimbo.add(playerName);
        ultimateGames.getConfigManager().getLobbyConfig().getConfig().set(LIMBO, playersInLimbo);
        ultimateGames.getConfigManager().getLobbyConfig().saveConfig();
    }

    /**
     * Teleports a player in limbo to the lobby if they login and are in limbo.
     */
    @SuppressWarnings("unchecked")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        List<String> playersInLimbo;
        if (ultimateGames.getConfigManager().getLobbyConfig().getConfig().contains(LIMBO)) {
            playersInLimbo = (ArrayList<String>) ultimateGames.getConfigManager().getLobbyConfig().getConfig().getList(LIMBO);
        } else {
            playersInLimbo = new ArrayList<String>();
        }
        if (playersInLimbo.contains(playerName)) {
            Bukkit.getPlayerExact(playerName).teleport(ultimateGames.getLobbyManager().getLobby());
            playersInLimbo.remove(playerName);
            ultimateGames.getConfigManager().getLobbyConfig().getConfig().set(LIMBO, playersInLimbo);
            ultimateGames.getConfigManager().getLobbyConfig().saveConfig();
        }
    }

    /**
     * Removes a player or spectator from an arena if the player disconnects.
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (isPlayerInArena(playerName) || isPlayerSpectatingArena(playerName)) {
            if (isPlayerInArena(playerName)) {
                removePlayerFromArena(player, true);
            } else {
                removeSpectatorFromArena(player);
            }
        }
        ultimateGames.getQueueManager().removePlayerFromQueues(player);
    }
}
