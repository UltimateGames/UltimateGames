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

import me.ampayne2.ultimategames.api.effects.ParticleEffect;
import me.ampayne2.ultimategames.api.games.Game;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Attachable;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Various ultimate games utilities to make your life easier.
 */
public final class UGUtils {
    private static final long AUTO_RESPAWN_DELAY = 1L;
    private static final double HORIZONTAL_TARGETER_ACCURACY = 1.5;
    private static final double VERTICAL_TARGETER_ACCURACY = 0.5;
    private static final BlockFace[] FACES = new BlockFace[]{BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private static final Random RANDOM = new Random();

    private UGUtils() {
    }

    // Math utilities

    /**
     * Clamps a value between a minimum and maximum value.
     *
     * @param value The value.
     * @param min   The minimum value.
     * @param max   The maximum value.
     * @return The clamped value.
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    // String utilities

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

    // Randomization utilities

    /**
     * Gets a random enum value from an enum.
     *
     * @param clazz The enum's class.
     * @return The random enum value.
     */
    public static <T extends Enum> T randomEnum(Class<T> clazz) {
        return clazz.getEnumConstants()[RANDOM.nextInt(clazz.getEnumConstants().length)];
    }

    /**
     * Gets a random color.
     *
     * @return The random color.
     */
    public static Color randomColor() {
        switch (RANDOM.nextInt(17)) {
            case 0:
                return Color.AQUA;
            case 1:
                return Color.BLACK;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.FUCHSIA;
            case 4:
                return Color.GRAY;
            case 5:
                return Color.GREEN;
            case 6:
                return Color.LIME;
            case 7:
                return Color.MAROON;
            case 8:
                return Color.NAVY;
            case 9:
                return Color.OLIVE;
            case 10:
                return Color.ORANGE;
            case 11:
                return Color.PURPLE;
            case 12:
                return Color.RED;
            case 13:
                return Color.SILVER;
            case 14:
                return Color.TEAL;
            case 15:
                return Color.WHITE;
            case 16:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }

    /**
     * Gets a random firework effect.
     *
     * @return The random firework effect.
     */
    public static FireworkEffect randomFireworkEffect() {
        FireworkEffect.Type type = randomEnum(FireworkEffect.Type.class);
        Color c1 = randomColor();
        Color c2 = randomColor();

        return FireworkEffect.builder().flicker(RANDOM.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(RANDOM.nextBoolean()).build();
    }

    // Firework utilities

    /**
     * Spawns a firework.
     *
     * @param location Location to spawn the firework.
     * @param effect   Effect to give the firework.
     * @param power    Power of the firework.
     * @param detonate If the firework should detonate immediately.
     */
    public static void spawnFirework(Location location, FireworkEffect effect, int power, boolean detonate) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);

        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);

        if (detonate) {
            firework.detonate();
        }
    }

    /**
     * Spawns a firework with a random power.
     *
     * @param location Location to spawn the firework.
     * @param effect   Effect to give the firework.
     * @param detonate If the firework should detonate immediately.
     */
    public static void spawnFirework(Location location, FireworkEffect effect, boolean detonate) {
        spawnFirework(location, effect, RANDOM.nextInt(2) + 1, detonate);
    }

    /**
     * Spawns a firework with a random effect.
     *
     * @param location Location to spawn the firework.
     * @param power    Power of the firework.
     * @param detonate If the firework should detonate immediately.
     */
    public static void spawnFirework(Location location, int power, boolean detonate) {
        spawnFirework(location, randomFireworkEffect(), power, detonate);
    }

    /**
     * Spawns a firework with a random effect and power.
     *
     * @param location Location to spawn the firework.
     * @param detonate If the firework should detonate immediately.
     */
    public static void spawnFirework(Location location, boolean detonate) {
        spawnFirework(location, randomFireworkEffect(), RANDOM.nextInt(2) + 1, detonate);
    }

    // Player and Entity utilities

