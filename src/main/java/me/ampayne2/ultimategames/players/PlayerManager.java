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
package me.ampayne2.ultimategames.players;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.scoreboards.ArenaScoreboard;
import me.ampayne2.ultimategames.arenas.spawnpoints.PlayerSpawnPoint;
import me.ampayne2.ultimategames.config.ConfigType;
import me.ampayne2.ultimategames.events.players.*;
import me.ampayne2.ultimategames.players.classes.GameClass;
import me.ampayne2.ultimategames.signs.SignType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages players both in and out of arenas.
 */
public class PlayerManager implements Listener {
    private final UltimateGames ultimateGames;
    private Map<String, ArenaPlayer> players = new HashMap<String, ArenaPlayer>();
    private Map<String, ArenaSpectator> spectators = new HashMap<String, ArenaSpectator>();
    private static final String LIMBO = "limbo";

    /**
     * Creates a new PlayerManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     */
    public PlayerManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Checks to see if a player is in an arena.
     *
     * @param playerName The player's name.
     * @return If the player is in an arena or not.
     */
    public boolean isPlayerInArena(String playerName) {
        return players.containsKey(playerName);
    }

    /**
     * Checks to see if a player is spectating an arena.
     *
     * @param playerName The player's name.
     * @return If the player is in an arena or not.
     */
    public boolean isPlayerSpectatingArena(String playerName) {
        return spectators.containsKey(playerName);
    }

    /**
     * Gets the arena a player is in or spectating.
     *
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
     *
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

            // Makes the spectator appear as a ghost to other spectators.
            ArenaScoreboard scoreboard = ultimateGames.getScoreboardManager().getScoreboard(arena);
            if (scoreboard != null) {
                scoreboard.makePlayerGhost(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            }

            // Give the player flight
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    /**
     * Adds a player to an arena.
     *
     * @param player      The player.
     * @param arena       The arena.
     * @param sendMessage If a join message should be sent.
     */
    public void addPlayerToArena(Player player, Arena arena, Boolean sendMessage) {
        String playerName = player.getName();
        if (!players.containsKey(playerName) && arena.getPlayers().size() < arena.getMaxPlayers()) {
            PlayerPreJoinEvent event = new PlayerPreJoinEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            // Teleport the player to the lobby
            player.teleport(ultimateGames.getLobbyManager().getLobby());

            // Add the player to the arena and make the player an ArenaPlayer object
            if (arena.addPlayer(player.getName()) && arena.getGame().getGamePlugin().addPlayer(player, arena)) {
                players.put(playerName, new ArenaPlayer(ultimateGames, playerName, arena));
                ultimateGames.getMessenger().debug("Added player " + playerName + " to arena " + arena.getName() + " of game " + arena.getGame().getName());

                // Update the arena's lobby signs
                ultimateGames.getUGSignManager().updateUGSignsOfArena(arena, SignType.LOBBY);

                // Send a message that the player joined to the arena
                if (sendMessage) {
                    ultimateGames.getMessenger().sendMessage(arena, "arenas.join", playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
                }

                // Add the player to all of the arena's scoreboards
                ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
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

                Bukkit.getPluginManager().callEvent(new PlayerPostJoinEvent(player, arena));
            }
        }
    }

    /**
     * Adds a spectator to an arean.
     *
     * @param player The spectator's name.
     * @param arena  The arena.
     */
    public void addSpectatorToArena(Player player, Arena arena) {
        String playerName = player.getName();
        if (!spectators.containsKey(playerName)) {
            SpectatorPreJoinEvent event = new SpectatorPreJoinEvent(player, arena);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            // Teleport the spectator to the lobby
            player.teleport(ultimateGames.getLobbyManager().getLobby());

            // Add the spectator to the arena and make the spectator an ArenaSpectator object
            if (arena.addSpectator(playerName) && arena.getGame().getGamePlugin().addSpectator(player, arena)) {
                spectators.put(playerName, new ArenaSpectator(playerName, arena));

                // Add the spectator to the arena's scoreboard as a ghost.
                ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
                if (scoreBoard != null) {
                    scoreBoard.addPlayer(player);
                    scoreBoard.makePlayerGhost(player);
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

                // Gives the spectator invisibility to appear as a ghost to other spectators.
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));

                // Extinguish the spectator if on fire
                player.setFireTicks(0);

                // Give the spectator flight
                player.setAllowFlight(true);
                player.setFlying(true);

                // Add the spectator to limbo in case of server crash or disconnect
                addPlayerToLimbo(player);

                Bukkit.getPluginManager().callEvent(new PlayerPostJoinEvent(player, arena));
            }
        }
    }

