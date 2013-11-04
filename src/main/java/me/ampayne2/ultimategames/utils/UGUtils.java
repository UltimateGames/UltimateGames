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

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.effects.ParticleEffect;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UGUtils {
    private static final long AUTO_RESPAWN_DELAY = 1L;
    private static final double TARGETER_ACCURACY = 1.0;
    private static final BlockFace[] faces = new BlockFace[]{BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    /**
     * Gets the ordinal suffix of an integer.
     *
     * @param value The integer.
     * @return The ordinal suffix.
     */
    public static String getOrdinalSuffix(int value) {
        int roundedValue = Math.abs(value);
        int lastDigit = roundedValue % 10;
        int last2Digits = roundedValue % 100;
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
     *
     * @param chatColor The ChatColor.
     * @return The ChatColor's name.
     */
    public static String getChatColorName(ChatColor chatColor) {
        String[] nameParts = chatColor.name().split("_");
        String[] capitalizedNameParts = new String[nameParts.length];
        for (int i = 0; i < nameParts.length; i++) {
            String namePart = nameParts[i];
            capitalizedNameParts[i] = Character.toUpperCase(namePart.charAt(0)) + namePart.substring(1).toLowerCase();
        }
        StringBuilder nameBuilder = new StringBuilder(capitalizedNameParts[0]);
        for (int i = 1; i < capitalizedNameParts.length; i++) {
            nameBuilder.append(" ");
            nameBuilder.append(capitalizedNameParts[i]);
        }
        return nameBuilder.toString();
    }

    /**
     * Converts a ChatColor to a Color.
     *
     * @param chatColor The ChatColor.
     * @return The Color.
     */
    public static Color chatColorToColor(ChatColor chatColor) {
        Color color;
        switch (chatColor) {
            case BLACK:
                color = Color.BLACK;
                break;
            case DARK_BLUE:
                color = Color.NAVY;
                break;
            case DARK_GREEN:
                color = Color.GREEN;
                break;
            case DARK_AQUA:
                color = Color.AQUA;
                break;
            case DARK_RED:
                color = Color.MAROON;
                break;
            case DARK_PURPLE:
                color = Color.PURPLE;
                break;
            case GOLD:
                color = Color.ORANGE;
                break;
            case GRAY:
                color = Color.SILVER;
                break;
            case DARK_GRAY:
                color = Color.GRAY;
                break;
            case BLUE:
                color = Color.BLUE;
                break;
            case GREEN:
                color = Color.LIME;
                break;
            case AQUA:
                color = Color.TEAL;
                break;
            case RED:
                color = Color.RED;
                break;
            case LIGHT_PURPLE:
                color = Color.FUCHSIA;
                break;
            case YELLOW:
                color = Color.YELLOW;
                break;
            case WHITE:
                color = Color.WHITE;
                break;
            default:
                color = Color.WHITE;
        }
        return color;
    }

    /**
     * Creates an instruction book for a game.
     *
     * @param game The game.
     * @return The book.
     */
    public static ItemStack createInstructionBook(Game game) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(game.getName() + " Instructions");
        meta.setAuthor(game.getAuthor());
        for (String page : game.getInstructionPages()) {
            meta.addPage(ChatColor.translateAlternateColorCodes('&', page));
        }
        book.setItemMeta(meta);
        return book;
    }

    /**
     * Creates particles floating around a player pointing at other players. Uses the default particle effect settings.
     *
     * @param playerName    The player.
     * @param playersToScan Players to 'point' to.
     */
    public static void radarScan(String playerName, Collection<String> playersToScan) {
        radarScan(playerName, playersToScan, ParticleEffect.WITCH_MAGIC, 0, 1);
    }

    /**
     * Creates particles floating around a player pointing at other players.
     *
     * @param playerName     The main player's name.
     * @param playersToScan  The players on the main player's radar.
     * @param particleEffect The ParticleEffect to play.
     * @param speed          The speed of the particle effect.
     * @param amount         The amount of particles.
     */
    public static void radarScan(String playerName, Collection<String> playersToScan, ParticleEffect particleEffect, int speed, int amount) {
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
                particleEffect.display(particleLocation, 0, 0, 0, speed, amount, player);
            }
        }
    }

    /**
     * Closes a player's respawn screen 1 tick after called.<br>
     * Uses craftbukkit code and may break on updates. Confirmed working on 1.6.4 and earlier.
     *
     * @param player The player.
     */
    public static void autoRespawn(final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(UltimateGames.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);

                    Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".Packet205ClientCommand").newInstance();
                    packet.getClass().getField("a").set(packet, 1);

                    Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                    con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                } catch (Exception e) {
                    UltimateGames.getInstance().getMessageManager().debug(e);
                }
            }
        }, AUTO_RESPAWN_DELAY);
    }

    /**
     * Gets all the LivingEntities in a player's line of sight within a certain range.
     *
     * @param player            The player.
     * @param range             The range.
     * @param maxEntities       The maximum amount of entities the targeter will get. 0 for no limit.
     * @param highlightPath     Whether or not it should 'highlight' the path with firework spark effects.
     * @param highlightEntities Whether or not it should 'highlight' each entity with an explosion effect.
     * @return The LivingEntities.
     */
    public static Collection<LivingEntity> getLivingEntityTargets(Player player, double range, int maxEntities, Boolean goThroughWalls, Boolean highlightPath, Boolean highlightEntities) {
        Location location = player.getEyeLocation();
        Vector direction = location.getDirection();
        List<Entity> entities = player.getNearbyEntities(range, range, range);
        Set<LivingEntity> targetedEntities = new HashSet<LivingEntity>();
        int maxWorldHeight = location.getWorld().getMaxHeight();
        int distance = 0;
        while (distance < range && (maxEntities == 0 || targetedEntities.size() <= maxEntities)) {
            double locationX = location.getX();
            double locationY = location.getY();
            double locationZ = location.getZ();
            if ((!goThroughWalls && location.getBlock().getType() != Material.AIR) || locationY < 0 || locationY > maxWorldHeight) {
                break;
            }
            if (highlightPath) {
                ParticleEffect.FIREWORKS_SPARK.display(location, 0, 0, 0, 0, 1);
            }
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity && isLocationInEntity(locationX, locationY, locationZ, entity)) {
                    if (maxEntities == 0 || targetedEntities.size() < maxEntities) {
                        targetedEntities.add((LivingEntity) entity);
                        if (highlightEntities) {
                            ParticleEffect.HUGE_EXPLOSION.display(entity.getLocation(), 0, 0, 0, 0, 1);
                        }
                    } else {
                        break;
                    }
                }
            }
            location.add(direction);
            distance++;
        }
        return targetedEntities;
    }

    /**
     * Checks if a location is near or very close to an entity.
     *
     * @param locationX The location's x value.
     * @param locationY The location's y value.
     * @param locationZ The location's z value.
     * @param entity    The entity.
     * @return True if the location is within a certain range of the entity.
     */
    public static Boolean isLocationInEntity(double locationX, double locationY, double locationZ, Entity entity) {
        Location entityLocation = entity.getLocation();
        double entityYLower = entityLocation.getY();
        double entityYHigher = entityYLower + getEntityHeight(entity);
        return Math.abs(locationX - entityLocation.getX()) <= TARGETER_ACCURACY && (locationY >= entityYLower && locationY <= entityYHigher) && Math.abs(locationZ - entityLocation.getZ()) <= TARGETER_ACCURACY;
    }

    /**
     * Gets an entities height.
     *
     * @param entity The entity.
     * @return The entities height.
     */
    public static double getEntityHeight(Entity entity) {
        double y;
        if (entity instanceof Player || entity instanceof Skeleton || entity instanceof Creeper || entity instanceof Zombie || entity instanceof Snowman || entity instanceof Villager || entity instanceof Blaze) {
            y = 2.0;
        } else if (entity instanceof Cow || entity instanceof Pig || entity instanceof Sheep || entity instanceof MagmaCube || entity instanceof Slime) {
            y = 1.5;
        } else if (entity instanceof Chicken || entity instanceof Spider || entity instanceof Wolf) {
            y = 1.0;
        } else if (entity instanceof Ocelot || entity instanceof Silverfish) {
            y = 0.5;
        } else if (entity instanceof Enderman) {
            y = 2.5;
        } else if (entity instanceof Squid) {
            y = 0.1;
        } else {
            y = 1.0;
        }
        return y;
    }

    /**
     * Colors leather armor.
     *
     * @param itemStack The piece of armor.
     * @param color     The color.
     * @return The colored piece of armor.
     */
    public static ItemStack colorArmor(ItemStack itemStack, Color color) {
        if (itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
            meta.setColor(color);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    /**
     * Checks if a material has physics.
     *
     * @param material The material.
     * @return True if the material has physics, else false.
     */
    public static Boolean hasPhysics(Material material) {
        switch (material) {
            case ACTIVATOR_RAIL:
            case ANVIL:
            case CARPET:
            case CACTUS:
            case CROPS:
            case DEAD_BUSH:
            case DRAGON_EGG:
            case FIRE:
            case FLOWER_POT:
            case GRAVEL:
            case IRON_DOOR_BLOCK:
            case LEVER:
            case LONG_GRASS:
            case MELON_STEM:
            case NETHER_WARTS:
            case POWERED_RAIL:
            case RAILS:
            case RED_MUSHROOM:
            case RED_ROSE:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case REDSTONE_WIRE:
            case SAND:
            case SAPLING:
            case STONE_BUTTON:
            case SUGAR_CANE_BLOCK:
            case TORCH:
            case TRAP_DOOR:
            case TRIPWIRE_HOOK:
            case WATER_LILY:
            case WOOD_BUTTON:
            case WOODEN_DOOR:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if a block is attached to a block.
     *
     * @param attached The block that might be attached.
     * @param block    The support block.
     * @return True if the block is attached to the block, else false.
     */
    public static Boolean isAttachedToBlock(Block attached, Block block) {
        MaterialData data = attached.getState().getData();
        return data instanceof Attachable && attached.getRelative(((Attachable) data).getAttachedFace()).equals(block);
    }

    /**
     * Gets all signs extending from a block.
     *
     * @param block     The main block.
     * @param recursive If the attached signs should be checked for attached signs.
     * @return The extending signs.
     */
    public static Set<Sign> getAttachedSigns(Block block, boolean recursive) {
        Set<Sign> attachedSigns = new HashSet<Sign>();
        for (BlockFace face : faces) {
            Block faceBlock = block.getRelative(face);
            Material faceMaterial = faceBlock.getType();
            if (faceMaterial == Material.WALL_SIGN) {
                if (isAttachedToBlock(faceBlock, block)) {
                    attachedSigns.add((Sign) faceBlock.getState());
                    if (recursive) {
                        attachedSigns.addAll(getAttachedSigns(faceBlock, true));
                    }
                }
            } else if (faceMaterial == Material.SIGN_POST && face == BlockFace.UP) {
                attachedSigns.add((Sign) faceBlock.getState());
                if (recursive) {
                    attachedSigns.addAll(getAttachedSigns(faceBlock, true));
                }
            }
        }
        return attachedSigns;
    }
}
