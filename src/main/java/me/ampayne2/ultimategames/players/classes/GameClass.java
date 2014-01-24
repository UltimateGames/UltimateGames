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
package me.ampayne2.ultimategames.players.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.ArenaStatus;
import me.ampayne2.ultimategames.effects.GameSound;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic game class that handles resetting inventories and more.<br>
 * Must be registered with the {@link me.ampayne2.ultimategames.players.classes.GameClassManager}.
 */
public abstract class GameClass {
    private final UltimateGames ultimateGames;
    private final Game game;
    private final String name;
    private final boolean canSwitchToWithoutDeath;
    private ItemStack icon;
    private boolean isUnlockable = false;
    private String unlockableString = null;
    private List<String> players = new ArrayList<String>();
    private static final GameSound JOIN_SOUND = new GameSound(Sound.HORSE_ARMOR, 1, 1.5F);

    /**
     * Creates a new GameClass.
     *
     * @param ultimateGames           The {@link me.ampayne2.ultimategames.UltimateGames} instance.
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
     * Gets the players in the GameClass.
     *
     * @return The players in the class.
     */
    public List<String> getPlayers() {
        return new ArrayList<String>(players);
    }

    /**
     * Adds a player to the GameClass.
     *
     * @param player The player to add to the GameClass.
     * @return True if the player is added to the GameClass, else false.
     */
    public boolean addPlayer(Player player) {
        ArenaStatus arenaStatus = ultimateGames.getPlayerManager().getPlayerArena(player.getName()).getStatus();
        return addPlayer(player, canSwitchToWithoutDeath || arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING, true);
    }

    /**
     * Adds a player to the GameClass.
     *
     * @param player         The player to add to the GameClass.
     * @param resetInventory If the player's inventory should be reset immediately.
     * @return True if the player is added to the GameClass, else false.
     */
    public boolean addPlayer(Player player, boolean resetInventory) {
        return addPlayer(player, resetInventory, true);
    }

    /**
     * Adds a player to the GameClass.
     *
     * @param player         The player to add to the GameClass.
     * @param resetInventory If the player's inventory should be reset immediately.
     * @param notify    If a message and sound should be sent to the player.
     * @return True if the player is added to the GameClass, else false.
     */
    public boolean addPlayer(Player player, boolean resetInventory, boolean notify) {
        String playerName = player.getName();
        if (!players.contains(playerName)) {
            GameClass gameClass = ultimateGames.getGameClassManager().getPlayerClass(game, playerName);
            if (gameClass != null) {
                gameClass.removePlayer(playerName);
            }
            players.add(playerName);
            if (resetInventory) {
                resetInventory(player);
            }
            if (notify) {
                ultimateGames.getMessenger().sendMessage(player, resetInventory ? "classes.join" : "classes.nextdeath", name);
                JOIN_SOUND.play(player, player.getLocation());
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

    /**
     * Set the Class icon of this class when using the menu.
     *
     * @param icon The {@link org.bukkit.inventory.ItemStack} that represents this class
     */
    public void setClassIcon(ItemStack icon) {
        ItemStack newIcon = icon.clone();
        newIcon.setAmount(1);
        this.icon = newIcon;
    }

    /**
     * Get the class icon of this class.
     *
     * @return The {@link org.bukkit.inventory.ItemStack} that represents this class
     */
    public ItemStack getClassIcon() {
        return icon;
    }

    /**
     * Checks if this class is a unlockable one or not
     *
     * @return True if the class is a unlockable one else false
     */
    public boolean isUnlockable() {
        return isUnlockable;
    }

    /**
     * Set if this class is a unlockable one. If true, it will search if the player have that perk in the PointManager.
     *
     * @param unlockable If the class is unlockable or not
     */
    public void setIsUnlockable(boolean unlockable) {
        this.isUnlockable = unlockable;
    }

    /**
     * Set the unlockable string. Aka the value used to search if the player have that perk.
     *
     * @param value The string that represents this class
     */
    public void setUnlockableString(String value) {
        this.unlockableString = value;
    }

    /**
     * Retrieve the unlockable string.
     *
     * @return The string that represents this class
     */
    public String getUnlockableString() {
        return unlockableString;
    }

    /**
     * Checks if a player has access to this class.
     *
     * @param player The player to check for
     * @return True if the player has access, else false.
     */
    public boolean hasAccess(Player player) {
        return !(isUnlockable && getUnlockableString() != null) || ultimateGames.getPointManager().hasPerk(game, player.getName(), getUnlockableString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameClass gameClass = (GameClass) o;

        return game.equals(gameClass.game) && name.equals(gameClass.name);
    }

    @Override
    public int hashCode() {
        int result = game.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
