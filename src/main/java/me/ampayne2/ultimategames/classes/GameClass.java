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
package me.ampayne2.ultimategames.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for a GameClass in a game. Games extend this to make their own classes.
 */
public abstract class GameClass {
    private final UltimateGames ultimateGames;
    private final Game game;
    private final String name;
    private final boolean canSwitchToWithoutDeath;
    private List<String> players = new ArrayList<String>();

    /**
     * Creates a new GameClass.
     *
     * @param ultimateGames           A reference to the UltimateGames instance.
     * @param game                    The game of the GameClass.
     * @param name                    The name of the GameClass.
     * @param canSwitchToWithoutDeath If a player can join the GameClass without having to die first.
     */
    public GameClass(UltimateGames ultimateGames, Game game, String name, boolean canSwitchToWithoutDeath) {
        this.ultimateGames = ultimateGames;
        this.game = game;
        this.name = name;
        this.canSwitchToWithoutDeath = canSwitchToWithoutDeath;
    }

    /**
     * Gets the Game of a GameClass.
     *
     * @return The GameClass's Game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the name of a GameClass.
     *
     * @return The GameClass's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if a player can switch to the GameClass without having to die first.
     *
     * @return True if the player can switch without having to die first, else false.
     */
    public boolean canSwitchToWithoutDeath() {
        return canSwitchToWithoutDeath;
    }

    /**
     * Checks if a GameClass has a player.
     *
     * @param playerName The player's name.
     * @return True if the player is in the GameClass, else false.
     */
    public boolean hasPlayer(String playerName) {
        return players.contains(playerName);
    }

    /**
     * Adds a player to the GameClass.
     *
     * @param player The player to add to the GameClass.
     * @return True if the player is added to the GameClass, else false.
     */
    public boolean addPlayer(Player player) {
        String playerName = player.getName();
        if (!players.contains(playerName)) {
            GameClass gameClass = ultimateGames.getGameClassManager().getPlayerClass(game, playerName);
            if (gameClass != null) {
                gameClass.removePlayer(playerName);
            }
            players.add(playerName);
            ArenaStatus arenaStatus = ultimateGames.getPlayerManager().getPlayerArena(playerName).getStatus();
            if (canSwitchToWithoutDeath || arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING) {
                ultimateGames.getMessageManager().sendMessage(player, "classes.join", name);
                resetInventory(player);
            } else {
                ultimateGames.getMessageManager().sendMessage(player, "classes.nextdeath", name);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a player to the GameClass.
     *
     * @param player         The player to add to the GameClass.
     * @param resetInventory If the player's inventory should be reset immediately.
     * @return True if the player is added to the GameClass, else false.
     */
    public boolean addPlayer(Player player, Boolean resetInventory) {
        String playerName = player.getName();
        if (!players.contains(playerName)) {
            GameClass gameClass = ultimateGames.getGameClassManager().getPlayerClass(game, playerName);
            if (gameClass != null) {
                gameClass.removePlayer(playerName);
            }
            players.add(playerName);
            if (resetInventory) {
                ultimateGames.getMessageManager().sendMessage(player, "classes.join", name);
                resetInventory(player);
            } else {
                ultimateGames.getMessageManager().sendMessage(player, "classes.nextdeath", name);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a player from the GameClass.
     *
     * @param playerName The name of the player to remove from the GameClass.
     */
    public void removePlayer(String playerName) {
        if (players.contains(playerName)) {
            players.remove(playerName);
        }
    }

    /**
     * Removes all players from the GameClass.
     */
    public void removePlayers() {
        players.clear();
    }

    /**
     * Resets a player's inventory.
     *
     * @param player The player whose inventory you want to reset.
     */
    public abstract void resetInventory(Player player);
}
