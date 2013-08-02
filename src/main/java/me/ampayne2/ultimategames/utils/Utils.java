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

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

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
        value = Math.abs(value);
        final int lastDigit = value % 10;
        final int last2Digits = value % 100;
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
            meta.addPage(page);
        }
        book.setItemMeta(meta);
        return book;
    }

    /**
     * Creates purple particles floating around a player pointing to each player on their radar.
     * @param playerName The player.
     * @param playersToScan Players to 'point' to.
     */
    public void radarScan(String playerName, ArrayList<String> playersToScan) {
        if (playersToScan != null && playerName != null) {
            Player player = Bukkit.getPlayer(playerName);
            Double playerX = player.getLocation().getX();
            Double playerY = player.getEyeLocation().getY();
            Double playerZ = player.getLocation().getZ();
            for (String nextPlayerToScan : playersToScan) {
                Player playerToScan = Bukkit.getPlayer(nextPlayerToScan);
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
                } catch (Throwable e) {
                    ultimateGames.getMessageManager().debug((Exception) e);
                }
            }
        }, 1L);
    }
}
