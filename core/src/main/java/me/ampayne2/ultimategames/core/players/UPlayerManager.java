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
package me.ampayne2.ultimategames.core.players;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.scoreboards.Scoreboard;
import me.ampayne2.ultimategames.api.arenas.spawnpoints.PlayerSpawnPoint;
import me.ampayne2.ultimategames.api.config.ConfigType;
import me.ampayne2.ultimategames.api.effects.GameSound;
import me.ampayne2.ultimategames.api.events.players.*;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.api.players.ArenaPlayer;
import me.ampayne2.ultimategames.api.players.ArenaSpectator;
import me.ampayne2.ultimategames.api.players.PlayerManager;
import me.ampayne2.ultimategames.api.players.classes.GameClass;
import me.ampayne2.ultimategames.api.signs.SignType;
import me.ampayne2.ultimategames.api.utils.BossBar;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.arenas.UArena;
import me.ampayne2.ultimategames.core.arenas.scoreboards.UScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
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
public class UPlayerManager implements Listener, PlayerManager {
    private final UG ultimateGames;
    private Map<String, ArenaPlayer> players = new HashMap<>();
    private Map<String, ArenaSpectator> spectators = new HashMap<>();
    private static final String LIMBO = "limbo";
    private static final GameSound JOIN_SOUND = new GameSound(Sound.ENDERMAN_TELEPORT, 10, 1);

    /**
     * Creates a new PlayerManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UPlayerManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean isPlayerInArena(String playerName) {
        return players.containsKey(playerName);
    }

    @Override
    public boolean isPlayerSpectatingArena(String playerName) {
        return spectators.containsKey(playerName);
    }

    @Override
    public Arena getPlayerArena(String playerName) {
        if (players.containsKey(playerName)) {
            return players.get(playerName).getArena();
        } else if (spectators.containsKey(playerName)) {
            return spectators.get(playerName).getArena();
        } else {
            return null;
        }
    }

    @Override
    public void makePlayerSpectator(Player player) {
        String playerName = player.getName();
        if (players.containsKey(playerName)) {
            UArena arena = (UArena) players.get(playerName).getArena();

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
            Scoreboard scoreboard = ultimateGames.getScoreboardManager().getScoreboard(arena);
            if (scoreboard != null) {
                ((UScoreboard) scoreboard).makePlayerGhost(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            }

            // Give the player flight
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    @Override
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
            if (((UArena) arena).addPlayer(player.getName()) && arena.getGame().getGamePlugin().addPlayer(player, arena)) {
                players.put(playerName, new ArenaPlayer(ultimateGames, playerName, arena));
                ultimateGames.getMessenger().debug("Added player " + playerName + " to arena " + arena.getName() + " of game " + arena.getGame().getName());

                // Update the arena's lobby signs
                ultimateGames.getSignManager().updateSignsOfArena(arena, SignType.LOBBY);

                // Send a message that the player joined to the arena
                if (sendMessage) {
                    ultimateGames.getMessenger().sendMessage(arena, UGMessage.ARENA_JOIN, playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
                }

                JOIN_SOUND.play(player.getLocation());

                // Add the player to all of the arena's scoreboards
                Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
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

                // Set the player to survival mode
                player.setGameMode(GameMode.SURVIVAL);

                // Add the player to limbo in case of server crash or disconnect
                addPlayerToLimbo(player);

                Bukkit.getPluginManager().callEvent(new PlayerPostJoinEvent(player, arena));
            }
        }
    }

    @Override
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
            if (((UArena) arena).addSpectator(playerName) && arena.getGame().getGamePlugin().addSpectator(player, arena)) {
                spectators.put(playerName, new ArenaSpectator(playerName, arena));

                // Add the spectator to the arena's scoreboard as a ghost.
                Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
                if (scoreBoard != null) {
                    scoreBoard.addPlayer(player);
                    ((UScoreboard) scoreBoard).makePlayerGhost(player);
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

    @SuppressWarnings("deprecation")
    @Override
    public void removePlayerFromArena(Player player, Boolean sendMessage) {
        String playerName = player.getName();
        if (players.containsKey(playerName)) {
            // Removes the player from any teams the player is in
            ultimateGames.getTeamManager().removePlayerFromTeam(player);

            // Removes the player from the arena
            UArena arena = (UArena) players.get(playerName).getArena();
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
            Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
            if (scoreBoard != null) {
                scoreBoard.removePlayer(player);
            }

            // Extinguish the spectator if on fire
            player.setFireTicks(0);

            // Removes all of the player's potion effects
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }

            // Reset the player's inventory
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.updateInventory();

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
            ultimateGames.getSignManager().updateSignsOfArena(arena, SignType.LOBBY);

            // Sends a message that the player left the arena
            if (sendMessage) {
                ultimateGames.getMessenger().sendMessage(arena, UGMessage.ARENA_LEAVE, playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
            }

            // Removes the player from limbo
            removePlayerFromLimbo(player);

            Bukkit.getPluginManager().callEvent(new PlayerLeaveEvent(player, arena));
        }
    }

    @Override
    public void removeSpectatorFromArena(Player player) {
        String playerName = player.getName();
        if (spectators.containsKey(playerName)) {
            // Removes the spectator from the arena
            UArena arena = (UArena) spectators.get(playerName).getArena();
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
            Scoreboard scoreBoard = ultimateGames.getScoreboardManager().getScoreboard(arena);
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

    @Override
    public ArenaPlayer getArenaPlayer(String playerName) {
        return players.containsKey(playerName) ? players.get(playerName) : null;
    }

    @Override
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
            playersInLimbo = new ArrayList<>();
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
            playersInLimbo = new ArrayList<>();
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
            playersInLimbo = new ArrayList<>();
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
        if (BossBar.hasStatusBar(player)) {
            BossBar.removeStatusBar(player);
        }
    }
}
