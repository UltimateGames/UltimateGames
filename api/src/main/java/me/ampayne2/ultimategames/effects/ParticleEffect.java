/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.effects;

import me.ampayne2.ultimategames.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.Map.Entry;


/**
 * ParticleEffect Library v1.2
 *
 * This library was created by @DarkBlade12 based on content related to particles of @microgeek (names and packet values) and allows you to display all Minecraft particle effects on a Bukkit server
 *
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * 1. Credit us if you publish a plugin that uses this library
 * 2. Don't remove this text
 *
 * @author DarkBlade12
 */
public enum ParticleEffect {
    HUGE_EXPLOSION("hugeexplosion", 0),
    LARGE_EXPLODE("largeexplode", 1),
    FIREWORKS_SPARK("fireworksSpark", 2),
    BUBBLE("bubble", 3),
    SUSPEND("suspend", 4),
    DEPTH_SUSPEND("depthSuspend", 5),
    TOWN_AURA("townaura", 6),
    CRIT("crit", 7),
    MAGIC_CRIT("magicCrit", 8),
    MOB_SPELL("mobSpell", 9),
    MOB_SPELL_AMBIENT("mobSpellAmbient", 10),
    SPELL("spell", 11),
    INSTANT_SPELL("instantSpell", 12),
    WITCH_MAGIC("witchMagic", 13),
    NOTE("note", 14),
    PORTAL("portal", 15),
    ENCHANTMENT_TABLE("enchantmenttable", 16),
    EXPLODE("explode", 17),
    FLAME("flame", 18),
    LAVA("lava", 19),
    FOOTSTEP("footstep", 20),
    SPLASH("splash", 21),
    LARGE_SMOKE("largesmoke", 22),
    CLOUD("cloud", 23),
    RED_DUST("reddust", 24),
    SNOWBALL_POOF("snowballpoof", 25),
    DRIP_WATER("dripWater", 26),
    DRIP_LAVA("dripLava", 27),
    SNOW_SHOVEL("snowshovel", 28),
    SLIME("slime", 29),
    HEART("heart", 30),
    ANGRY_VILLAGER("angryVillager", 31),
    HAPPY_VILLAGER("happyVillager", 32);

    private static final Map<String, ParticleEffect> NAME_MAP = new HashMap<>();
    private static final Map<Integer, ParticleEffect> ID_MAP = new HashMap<>();
    private static final double MAX_RANGE = 20.0D;
    private static Constructor<?> PARTICLE_PACKET_CONSTRUCTOR;

