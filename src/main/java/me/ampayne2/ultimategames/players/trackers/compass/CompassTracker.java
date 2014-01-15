package me.ampayne2.ultimategames.players.trackers.compass;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.players.trackers.Tracker;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * A tracker that tracks a location with a compass.
 */
public class CompassTracker extends Tracker {
    private Location target;

    /**
     * Creates a new CompassTracker.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param player        The tracking player.
     * @param target        The initial target location.
     * @param arena         The tracking player's arena.
     */
    public CompassTracker(UltimateGames ultimateGames, Player player, Location target, Arena arena) {
        super(ultimateGames, player, arena);
        this.target = target;
    }

    /**
     * Creates a new CompassTracker with the initial target at the player's location.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param player        The tracking player.
     * @param arena         The tracking player's arena.
     */
    public CompassTracker(UltimateGames ultimateGames, Player player, Arena arena) {
        this(ultimateGames, player, player.getLocation(), arena);
    }

    @Override
    public void run() {
        Location location = getTarget();
        if (location != null) {
            getPlayer().setCompassTarget(location);
        }
    }

    /**
     * Sets the target of the tracker.
     *
     * @param target The target.
     */
    public void setTarget(Location target) {
        this.target = target;
    }

    /**
     * Gets the target of the tracker.
     *
     * @return The target.
     */
    public Location getTarget() {
        return target;
    }
}
