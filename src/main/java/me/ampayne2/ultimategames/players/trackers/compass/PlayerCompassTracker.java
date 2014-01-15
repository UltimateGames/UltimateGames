package me.ampayne2.ultimategames.players.trackers.compass;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * A tracker that tracks a player with a compass.
 */
public class PlayerCompassTracker extends CompassTracker {
    private Player targetPlayer;

    /**
     * Creates a new PlayerCompassTracker.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param player        The tracking player.
     * @param targetPlayer  The initial target player.
     * @param arena         The tracking player's arena.
     */
    public PlayerCompassTracker(UltimateGames ultimateGames, Player player, Player targetPlayer, Arena arena) {
        super(ultimateGames, player, arena);
        this.targetPlayer = targetPlayer;
    }

    /**
     * Creates a new PlayerCompassTracker with no initial target player.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param player        The tracking player.
     * @param arena         The tracking player's arena.
     */
    public PlayerCompassTracker(UltimateGames ultimateGames, Player player, Arena arena) {
        this(ultimateGames, player, null, arena);
    }

    @Override
    public Location getTarget() {
        setTarget(targetPlayer == null ? null : targetPlayer.getLocation());
        return super.getTarget();
    }

    /**
     * Sets the target player of the tracker.
     *
     * @param targetPlayer The target player.
     */
    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    /**
     * Gets the target player of the tracker.
     *
     * @return targetPlayer The target player.
     */
    public Player getTargetPlayer() {
        return targetPlayer;
    }

    /**
     * Stops the tracker when the target player disconnects.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTargetPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer().equals(targetPlayer)) {
            stop();
        }
    }
}
