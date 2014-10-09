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
import net.canarymod.api.world.position.Location;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

/**
 * A utility to show and modify a boss health bar.
 */
public class BossBar {
    private static final PlayerMap<FakeDragon> DRAGONS = new PlayerMap<>();

    /**
     * Checks to see if the player is currently being displayed a status bar via fake Ender Dragon.
     * <br><br>
     * This may sometimes return a false positive.  Specifically, if a player is sent a fake dragon, and
     * subsequently logs off and back on and the bar is not restored, the record of the dragon will remain
     * here even though the client no longer has the entity.  To avoid this, be sure to remove the bar
     * manually using {@link #removeStatusBar(Player)} when the player leaves the server
     * ({@link org.bukkit.event.player.PlayerQuitEvent} and {@link org.bukkit.event.player.PlayerKickEvent})
     *
     * @param player The player.
     * @return True if this API has a record of the player being sent a bar.
     */
    public static boolean hasStatusBar(Player player) {
        return DRAGONS.containsKey(player) && DRAGONS.get(player) != null;
    }

    /**
     * Removes a player's status bar by destroying their fake dragon (if they have one).
     *
     * @param player The player.
     */
    public static void removeStatusBar(Player player) {
        if (hasStatusBar(player)) {
            try {
                ReflectionUtil.sendPacket(player, DRAGONS.get(player).getDestroyPacket());
                DRAGONS.remove(player);
            } catch (Exception e) {
                //Bukkit.getLogger().log(Level.SEVERE, "Failed to remove a UG Boss Bar from a player.");
            }
        }
    }

