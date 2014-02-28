/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

/**
 * A utility that allows creating inventories with icons that do stuff.
 */
public class IconMenu implements Listener {
    private String name;
    private int size;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private Player player;

    private String[] optionNames;
    private ItemStack[] optionIcons;
    private static final ItemStack EMPTY_SLOT;

    /**
     * Creates a new IconMenu.
     *
     * @param name    The name of the inventory.
     * @param size    The size of the inventory.
     * @param handler The OptionClickEventHandler.
     * @param plugin  The Plugin instance.
     */
    public IconMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin) {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Sets the ItemStack of a slot.
     *
     * @param position The slot position.
     * @param icon     The ItemStack.
     * @param name     The name to give the ItemStack.
     * @param info     The lore to give the ItemStack.
     * @return The IconMenu.
     */
    public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    /**
     * Sets the IconMenu specific to a player.
     *
     * @param player The player.
     */
    public void setSpecificTo(Player player) {
        this.player = player;
    }

    /**
     * Checks if the IconMenu is specific to a player.
     *
     * @return True if the IconMenu is specific to a player, else false.
     */
    public boolean isSpecific() {
        return player != null;
    }

    /**
     * Opens the IconMenu for a player.
     *
     * @param player The player.
     */
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size, name);
        for (int i = 0; i < optionIcons.length; i++) {
            inventory.setItem(i, optionIcons[i] == null ? EMPTY_SLOT : optionIcons[i]);
        }
        player.openInventory(inventory);
    }

    /**
     * Destroys the IconMenu.
     */
    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    /**
     * Prevents the IconMenu from being blocked from opening.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getName().equals(name)) {
            event.setCancelled(false);
        }
    }

    /**
     * Handles InventoryClickEvents for the IconMenu.
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && (player == null || event.getWhoClicked() == player)) {
            event.setCancelled(true);
            if (event.getClick() != ClickType.LEFT) {
                return;
            }
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size && optionNames[slot] != null) {
                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
                handler.onOptionClick(e);
                ((Player) event.getWhoClicked()).updateInventory();
                if (e.willClose()) {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            p.closeInventory();
                        }
                    }, 1);
                }
                if (e.willDestroy()) {
                    destroy();
                }
            }
        }
    }

    static {
        EMPTY_SLOT = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
        ItemMeta meta = EMPTY_SLOT.getItemMeta();
        meta.setDisplayName(" ");
        EMPTY_SLOT.setItemMeta(meta);
    }

    /**
     * Handles OptionClickEvents.
     */
    public interface OptionClickEventHandler {

        /**
         * Called when an Icon in the IconMenu is clicked.
         *
         * @param event The OptionClickEvent.
         */
        void onOptionClick(OptionClickEvent event);
    }

    /**
     * An event fired when an Icon in the IconMenu is clicked.
     */
    public class OptionClickEvent {
        private Player player;
        private int position;
        private String name;
        private boolean close;
        private boolean destroy;

        public OptionClickEvent(Player player, int position, String name) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
        }

        /**
         * Gets the player who clicked.
         *
         * @return The player.
         */
        public Player getPlayer() {
            return player;
        }

        /**
         * Gets the position of the Icon clicked.
         *
         * @return The position.
         */
        public int getPosition() {
            return position;
        }

        /**
         * Gets the name of the Icon clicked.
         *
         * @return The name.
         */
        public String getName() {
            return name;
        }

        /**
         * Checks if the inventory will close.
         *
         * @return True if the inventory will close, else false.
         */
        public boolean willClose() {
            return close;
        }

        /**
         * Checks if the inventory will be destroyed.
         *
         * @return True if the inventory will be destroyed, else false.
         */
        public boolean willDestroy() {
            return destroy;
        }

        /**
         * Sets if the inventory will close.
         *
         * @param close If the inventory will close.
         */
        public void setWillClose(boolean close) {
            this.close = close;
        }

        /**
         * Sets if the inventory will be destroyed.
         *
         * @param destroy If the inventory will be destroyed.
         */
        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }

    /**
     * Sets the name and lore of an ItemStack.
     *
     * @param item The ItemStack.
     * @param name The name.
     * @param lore The lore.
     * @return The ItemStack.
     */
    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }
}
