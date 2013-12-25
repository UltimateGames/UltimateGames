package me.ampayne2.ultimategames.utils;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class MenuIcon implements Listener {

    private final UltimateGames ultimateGames;
    private final ItemStack icon;
    private final Game game;
    public MenuIcon(UltimateGames ultimateGames, ItemStack icon, Game game) {
        this.ultimateGames = ultimateGames;
        this.icon = icon;
        this.game = game;
        ultimateGames.getServer().getPluginManager().registerEvents(this, ultimateGames);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().isSimilar(icon)) {
                ultimateGames.getGameClassManager().getMenu(ultimateGames, game, event.getPlayer()).open(event.getPlayer());
            }
        }
    }
}
