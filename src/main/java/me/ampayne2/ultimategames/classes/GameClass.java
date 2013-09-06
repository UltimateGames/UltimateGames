package me.ampayne2.ultimategames.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.games.Game;

/**
 * The base class for a GameClass in a game. Games extend this to make their own classes.
 */
public abstract class GameClass {

    private UltimateGames ultimateGames;
    private Game game;
    private String name;
    private boolean canSwitchToWithoutDeath;
    private List<String> players = new ArrayList<String>();

    /**
     * Creates a new GameClass.
     * @param ultimateGames A reference to the UltimateGames instance.
     * @param game The game of the GameClass.
     * @param name The name of the GameClass.
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
     * @return The GameClass's Game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the name of a GameClass.
     * @return The GameClass's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if a player can switch to the GameClass without having to die first.
     * @return True if the player can switch without having to die first, else false.
     */
    public boolean canSwitchToWithoutDeath() {
        return canSwitchToWithoutDeath;
    }

    /**
     * Checks if a GameClass has a player.
     * @param playerName The player's name.
     * @return True if the player is in the GameClass, else false.
     */
    public boolean hasPlayer(String playerName) {
        return players.contains(playerName);
    }

    /**
     * Adds a player to the GameClass.
     * @param player The player to add to the GameClass.
     * @return True if the player is added to the GameClass, else false.
     */
    public boolean addPlayerToClass(Player player) {
        String playerName = player.getName();
        if (!players.contains(playerName)) {
            players.add(playerName);
            GameClass gameClass = ultimateGames.getClassManager().getPlayerClass(game, playerName);
            if (gameClass != null) {
                gameClass.removePlayerFromClass(playerName);
            }
            ArenaStatus arenaStatus = ultimateGames.getPlayerManager().getPlayerArena(playerName).getStatus();
            if (canSwitchToWithoutDeath || arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING) {
                ultimateGames.getMessageManager().sendReplacedMessage(player, "classes.join", name);
                resetInventory(player);
            } else {
                ultimateGames.getMessageManager().sendReplacedMessage(player, "classes.nextdeath", name);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a player to the GameClass.
     * @param player The player to add to the GameClass.
     * @param resetInventory If the player's inventory should be reset immediately.
     * @return True if the player is added to the GameClass, else false.
     */
    public boolean addPlayerToClass(Player player, Boolean resetInventory) {
        String playerName = player.getName();
        if (!players.contains(playerName)) {
            players.add(playerName);
            GameClass gameClass = ultimateGames.getClassManager().getPlayerClass(game, playerName);
            if (gameClass != null) {
                gameClass.removePlayerFromClass(playerName);
            }
            if (resetInventory) {
                ultimateGames.getMessageManager().sendReplacedMessage(player, "classes.join", name);
                resetInventory(player);
            } else {
                ultimateGames.getMessageManager().sendReplacedMessage(player, "classes.nextdeath", name);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a player from the GameClass.
     * @param playerName The name of the player to remove from the GameClass.
     */
    public void removePlayerFromClass(String playerName) {
        if (players.contains(playerName)) {
            players.remove(playerName);
        }
    }

    /**
     * Removes all players from the GameClass.
     */
    public void removePlayersFromClass() {
        players.clear();
    }

    /**
     * Resets a player's inventory.
     * @param player The player whose inventory you want to reset.
     */
    public abstract void resetInventory(Player player);

}