    /**
     * Sets a player's status bar to display a specific message and fill amount.
     *
     * @param player  The player.
     * @param text    The text.
     * @param percent The fill amount.
     */
    public static void setStatusBar(Player player, String text, float percent) {
        FakeDragon dragon = DRAGONS.containsKey(player) ? DRAGONS.get(player) : null;

        if (text.length() > 64) {
            text = text.substring(0, 63);
        }
        percent = UGUtils.clamp(percent, 1.0f, 0.05f);

        try {
            if (dragon == null) {
                dragon = new FakeDragon(player.getLocation().add(0, -200, 0), text, percent);
                ReflectionUtil.sendPacket(player, dragon.getSpawnPacket());
                DRAGONS.put(player, dragon);
            } else {
                if (text.isEmpty()) {
                    removeStatusBar(player);
                }
                dragon.setName(text);
                dragon.setHealth(percent);
                ReflectionUtil.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
                ReflectionUtil.sendPacket(player, dragon.getTeleportPacket(player.getLocation().add(0, -200, 0)));
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to set the UG Boss Bar for a player.");
        }
    }

    /**
     * Removes the status bar for all players on the server.  See {@link #removeStatusBar(Player)}.
     */
    public static void removeAllStatusBars() {
        for (Player player : Canary.getServer().getPlayerList()) {
            removeStatusBar(player);
        }
    }

    /**
     * Sets the status bar for all players on the server.  See {@link #setStatusBar(Player, String, float)}.
     *
     * @param text    The text.
     * @param percent The fill amount.
     */
    public static void setAllStatusBars(String text, float percent) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setStatusBar(player, text, percent);
        }
    }

    private static class FakeDragon {
        private int id;
        private int x;
        private int y;
        private int z;
        private int pitch = 0;
        private int yaw = 0;
        private byte xvel = 0;
        private byte yvel = 0;
        private byte zvel = 0;
        private float health;
        private boolean visible = false;
        private String name;
        private Object world;

        private Object dragon;

        private static final int MAX_HEALTH = 200;

        public FakeDragon(Location loc, String name, float percent) {
            this.name = name;
            this.x = loc.getBlockX();
            this.y = loc.getBlockY();
            this.z = loc.getBlockZ();
            this.health = percent * MAX_HEALTH;
            try {
                this.world = ReflectionUtil.getHandle(loc.getWorld());
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to spawn a fake dragon for a UG Boss Bar.");
            }
        }

        public void setHealth(float percent) {
            this.health = percent / MAX_HEALTH;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getSpawnPacket() {
            try {
                Class<?> Entity = ReflectionUtil.getClass("Entity", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                Class<?> EntityLiving = ReflectionUtil.getClass("EntityLiving", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                Class<?> EntityEnderDragon = ReflectionUtil.getClass("EntityEnderDragon", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                dragon = EntityEnderDragon.getConstructor(ReflectionUtil.getClass("World", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER)).newInstance(world);

                ReflectionUtil.getMethod("setLocation", EntityEnderDragon, double.class, double.class, double.class, float.class, float.class).invoke(dragon, x, y, z, pitch, yaw);
                ReflectionUtil.getMethod("setInvisible", EntityEnderDragon, boolean.class).invoke(dragon, visible);
                ReflectionUtil.getMethod("setCustomName", EntityEnderDragon, String.class).invoke(dragon, name);
                ReflectionUtil.getMethod("setHealth", EntityEnderDragon, float.class).invoke(dragon, health);

                ReflectionUtil.getField("motX", Entity).set(dragon, xvel);
                ReflectionUtil.getField("motY", Entity).set(dragon, yvel);
                ReflectionUtil.getField("motZ", Entity).set(dragon, zvel);

                this.id = (Integer) ReflectionUtil.getMethod("getId", EntityEnderDragon).invoke(dragon);

                Class<?> packetClass = ReflectionUtil.getClass("PacketPlayOutSpawnEntityLiving", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                return packetClass.getConstructor(new Class<?>[]{EntityLiving}).newInstance(dragon);
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to get the spawn packet of a fake dragon for a UG Boss Bar.");
                return null;
            }
        }

        public Object getDestroyPacket() {
            try {
                Class<?> packetClass = ReflectionUtil.getClass("PacketPlayOutEntityDestroy", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                return packetClass.getConstructor(new Class<?>[]{int[].class}).newInstance(new int[]{id});
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to get the destroy packet of a fake dragon for a UG Boss Bar.");
                return null;
            }
        }

        public Object getMetaPacket(Object watcher) {
            try {
                Class<?> watcherClass = ReflectionUtil.getClass("DataWatcher", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                Class<?> packetClass = ReflectionUtil.getClass("PacketPlayOutEntityMetadata", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                return packetClass.getConstructor(new Class<?>[]{int.class, watcherClass, boolean.class}).newInstance(id, watcher, true);
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to get the meta packet of a fake dragon for a UG Boss Bar.");
                return null;
            }
        }

        public Object getTeleportPacket(Location loc) {
            try {
                Class<?> packetClass = ReflectionUtil.getClass("PacketPlayOutEntityTeleport", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                return packetClass.getConstructor(new Class<?>[]{int.class, int.class, int.class, int.class, byte.class, byte.class}).newInstance(this.id, loc.getBlockX() * 32, loc.getBlockY() * 32, loc.getBlockZ() * 32, (byte) ((int) loc.getYaw() * 256 / 360), (byte) ((int) loc.getPitch() * 256 / 360));
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to get the teleport packet of a fake dragon for a UG Boss Bar.");
                return null;
            }
        }

        public Object getWatcher() {
            try {
                Class<?> Entity = ReflectionUtil.getClass("Entity", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                Class<?> DataWatcher = ReflectionUtil.getClass("DataWatcher", ReflectionUtil.DynamicPackage.MINECRAFT_SERVER);
                Object watcher = DataWatcher.getConstructor(new Class<?>[]{Entity}).newInstance(dragon);
                Method a = ReflectionUtil.getMethod("a", DataWatcher, int.class, Object.class);


                a.invoke(watcher, 0, visible ? (byte) 0 : (byte) 0x20);
                a.invoke(watcher, 6, health);
                a.invoke(watcher, 7, 0);
                a.invoke(watcher, 8, (byte) 0);
                a.invoke(watcher, 10, name);
                a.invoke(watcher, 11, (byte) 1);
                return watcher;
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getLogger().log(Level.SEVERE, "Failed to get the datawatcher of a fake dragon for a UG Boss Bar.");
                return null;
            }
        }
    }

    /**
     * A class that attempts to overcome the traditional aversion to using org.bukkit.entity.Player as
     * the key type for a Map, namely that Player references can be quite large and we don't want to
     * keep them around after they're gone unless necessary.
     * <br><br>
     * This class is externally typed with {@link net.canarymod.api.entity.living.humanoid.Player} as the key type, but internally
     * uses {@link java.lang.String} as the key type, using the player's name.
     * <br><br>
     * In addition to this memory-saving measure, this map also allows the contents to be accessed through
     * either the player's name or the player object itself, meaning no more hassle with {@link Player#getName()}
     * or {@link net.canarymod.Canary} when you want to pull out of a map.
     *
     * @param <V> whatever you want to store
     * @author AmoebaMan
     */
    private static class PlayerMap<V> implements Map<Player, V> {
        private final V defaultValue;
        private final Map<String, V> contents;

        public PlayerMap() {
            contents = new HashMap<>();
            defaultValue = null;
        }

        public void clear() {
            contents.clear();
        }

        public boolean containsKey(Object key) {
            if (key instanceof Player) {
                return contents.containsKey(((Player) key).getName());
            } else if (key instanceof String) {
                return contents.containsKey(key);
            }
            return false;
        }

        public boolean containsValue(Object value) {
            return contents.containsValue(value);
        }

        public Set<Entry<Player, V>> entrySet() {
            Set<Entry<Player, V>> toReturn = new HashSet<>();
            for (String playerName : contents.keySet()) {
                toReturn.add(new PlayerEntry(Canary.getServer().getPlayer(playerName), contents.get(playerName)));
            }
            return toReturn;
        }

        public V get(Object key) {
            V result = null;
            if (key instanceof Player) {
                result = contents.get(((Player) key).getName());
            } else if (key instanceof String) {
                result = contents.get(key);
            }
            return (result == null) ? defaultValue : result;
        }

        public boolean isEmpty() {
            return contents.isEmpty();
        }

        public Set<Player> keySet() {
            Set<Player> toReturn = new HashSet<>();
            for (String playerName : contents.keySet()) {
                toReturn.add(Canary.getServer().getPlayer(playerName));
            }
            return toReturn;
        }

        public V put(Player key, V value) {
            return key == null ? null : contents.put(key.getName(), value);
        }

        public void putAll(Map<? extends Player, ? extends V> map) {
            for (Entry<? extends Player, ? extends V> entry : map.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }

        public V remove(Object key) {
            if (key instanceof Player) {
                return contents.remove(((Player) key).getName());
            } else if (key instanceof String) {
                return contents.remove(key);
            }
            return null;
        }

        public int size() {
            return contents.size();
        }

        public Collection<V> values() {
            return contents.values();
        }

        public String toString() {
            return contents.toString();
        }

        public class PlayerEntry implements Map.Entry<Player, V> {
            private Player key;
            private V value;

            public PlayerEntry(Player key, V value) {
                this.key = key;
                this.value = value;
            }

            public Player getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }

            public V setValue(V value) {
                V toReturn = this.value;
                this.value = value;
                return toReturn;
            }
        }
    }
}
