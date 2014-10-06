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

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.CustomStorageInventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.InventoryHook;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

/**
 * A utility that allows creating inventories with icons that do stuff.
 */
public class IconMenu implements PluginListener {
    private String name;
    private int size;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private Player player;

    private String[] optionNames;
    private Item[] optionIcons;
    private static final Item EMPTY_SLOT;

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
        this.optionIcons = new Item[size];
        Canary.hooks().registerListener(this, plugin);
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
    public IconMenu setOption(int position, Item icon, String name, String... info) {
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
        CustomStorageInventory inventory = Canary.factory().getObjectFactory().newCustomStorageInventory(name, size / 9);
        for (int i = 0; i < optionIcons.length; i++) {
            inventory.setSlot(i, optionIcons[i] == null ? EMPTY_SLOT : optionIcons[i]);
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
    @HookHandler(priority = Priority.PASSIVE, ignoreCanceled = true)
    void onInventoryOpen(InventoryHook event) {
        if (event.getInventory().getInventoryName().equals(name) && event.isCanceled()) {
            //TODO : Fix canary so we can uncancel a event
            //event.setCanceled(false);
        }
    }

    /**
     * Handles InventoryClickEvents for the IconMenu.
     */
    @SuppressWarnings("deprecation")
    @HookHandler(priority = Priority.NORMAL)
    void onInventoryClick(Click event) {
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
     * Gets the minimum inventory size required to hold a certain amount of items.
     *
     * @param amount The amount of items.
     * @return The minimum inventory size required.
     */
    public static int getRequiredSize(int amount) {
        return ((int) Math.ceil(amount / 9.0)) * 9;
    }

    /**
     * Sets the name and lore of an ItemStack.
     *
     * @param item The ItemStack.
     * @param name The name.
     * @param lore The lore.
     * @return The ItemStack.
     */
    private Item setItemNameAndLore(Item item, String name, String[] lore) {
        item.setDisplayName(name);
        item.setLore(lore);
        return item;
    }
}
