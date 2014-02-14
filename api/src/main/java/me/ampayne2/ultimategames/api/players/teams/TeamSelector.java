package me.ampayne2.ultimategames.api.players.teams;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.games.items.GameItem;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A team selector GameItem.
 */
public class TeamSelector extends GameItem {
    private final UltimateGames ultimateGames;
    private static final ItemStack TEAM_SELECTOR;

    /**
     * Creates a new team selector item.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     */
    public TeamSelector(UltimateGames ultimateGames) {
        super(TEAM_SELECTOR, false);
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean click(Arena arena, PlayerInteractEvent event) {
        ultimateGames.getTeamManager().getTeamSelector(arena).open(event.getPlayer());
        return true;
    }

    static {
        TEAM_SELECTOR = new ItemStack(397, 1, (short) 3);
        ItemMeta meta = TEAM_SELECTOR.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD.toString() + ChatColor.AQUA + "Team Selector");
        TEAM_SELECTOR.setItemMeta(meta);
    }
}
