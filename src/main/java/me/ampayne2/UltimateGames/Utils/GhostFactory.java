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
package me.ampayne2.UltimateGames.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * GhostFactory
 * Make players look like ghosts
 * 
 * Requirements:
 * - MC 1.5+
 * 
 * @author lenis0012
 */
public class GhostFactory {
    private static final String MC_VERSION;
    
    static {
        String version = "";
        if(!checkVersion(version)) {
            StringBuilder builder = new StringBuilder();
            for(int a = 0; a < 10; a++) {
                for(int b = 0; b < 10; b++) {
                    for(int c = 0; c < 10; c++) {
                        builder.setLength(0);
                        builder.append('v').append(a).append('_').append(b).append("_R").append(c).append('.');
                        version = builder.toString();
                        if(checkVersion(version))
                            a = b = c = 10;
                    }
                }
            }
        }
        
        MC_VERSION = version;
    }
    
    private static final String NMS_ROOT = "net.minecraft.server." + MC_VERSION;
    
    private static boolean checkVersion(String version) {
        try {
            Class.forName("net.minecraft.server." + version + "World");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    private List<String> players = new ArrayList<String>();
    private boolean created = false;
    private boolean show;
    
    public GhostFactory(Plugin plugin, boolean show) {
        this.show = show;
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new GhostListener(this), plugin);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for(String user : players) {
                    Player player = Bukkit.getPlayer(user);
                    if(!player.hasPotionEffect(PotionEffectType.INVISIBILITY))
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
                }
            }
        }, 5L, 5L);
    }
    
    public void create() {
        if(created)
            return;
        
        this.update(0);
        this.created = true;
    }
    
    public void remove() {
        if(!created)
            return;
        
        this.update(1);
        this.created = false;
    }
    
    public boolean isCreated() {
        return this.created;
    }
    
    public void addGhost(Player player) {
        if(!show)
            this.addGhostBuster(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
    }
    
    public void removeGhost(Player player) {
        if(!show)
            this.removeGhostBuster(player);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }
    
    public boolean addGhostBuster(Player player) {
        if(players.add(player.getName())) {
            this.update(3);
            return true;
        } else
            return false;
    }
    
    public boolean removeGhostBuster(Player player) {
        if(players.remove(player.getName())) {
            this.update(4);
            return true;
        } else
            return false;
    }
    
    public boolean isGhostBuster(Player player) {
        return players.contains(player.getName());
    }
    
    public boolean isGhost(Player player) {
        return player.hasPotionEffect(PotionEffectType.INVISIBILITY);
    }
    
    public String[] getGhostBusters() {
        return players.toArray(new String[0]);
    }
    
    public void clearGhostBusters() {
        players.clear();
    }
    
    private void update(int action) {
        Object packet = this.createPacket("Packet209SetScoreboardTeam");
        this.setValue(packet, "a", "ghosts");
        this.setValue(packet, "f", action);
        this.setValue(packet, "b", "Ghosts");
        this.setValue(packet, "c", "");
        this.setValue(packet, "d", "");
        this.setValue(packet, "g", 2);
        this.setValue(packet, "e", players);
        
        for(Player player : Bukkit.getOnlinePlayers()) {
            this.sendPacket(player, packet);
        }
    }
    
    private void sendTo(Player player) {
        Object packet = this.createPacket("Packet209SetScoreboardTeam");
        this.setValue(packet, "a", "ghosts");
        this.setValue(packet, "f", 0);
        this.setValue(packet, "b", "Ghosts");
        this.setValue(packet, "c", "");
        this.setValue(packet, "d", "");
        this.setValue(packet, "g", 2);
        this.setValue(packet, "e", players);
        this.sendPacket(player, packet);
    }
    
    private void setValue(Object instance, String fieldName, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private Object getValue(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Object invoke(Object instance, String methodName, Class<?>[] fields, Object[] values) {
        try {
            Method method = instance.getClass().getDeclaredMethod(methodName, fields);
            method.setAccessible(true);
            return method.invoke(instance, values);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Object createPacket(String name) {
        try {
            Class<?> clazz = Class.forName(NMS_ROOT + name);
            return clazz.newInstance();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void sendPacket(Player player, Object packet) {
        try {
            Class<?> Packet = Class.forName(NMS_ROOT + "Packet");
            Object playerHandle = this.invoke(player, "getHandle", new Class<?>[0], new Class<?>[0]);
            Object playerConnection = this.getValue(playerHandle, "playerConnection");
            this.invoke(playerConnection, "sendPacket", new Class<?>[] {Packet}, new Object[] {packet});
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static class GhostListener implements Listener {
        private GhostFactory factory;
        
        public GhostListener(GhostFactory factory) {
            this.factory = factory;
        }
        
        @EventHandler(priority = EventPriority.MONITOR)
        public void onPlayerQuit(PlayerQuitEvent event) {
            Player player = event.getPlayer();
            factory.removeGhost(player);
            if(factory.show)
                factory.removeGhostBuster(player);
        }
        
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            factory.sendTo(player);
            if(factory.show)
                factory.addGhostBuster(player);
        }
    }
}