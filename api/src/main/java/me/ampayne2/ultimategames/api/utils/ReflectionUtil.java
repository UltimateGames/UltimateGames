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
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * ReflectionUtil v1.1
 *
 * You are welcome to use it, modify it and redistribute it under the condition to not claim this class as your own
 *
 * @author DarkBlade12
 */
public abstract class ReflectionUtil {
    private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<>();

    static {
        CORRESPONDING_TYPES.put(Byte.class, byte.class);
        CORRESPONDING_TYPES.put(Short.class, short.class);
        CORRESPONDING_TYPES.put(Integer.class, int.class);
        CORRESPONDING_TYPES.put(Long.class, long.class);
        CORRESPONDING_TYPES.put(Character.class, char.class);
        CORRESPONDING_TYPES.put(Float.class, float.class);
        CORRESPONDING_TYPES.put(Double.class, double.class);
        CORRESPONDING_TYPES.put(Boolean.class, boolean.class);
    }

    public enum DynamicPackage {
        MINECRAFT_SERVER {
            @Override
            public String toString() {
                return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().substring(23, 30);
            }
        },
        CRAFTBUKKIT {
            @Override
            public String toString() {
                return Bukkit.getServer().getClass().getPackage().getName();
            }
        }
    }

    public static class FieldEntry {
        private String key;
        private Object value;

        public FieldEntry(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }
    }

    private static Class<?> getPrimitiveType(Class<?> clazz) {
        return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES.get(clazz) : clazz;
    }

    private static Class<?>[] toPrimitiveTypeArray(Object[] objects) {
        int a = objects != null ? objects.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++) {
            types[i] = getPrimitiveType(objects[i].getClass());
        }
        return types;
    }

    private static Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++) {
            types[i] = getPrimitiveType(classes[i]);
        }
        return types;
    }

    private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(o[i]) && !a[i].equals(o[i].getSuperclass())) {
                return false;
            }
        }
        return true;
    }

    public static Class<?> getClass(String name, DynamicPackage pack, String subPackage) throws ClassNotFoundException {
        return Class.forName(pack + (subPackage != null && subPackage.length() > 0 ? "." + subPackage : "") + "." + name);
    }

    public static Class<?> getClass(String name, DynamicPackage pack) throws ClassNotFoundException {
        return getClass(name, pack, null);
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Constructor<?> c : clazz.getConstructors()) {
            Class<?>[] types = toPrimitiveTypeArray(c.getParameterTypes());
            if (equalsTypeArray(types, t)) {
                return c;
            }
        }
        return null;
    }

    public static Object newInstance(Class<?> clazz, Object... args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return getConstructor(clazz, toPrimitiveTypeArray(args)).newInstance(args);
    }

    public static Object newInstance(String name, DynamicPackage pack, String subPackage, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return newInstance(getClass(name, pack, subPackage), args);
    }

    public static Object newInstance(String name, DynamicPackage pack, Object... args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return newInstance(getClass(name, pack, null), args);
    }

    public static Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Method m : clazz.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
            if (m.getName().equals(name) && equalsTypeArray(types, t)) {
                return m;
            }
        }
        return null;
    }

    public static Object invokeMethod(String name, Class<?> clazz, Object obj, Object... args) throws IllegalAccessException, InvocationTargetException {
        return getMethod(name, clazz, toPrimitiveTypeArray(args)).invoke(obj, args);
    }

    public static Field getField(String name, Class<?> clazz) throws NoSuchFieldException {
        return clazz.getDeclaredField(name);
    }

    public static Object getValue(String name, Object obj) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(name, obj.getClass());
        f.setAccessible(true);
        return f.get(obj);
    }

    public static void setValue(Object obj, FieldEntry entry) throws NoSuchFieldException, IllegalAccessException {
        Field f = getField(entry.getKey(), obj.getClass());
        f.setAccessible(true);
        f.set(obj, entry.getValue());
    }

    public static void setValues(Object obj, FieldEntry... entrys) throws NoSuchFieldException, IllegalAccessException {
        for (FieldEntry f : entrys) {
            setValue(obj, f);
        }
    }

    public static Object getHandle(Entity entity) throws IllegalAccessException, InvocationTargetException {
        return getMethod("getHandle", entity.getClass()).invoke(entity);
    }

    public static Object getHandle(World world) throws IllegalAccessException, InvocationTargetException {
        return getMethod("getHandle", world.getClass()).invoke(world);
    }

    public static void sendPacket(Player player, Object packet) throws IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Object nmsPlayer = getHandle(player);
        Field connectionField = nmsPlayer.getClass().getField("playerConnection");
        Object connection = connectionField.get(nmsPlayer);
        Method sendPacket = getMethod("sendPacket", connection.getClass());
        sendPacket.invoke(connection, packet);
    }
}