    static {
        for (ParticleEffect effect : values()) {
            NAME_MAP.put(effect.name, effect);
            ID_MAP.put(effect.id, effect);
        }
        try {
            PARTICLE_PACKET_CONSTRUCTOR = ReflectionUtil.getConstructor(ReflectionUtil.getClass("PacketPlayOutWorldParticles", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String name;
    private int id;

    ParticleEffect(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static ParticleEffect fromName(String name) {
        if (name != null)
            for (Entry<String, ParticleEffect> e : NAME_MAP.entrySet())
                if (e.getKey().equalsIgnoreCase(name))
                    return e.getValue();
        return null;
    }

    public static ParticleEffect fromId(int id) {
        return ID_MAP.get(id);
    }

    private static List<Player> getPlayersInRange(Location loc, double range) {
        List<Player> players = new ArrayList<>();
        double sqr = range * range;
        for (Player p : loc.getWorld().getPlayers())
            if (p.getLocation().distanceSquared(loc) <= sqr)
                players.add(p);
        return players;
    }

    private static Object createPacket(String name, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount of particles has to be greater than 0");
        try {
            Object p = PARTICLE_PACKET_CONSTRUCTOR.newInstance();
            ReflectionUtil.setValues(p, new ReflectionUtil.FieldEntry("a", name), new ReflectionUtil.FieldEntry("b", (float) loc.getX()), new ReflectionUtil.FieldEntry("c", (float) loc.getY()), new ReflectionUtil.FieldEntry("d", (float) loc.getZ()), new ReflectionUtil.FieldEntry("e", offsetX), new ReflectionUtil.FieldEntry("f", offsetY), new ReflectionUtil.FieldEntry("g", offsetZ), new ReflectionUtil.FieldEntry("h", speed), new ReflectionUtil.FieldEntry("i", amount));
            return p;
        } catch (Exception e) {
            Bukkit.getLogger().warning("[ParticleEffect] Failed to create a particle packet!");
            return null;
        }
    }

    private Object createPacket(Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket(this.getName(), loc, offsetX, offsetY, offsetZ, speed, amount);
    }

    private static Object createIconCrackPacket(int id, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket("iconcrack_" + id, loc, offsetX, offsetY, offsetZ, speed, amount);
    }

    private static Object createBlockCrackPacket(int id, byte data, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket("blockcrack_" + id + "_" + data, loc, offsetX, offsetY, offsetZ, speed, amount);
    }

    private static Object createBlockDustPacket(int id, byte data, Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        return createPacket("blockdust_" + id + "_" + data, loc, offsetX, offsetY, offsetZ, speed, amount);
    }

    private static void sendPacket(Player p, Object packet) {
        if (packet != null)
            try {
                Object entityPlayer = ReflectionUtil.invokeMethod("getHandle", p.getClass(), p);
                Object playerConnection = ReflectionUtil.getValue("playerConnection", entityPlayer);
                ReflectionUtil.invokeMethod("sendPacket", playerConnection.getClass(), playerConnection, packet);
            } catch (Exception e) {
                Bukkit.getLogger().warning("[ParticleEffect] Failed to send a particle packet to " + p.getName() + "!");
            }
    }

    private static void sendPacket(Collection<Player> players, Object packet) {
        for (Player p : players)
            sendPacket(p, packet);
    }

    /**
     * Displays a particle effect which is only visible for specific players
     */
    public void display(Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createPacket(loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a particle effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param loc
     */
    public void display(Location loc, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        display(loc, MAX_RANGE, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a particle effect which is visible for all players whitin a certain range in the the world of @param loc
     */
    public void display(Location loc, double range, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of 20");
        sendPacket(getPlayersInRange(loc, range), createPacket(loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays an icon crack (item break) effect which is only visible for specific players
     */
    public static void displayIconCrack(Location loc, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createIconCrackPacket(id, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays an icon crack (item break) effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param loc
     */
    public static void displayIconCrack(Location loc, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        displayIconCrack(loc, MAX_RANGE, id, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays an icon crack (item break) effect which is visible for all players whitin a certain range in the the world of @param loc
     */
    public static void displayIconCrack(Location loc, double range, int id, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of 20");
        sendPacket(getPlayersInRange(loc, range), createIconCrackPacket(id, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block crack (block break) effect which is only visible for specific players
     */
    public static void displayBlockCrack(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createBlockCrackPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block crack (block break) effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param loc
     */
    public static void displayBlockCrack(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        displayBlockCrack(loc, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a block crack (block break) effect which is visible for all players whitin a certain range in the the world of @param loc
     */
    public static void displayBlockCrack(Location loc, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of 20");
        sendPacket(getPlayersInRange(loc, range), createBlockCrackPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block dust effect which is only visible for specific players
     */
    public static void displayBlockDust(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Player... players) {
        sendPacket(Arrays.asList(players), createBlockDustPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }

    /**
     * Displays a block dust effect which is visible for all players whitin the maximum range of 20 blocks in the world of @param loc
     */
    public static void displayBlockDust(Location loc, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        displayBlockDust(loc, MAX_RANGE, id, data, offsetX, offsetY, offsetZ, speed, amount);
    }

    /**
     * Displays a block dust effect which is visible for all players whitin a certain range in the the world of @param loc
     */
    public static void displayBlockDust(Location loc, double range, int id, byte data, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
        if (range > MAX_RANGE)
            throw new IllegalArgumentException("Range has to be lower/equal the maximum of 20");
        sendPacket(getPlayersInRange(loc, range), createBlockDustPacket(id, data, loc, offsetX, offsetY, offsetZ, speed, amount));
    }
}