    /**
     * Closes a player's respawn screen 1 tick after called.<br>
     * Uses craftbukkit code and may break on updates. Confirmed working on 1.6.4 and earlier.
     *
     * @param player The player.
     */
    public static void autoRespawn(JavaPlugin plugin, final Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (player.isDead()) {
                    try {
                        Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
                        Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
                        Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");

                        for (Object ob : enumClass.getEnumConstants()) {
                            if (ob.toString().equals("PERFORM_RESPAWN")) {
                                packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
                            }
                        }

                        Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                        con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
                    } catch (Exception e) {
                        // Respawn code broke
                    }
                }
            }
        }, AUTO_RESPAWN_DELAY);
    }

    /**
     * Teleports an entity with a vehicle or passenger correctly.
     *
     * @param entity   The entity to teleport.
     * @param location The location to teleport the entity to.
     */
    public static void teleportEntity(Entity entity, Location location) {
        Location entityLocation = entity.getLocation();

        Entity vehicle = entity.getVehicle();
        if (vehicle != null) {
            // Eject the entity from the vehicle and teleport the vehicle to the correct location.
            vehicle.eject();
            Location vehicleLocation = vehicle.getLocation();
            Location teleportLocation = location.clone().subtract(0, entityLocation.getY() - vehicleLocation.getY(), 0);
            teleportLocation.setPitch(vehicleLocation.getPitch());
            teleportLocation.setYaw(vehicleLocation.getYaw());
            teleportEntity(vehicle, teleportLocation);
        }

        Entity passenger = entity.getPassenger();
        if (passenger != null) {
            // Eject the passenger from the entity and teleport the passenger to the correct location.
            entity.eject();
            Location passengerLocation = passenger.getLocation();
            Location teleportLocation = location.clone().add(0, passengerLocation.getY() - entityLocation.getY(), 0);
            teleportLocation.setPitch(passengerLocation.getPitch());
            teleportLocation.setYaw(passengerLocation.getYaw());
            teleportEntity(vehicle, teleportLocation);
        }

        // Teleport the entity to the correct location.
        entity.teleport(location);

        // Connect the vehicle, entity, and passenger back together if existing.
        if (vehicle != null) {
            vehicle.setPassenger(entity);
        }
        if (passenger != null) {
            entity.setPassenger(passenger);
        }
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
        Set<LivingEntity> targetedEntities = new HashSet<>();
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
    public static boolean isLocationInEntity(double locationX, double locationY, double locationZ, Entity entity) {
        Location entityLocation = entity.getLocation();
        double entityYLower = entityLocation.getY();
        double entityYHigher = entityYLower + getEntityHeight(entity);
        if (Math.abs(locationX - entityLocation.getX()) > HORIZONTAL_TARGETER_ACCURACY) {
            return false;
        } else if (locationY >= (entityYLower - VERTICAL_TARGETER_ACCURACY) && locationY <= (entityYHigher + VERTICAL_TARGETER_ACCURACY)) {
            return false;
        } else if (Math.abs(locationZ - entityLocation.getZ()) > HORIZONTAL_TARGETER_ACCURACY) {
            return false;
        }
        return true;
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
     * Gets the nearest player to a player from a collection of players.
     *
     * @param targeter The player.
     * @param players  The players to get the nearest player from.
     * @return The nearest player, null if no players to choose from.
     */
    public static Player getNearestPlayer(Player targeter, Collection<Player> players) {
        Player nearestPlayer = null;
        double distance = Double.POSITIVE_INFINITY;

        for (Player player : players) {
            if (!player.getName().equals(targeter.getName())) {
                if (nearestPlayer == null) {
                    nearestPlayer = player;
                    distance = targeter.getLocation().distance(nearestPlayer.getLocation());
                } else {
                    double distanceTo = targeter.getLocation().distance(player.getLocation());
                    if (distanceTo < distance) {
                        nearestPlayer = player;
                        distance = distanceTo;
                    }
                }
            }
        }

        return nearestPlayer;
    }

    // Inventory and ItemStack utilities

    /**
     * Gets the total amount of a certain material in an inventory.
     *
     * @param inventory The Inventory.
     * @param material  The Material.
     * @return The total amount of items.
     */
    public static int getTotalItems(Inventory inventory, Material material) {
        int amount = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && itemStack.getType().equals(material)) {
                amount += itemStack.getAmount();
            }
        }
        return amount;
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
     * Gets the head of a player.
     *
     * @param playerName The player's name.
     * @return The player skull.
     */
    @SuppressWarnings("deprecation")
    public static ItemStack getPlayerHead(String playerName) {
        ItemStack skull = new ItemStack(397, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(playerName);
        skull.setItemMeta(meta);
        return skull;
    }

    // Block and Material utilities

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
        Set<Sign> attachedSigns = new HashSet<>();
        for (BlockFace face : FACES) {
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

    // Potion Effect utilities

    /**
     * Gets an active PotionEffect on a LivingEntity.
     *
     * @param entity The entity.
     * @param type   The type of potion effect.
     * @return The PotionEffect, null if player doesn't have the potion effect.
     */
    public static PotionEffect getActivePotionEffect(LivingEntity entity, PotionEffectType type) {
        if (entity.hasPotionEffect(type)) {
            for (PotionEffect effect : entity.getActivePotionEffects()) {
                if (effect.getType().equals(type)) {
                    return effect;
                }
            }
        }
        return null;
    }

    /**
     * Sets the duration of a PotionEffect on a LivingEntity.
     *
     * @param entity   The entity.
     * @param type     The type of potion effect.
     * @param duration The new duration.
     */
    public static void setPotionEffectDuration(LivingEntity entity, PotionEffectType type, int duration) {
        if (entity.hasPotionEffect(type)) {
            PotionEffect effect = getActivePotionEffect(entity, type);
            int amplifier = effect.getAmplifier();
            entity.removePotionEffect(type);
            entity.addPotionEffect(new PotionEffect(type, duration, amplifier));
        }
    }

    /**
     * Sets the amplifier of a PotionEffect on a LivingEntity.
     *
     * @param entity    The entity.
     * @param type      The type of potion effect.
     * @param amplifier The new amplifier.
     */
    public static void setPotionEffectAmplifier(LivingEntity entity, PotionEffectType type, int amplifier) {
        if (entity.hasPotionEffect(type)) {
            PotionEffect effect = getActivePotionEffect(entity, type);
            int duration = effect.getDuration();
            entity.removePotionEffect(type);
            entity.addPotionEffect(new PotionEffect(type, duration, amplifier));
        }
    }

    /**
     * Increases the amplifier of a LivingEntity's PotionEffect by the given amount.
     *
     * @param entity The entity.
     * @param type   The type of potion effect.
     * @param amount The amount to increase the amplifier by.
     */
    public static void increasePotionEffect(LivingEntity entity, PotionEffectType type, int amount) {
        if (entity.hasPotionEffect(type)) {
            PotionEffect effect = getActivePotionEffect(entity, type);
            int duration = effect.getDuration();
            int amplifier = effect.getAmplifier();
            entity.removePotionEffect(type);
            entity.addPotionEffect(new PotionEffect(type, duration, amplifier + amount));
        } else {
            entity.addPotionEffect(new PotionEffect(type, Integer.MAX_VALUE, amount - 1));
        }
    }

    /**
     * Increases the amplifier of a LivingEntity's PotionEffect by 1.
     *
     * @param entity The entity.
     * @param type   The type of potion effect.
     */
    public static void increasePotionEffect(LivingEntity entity, PotionEffectType type) {
        increasePotionEffect(entity, type, 1);
    }

    /**
     * Decreases the amplifier of a LivingEntity's PotionEffect by the given amount.<br>
     * If the amplifier is decreased to less than or equal to 0, the PotionEffect is removed.
     *
     * @param entity The entity.
     * @param type   The type of potion effect.
     * @param amount The amount to decrease the amplifier by.
     */
    public static void decreasePotionEffect(LivingEntity entity, PotionEffectType type, int amount) {
        if (entity.hasPotionEffect(type)) {
            PotionEffect effect = getActivePotionEffect(entity, type);
            int duration = effect.getDuration();
            int amplifier = effect.getAmplifier();
            entity.removePotionEffect(type);
            if (amplifier - amount >= 0) {
                entity.addPotionEffect(new PotionEffect(type, duration, amplifier - amount));
            }
        }
    }

    /**
     * Decreases the amplifier of a LivingEntity's potion effect by 1.<br>
     * If the amplifier is decreased to less than or equal to 0, the PotionEffect is removed.
     *
     * @param entity The entity.
     * @param type   The type of potion effect.
     */
    public static void decreasePotionEffect(LivingEntity entity, PotionEffectType type) {
        decreasePotionEffect(entity, type, 1);
    }

    /**
     * Removes a potion effect from a LivingEntity.
     *
     * @param entity The entity.
     * @param type   The type of potion effect.
     */
    public static void removePotionEffect(LivingEntity entity, PotionEffectType type) {
        if (entity.hasPotionEffect(type)) {
            entity.removePotionEffect(type);
        }
    }

    // Miscellaneous utilities

    /**
     * Creates an instruction book for a game.
     *
     * @param game The game.
     * @return The book.
     */
    public static ItemStack createInstructionBook(Game game) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle(ChatColor.AQUA.toString() + ChatColor.BOLD + game.getName() + " Instructions");
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
}
