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
package me.ampayne2.ultimategames.utils;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.BlockIterator;

public class Utils {
    private UltimateGames ultimateGames;

    public Utils(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Gets the ordinal suffix of an integer.
     * @param value The integer.
     * @return The ordinal suffix.
     */
    public String getOrdinalSuffix(int value) {
        Integer roundedValue = Math.abs(value);
        final int lastDigit = roundedValue % 10;
        final int last2Digits = roundedValue % 100;
        switch (lastDigit) {
        case 1:
            return last2Digits == 11 ? "th" : "st";

        case 2:
            return last2Digits == 12 ? "th" : "nd";

        case 3:
            return last2Digits == 13 ? "th" : "rd";

        default:
            return "th";
        }
    }
    
    /**
     * Gets the name of a ChatColor.
     * @param chatColor The ChatColor.
     * @return The ChatColor's name.
     */
    public String getChatColorName(ChatColor chatColor) {
        String[] nameParts = chatColor.name().split("_");
        String[] capitalizedNameParts = new String[nameParts.length];
        for (int i = 0; i < nameParts.length; i++) {
            String namePart = nameParts[i];
            capitalizedNameParts[i] = Character.toUpperCase(namePart.charAt(0)) + namePart.substring(1).toLowerCase();
        }
        String name = capitalizedNameParts[0];
        for (int i = 1; i < capitalizedNameParts.length; i++) {
            name = name + " " + capitalizedNameParts[1];
        }
        return name;
    }
    
    /**
     * Converts a ChatColor to a Color.
     * @param chatColor The ChatColor.
     * @return The Color.
     */
    public Color chatColorToColor(ChatColor chatColor) {
        Color color;
        switch (chatColor) {
        case BLACK:
            color = Color.fromRGB(0, 0, 0);
            break;
        case DARK_BLUE:
            color = Color.fromRGB(0, 0, 42);
            break;
        case DARK_GREEN:
            color = Color.fromRGB(0, 42, 0);
            break;
        case DARK_AQUA:
            color = Color.fromRGB(0, 42, 42);
            break;
        case DARK_RED:
            color = Color.fromRGB(42, 0, 0);
            break;
        case DARK_PURPLE:
            color = Color.fromRGB(42, 0, 42);
            break;
        case GOLD:
            color = Color.fromRGB(42, 42, 0);
            break;
        case GRAY:
            color = Color.fromRGB(42, 42, 42);
            break;
        case DARK_GRAY:
            color = Color.fromRGB(21, 21, 21);
            break;
        case BLUE:
            color = Color.fromRGB(21, 21, 63);
            break;
        case GREEN:
            color = Color.fromRGB(21, 63, 21);
            break;
        case AQUA:
            color = Color.fromRGB(21, 63, 63);
        case RED:
            color = Color.fromRGB(63, 21, 21);
            break;
        case LIGHT_PURPLE:
            color = Color.fromRGB(63, 21, 63);
            break;
        case YELLOW:
            color = Color.fromRGB(63, 63, 21);
            break;
        case WHITE:
            color = Color.fromRGB(63, 63, 63);
            break;
        default:
            color = Color.fromRGB(63, 63, 63);
        }
        return color;
    }

    /**
     * Creates an instruction book for a game.
     * @param title The book's title.
     * @param author The book's author.
     * @param pages The book's pages.
     * @return The book.
     */
    public ItemStack createInstructionBook(Game game) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(game.getGameDescription().getName() + " Instructions");
        meta.setAuthor(game.getGameDescription().getAuthor());
        for (String page : game.getGameDescription().getInstructionPages()) {
            meta.addPage(ChatColor.translateAlternateColorCodes('&', page));
        }
        book.setItemMeta(meta);
        return book;
    }

    /**
     * Creates purple particles floating around a player pointing to each player on their radar.
     * @param playerName The player.
     * @param playersToScan Players to 'point' to.
     */
    public void radarScan(String playerName, List<String> playersToScan) {
        if (playersToScan != null && playerName != null) {
            Player player = Bukkit.getPlayerExact(playerName);
            Double playerX = player.getLocation().getX();
            Double playerY = player.getEyeLocation().getY();
            Double playerZ = player.getLocation().getZ();
            for (String nextPlayerToScan : playersToScan) {
                Player playerToScan = Bukkit.getPlayerExact(nextPlayerToScan);
                Double playerToScanX = playerToScan.getLocation().getX();
                Double playerToScanZ = playerToScan.getLocation().getZ();
                Double x = playerToScanX - playerX;
                Double z = playerToScanZ - playerZ;
                Double divisor = Math.sqrt((x * x) + (z * z)) / 2;
                Double relativeX = x / divisor;
                Double relativeZ = z / divisor;
                Location particleLocation = new Location(player.getWorld(), playerX + relativeX, playerY + 1, playerZ + relativeZ);
                ParticleEffect particleEffect = ParticleEffect.WITCH_MAGIC;
                particleEffect.play(player, particleLocation, 0, 0, 0, 0, 1);
            }
        }
    }

    /**
     * Closes a player's respawn screen 1 tick after called.
     * @param player The player.
     */
    public void autoRespawn(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(ultimateGames, new Runnable() {
            @Override
            public void run() {
                try {
                    Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);

                    Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".Packet205ClientCommand").newInstance();
                    packet.getClass().getField("a").set(packet, 1);

                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                } catch (Exception e) {
                    ultimateGames.getMessageManager().debug(e);
                }
            }
        }, 1L);
    }

    /**
     * Gets all the entities in a player's line of sight within a certain range.
     * @param player The player.
     * @param range The range.
     * @param highlightPath Whether or not it should 'highlight' the path with firework spark effects.
     * @param highlightEntities Whether or not it should 'highlight' each entity with an explosion effect.
     * @return The entities.
     */
    public List<Entity> getEntityTargets(final Player player, Integer range, Boolean highlightPath, Boolean highlightEntities) {
        BlockIterator iterator = new BlockIterator(player.getWorld(), player.getEyeLocation().toVector(), player.getEyeLocation().getDirection(), 0, range);
        List<Entity> target = new ArrayList<Entity>();
        while (iterator.hasNext()) {
            Block item = iterator.next();
            if (item.getType() != Material.AIR) {
                break;
            }
            if (highlightPath) {
                ParticleEffect.FIREWORKS_SPARK.play(item.getLocation(), 0, 0, 0, 0, 1);
            }
            for (Entity entity : player.getNearbyEntities(range, range, range)) {
                int acc = 1;
                for (int x = -acc; x < acc; x++) {
                    for (int z = -acc; z < acc; z++) {
                        for (int y = -acc; y < acc; y++) {
                            if (entity.getLocation().getBlock().getRelative(x, y, z).equals(item)) {
                                target.add(entity);
                                if (highlightEntities) {
                                    ParticleEffect.HUGE_EXPLOSION.play(entity.getLocation(), 0, 0, 0, 0, 1);
                                }
                            }
                        }
                    }
                }
            }
        }
        return target;
    }
    
    /**
     * Colors leather armor.
     * @param itemStack The piece of armor.
     * @param color The color.
     * @return The colored piece of armor.
     */
    public ItemStack colorArmor(ItemStack itemStack, Color color) {
        if (itemStack.getType() == Material.LEATHER_HELMET || itemStack.getType() == Material.LEATHER_CHESTPLATE || itemStack.getType() == Material.LEATHER_LEGGINGS || itemStack.getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
            meta.setColor(color);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
}
