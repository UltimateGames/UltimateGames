package me.ampayne2.ultimategames.players.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.entity.Player;

/**
 * A tiered GameClass. Resets inventories based on the player's highest unlocked tier.
 */
public abstract class TieredClass extends GameClass {
    private final UltimateGames ultimateGames;

    /**
     * Creates a new GameClass.
     *
     * @param ultimateGames           A reference to the UltimateGames instance.
     * @param game                    The game of the GameClass.
     * @param name                    The name of the GameClass.
     * @param canSwitchToWithoutDeath If a player can join the GameClass without having to die first.
     */
    public TieredClass(UltimateGames ultimateGames, Game game, String name, boolean canSwitchToWithoutDeath) {
        super(ultimateGames, game, name, canSwitchToWithoutDeath);
        this.ultimateGames = ultimateGames;
    }

    /**
     * Gets the highest tier of this class the player has unlocked.
     *
     * @param player The player.
     * @return The highest tier unlocked.
     */
    public int getTier(Player player) {
        int tier = 1;
        while (ultimateGames.getPointManager().hasPerk(getGame(), player.getName(), getName().toLowerCase() + (tier + 1))) {
            tier++;
        }
        return tier;
    }

    @Override
    public void resetInventory(Player player) {
        resetInventory(player, getTier(player));
    }

    /**
     * Resets a player's inventory.
     *
     * @param player The player whose inventory you want to reset.
     * @param tier   The tier of the player's class.
     */
    public abstract void resetInventory(Player player, int tier);
}