    /**
     * Removes a player from an arena.
     *
     * @param player      The player.
     * @param sendMessage If a leave message should be sent.
     */
    public void removePlayerFromArena(Player player, Boolean sendMessage) {
        String playerName = player.getName();
        if (players.containsKey(playerName)) {
            // Removes the player from any teams the player is in
            ultimateGames.getTeamManager().removePlayerFromTeam(playerName);

            // Removes the player from the arena
            Arena arena = players.get(playerName).getArena();
            arena.removePlayer(playerName);
            arena.getGame().getGamePlugin().removePlayer(player, arena);
            players.remove(playerName);

            // Removes the player from any classes the player is in
            GameClass gameClass = ultimateGames.getGameClassManager().getPlayerClass(arena.getGame(), playerName);
            if (gameClass != null) {
                gameClass.removePlayer(playerName);
            }

            // Removes the player from any spawnpoints the player is locked in
            for (PlayerSpawnPoint spawnPoint : ultimateGames.getSpawnpointManager().getSpawnPointsOfArena(arena)) {
                if (spawnPoint.getPlayer() != null && spawnPoint.getPlayer().equals(playerName)) {
                    spawnPoint.teleportPlayer(null);
                }
            }

            // Removes the player from the arena scoreboard
            ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.removePlayer(player);
            }

            // Extinguish the spectator if on fire
            player.setFireTicks(0);

            // Removes all of the player's potion effects
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            // Shows all spectators to the player
            for (String spectator : arena.getSpectators()) {
                player.showPlayer(Bukkit.getPlayerExact(spectator));
            }

            // Teleports a player to the lobby
            Location location = ultimateGames.getLobbyManager().getLobby();
            if (location != null) {
                player.teleport(location);
            }

            ultimateGames.getMessenger().debug("Removed player " + playerName + " from arena " + arena.getName() + " of game " + arena.getGame().getName());

            // Stops the arena's starting countdown if there is not enough players anymore
            if (arena.getPlayers().size() < arena.getMinPlayers() && ultimateGames.getCountdownManager().hasStartingCountdown(arena)) {
                ultimateGames.getCountdownManager().stopStartingCountdown(arena);
            }

            // Updates the arena's lobby signs
            ultimateGames.getUGSignManager().updateUGSignsOfArena(arena, SignType.LOBBY);

            // Sends a message that the player left the arena
            if (sendMessage) {
                ultimateGames.getMessenger().sendMessage(arena, "arenas.leave", playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
            }

            // Removes the player from limbo
            removePlayerFromLimbo(player);

            Bukkit.getPluginManager().callEvent(new PlayerLeaveEvent(player, arena));
        }
    }

    /**
     * Removes a spectator from an arena.
     *
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
            for (PlayerSpawnPoint spawnPoint : ultimateGames.getSpawnpointManager().getSpawnPointsOfArena(arena)) {
                if (spawnPoint.getPlayer() != null && spawnPoint.getPlayer().equals(playerName)) {
                    spawnPoint.teleportPlayer(null);
                }
            }

            // Removes the spectator from all arena scoreboards
            ArenaScoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.removePlayer(player);
            }

            // Extinguish the spectator if on fire
            player.setFireTicks(0);

            // Removes all of the spectator's potion effects
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            // Takes away flight from the spectator
            player.setAllowFlight(false);
            player.setFlying(false);

            // Shows the spectator to all arena players
            for (String normalPlayer : arena.getPlayers()) {
                Bukkit.getPlayerExact(normalPlayer).showPlayer(player);
            }

            // Teleports the spectator to the lobby
            Location location = ultimateGames.getLobbyManager().getLobby();
            if (location != null) {
                player.teleport(location);
            }

            // Removes the spectator from limbo
            removePlayerFromLimbo(player);

            Bukkit.getPluginManager().callEvent(new SpectatorLeaveEvent(player, arena));
        }
    }

    /**
     * Gets the ArenaPlayer of a player in an arena.
     *
     * @param playerName The player's name.
     * @return The ArenaPlayer.
     */
    public ArenaPlayer getArenaPlayer(String playerName) {
        return players.containsKey(playerName) ? players.get(playerName) : null;
    }

    /**
     * Gets the ArenaSpectator of a spectator spectating an arena.
     *
     * @param playerName The spectator's name.
     * @return The ArenaSpectator.
     */
    public ArenaSpectator getArenaSpectator(String playerName) {
        return spectators.containsKey(playerName) ? spectators.get(playerName) : null;
    }

    /**
     * Removes a player from limbo.
     *
     * @param player The player to remove from limbo.
     */
    @SuppressWarnings("unchecked")
    public void removePlayerFromLimbo(Player player) {
        String playerName = player.getName();
        List<String> playersInLimbo;
        if (ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).contains(LIMBO)) {
            playersInLimbo = (ArrayList<String>) ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).getList(LIMBO);
        } else {
            playersInLimbo = new ArrayList<String>();
        }
        if (playersInLimbo.contains(playerName)) {
            playersInLimbo.remove(playerName);
            ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).set(LIMBO, playersInLimbo);
            ultimateGames.getConfigManager().getConfigAccessor(ConfigType.LOBBY).saveConfig();
        }
    }

    /**
     * Adds a player to limbo. Prevents people getting stuck in arena world on crashes.
     *
     * @param player The player to add to limbo.
     */
    @SuppressWarnings("unchecked")
    public void addPlayerToLimbo(Player player) {
        String playerName = player.getName();
        List<String> playersInLimbo;
        if (ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).contains(LIMBO)) {
            playersInLimbo = (ArrayList<String>) ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).getList(LIMBO);
        } else {
            playersInLimbo = new ArrayList<String>();
        }
        playersInLimbo.add(playerName);
        ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).set(LIMBO, playersInLimbo);
        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.LOBBY).saveConfig();
    }

    /**
     * Teleports a player in limbo to the lobby if they login and are in limbo.
     */
    @SuppressWarnings("unchecked")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        List<String> playersInLimbo;
        if (ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).contains(LIMBO)) {
            playersInLimbo = (ArrayList<String>) ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).getList(LIMBO);
        } else {
            playersInLimbo = new ArrayList<String>();
        }
        if (playersInLimbo.contains(playerName)) {
            Bukkit.getPlayerExact(playerName).teleport(ultimateGames.getLobbyManager().getLobby());
            playersInLimbo.remove(playerName);
            ultimateGames.getConfigManager().getConfig(ConfigType.LOBBY).set(LIMBO, playersInLimbo);
            ultimateGames.getConfigManager().getConfigAccessor(ConfigType.LOBBY).saveConfig();
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
