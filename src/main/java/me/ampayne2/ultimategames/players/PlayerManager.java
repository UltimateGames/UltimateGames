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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.SpawnPoint;
import me.ampayne2.ultimategames.events.GameJoinEvent;
import me.ampayne2.ultimategames.scoreboards.ArenaScoreboard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

public class PlayerManager implements Listener {
    private UltimateGames ultimateGames;
    private Map<String, ArenaPlayer> players = new HashMap<String, ArenaPlayer>();
    private List<String> playersInLimbo = new ArrayList<String>();
    private static final String LIMBO = "limbo";

    @SuppressWarnings("unchecked")
    public PlayerManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        if (ultimateGames.getConfigManager().getLobbyConfig().getConfig().contains(LIMBO)) {
            playersInLimbo = (ArrayList<String>) ultimateGames.getConfigManager().getLobbyConfig().getConfig().getList(LIMBO);
        }
    }

    /**
     * Checks to see if a player is in an arena.
     * @param playerName The player's name.
     * @return If the player is in an arena or not.
     */
    public Boolean isPlayerInArena(String playerName) {
        return players.containsKey(playerName);
    }

    /**
     * Gets the arena a player is in.
     * @param playerName The player's name.
     * @return The arena a player is in. Null if the player isn't in an arena.
     */
    public Arena getPlayerArena(String playerName) {
        if (players.containsKey(playerName)) {
            return players.get(playerName).getArena();
        } else {
            return null;
        }
    }

    /**
     * Adds a player to an arena.
     * @param playerName The player's name.
     * @param arena The arena.
     */
    public void addPlayerToArena(Player player, Arena arena, Boolean sendMessage) {
        String playerName = player.getName();
        if (!isPlayerInArena(playerName) && getPlayerArena(playerName) == null && arena.getPlayers().size() < arena.getMaxPlayers()) {
            player.teleport(ultimateGames.getLobbyManager().getLobby());
            if (arena.addPlayer(player.getName()) && arena.getGame().getGamePlugin().addPlayer(player, arena)) {
                players.put(playerName, new ArenaPlayer(playerName, arena));
                GameJoinEvent gameJoinEvent = new GameJoinEvent(player, arena);
                Bukkit.getServer().getPluginManager().callEvent(gameJoinEvent);
                ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
                if (sendMessage) {
                    ultimateGames.getMessageManager().broadcastReplacedMessageToArena(arena, "arenas.join", playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
                }
                for (ArenaScoreboard scoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
                    scoreBoard.addPlayer(player);
                }
                ultimateGames.getQueueManager().removePlayerFromQueues(playerName);
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
            Arena arena = players.get(playerName).getArena();
            arena.removePlayer(playerName);
            arena.getGame().getGamePlugin().removePlayer(player, arena);
            players.remove(playerName);
            for (SpawnPoint spawnPoint : ultimateGames.getSpawnpointManager().getSpawnPointsOfArena(arena)) {
                if (spawnPoint.getPlayer() != null && spawnPoint.getPlayer().equals(playerName)) {
                    spawnPoint.teleportPlayer(null);
                }
            }
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(potionEffect.getType());
            }
            Location location = ultimateGames.getLobbyManager().getLobby();
            if (location != null) {
                player.teleport(location);
            }
            if (arena.getPlayers().size() < arena.getMinPlayers()) {
                if (ultimateGames.getCountdownManager().isStartingCountdownEnabled(arena)) {
                    ultimateGames.getCountdownManager().stopStartingCountdown(arena);
                }
            }
            for (ArenaScoreboard scoreBoard : ultimateGames.getScoreboardManager().getArenaScoreboards(arena)) {
                scoreBoard.removePlayer(player);
            }
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(arena);
            if (sendMessage) {
                ultimateGames.getMessageManager().broadcastReplacedMessageToArena(arena, "arenas.leave", playerName, arena.getPlayers().size() + " / " + arena.getMaxPlayers());
            }
        }
    }

    /**
     * Teleports a player in limbo to the lobby.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        if (playersInLimbo.contains(playerName)) {
            Bukkit.getPlayerExact(playerName).teleport(ultimateGames.getLobbyManager().getLobby());
            playersInLimbo.remove(playerName);
            ultimateGames.getConfigManager().getLobbyConfig().getConfig().set(LIMBO, playersInLimbo);
            ultimateGames.getConfigManager().getLobbyConfig().saveConfig();
        }
    }

    /**
     * Puts a player in limbo if the player is in an arena and leaves the game.
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        String playerName = event.getPlayer().getName();
        if (isPlayerInArena(playerName)) {
            removePlayerFromArena(event.getPlayer(), true);
            playersInLimbo.add(playerName);
            ultimateGames.getConfigManager().getLobbyConfig().getConfig().set(LIMBO, playersInLimbo);
            ultimateGames.getConfigManager().getLobbyConfig().saveConfig();
        }
        ultimateGames.getQueueManager().removePlayerFromQueues(playerName);
    }
}
