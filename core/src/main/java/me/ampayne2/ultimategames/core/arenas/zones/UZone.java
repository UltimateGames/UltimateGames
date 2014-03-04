/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2014, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.core.arenas.zones;

import me.ampayne2.ultimategames.api.UltimateGames;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.zones.RadiusType;
import me.ampayne2.ultimategames.api.arenas.zones.Zone;
import me.ampayne2.ultimategames.api.config.ConfigType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class UZone implements Zone {
    private final UltimateGames ultimateGames;
    private final Arena arena;
    private final String name;
    private final Location center;
    private int radius;
    private int radiusSquared;
    private RadiusType radiusType;

    /**
     * Creates a new Zone.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param arena         The arena of the zone.
     * @param center        The center location of the zone.
     * @param radius        The radius of the zone.
     * @param radiusType    The RadiusType of the zone.
     */
    public UZone(UltimateGames ultimateGames, Arena arena, String name, Location center, int radius, RadiusType radiusType) {
        this.ultimateGames = ultimateGames;
        this.arena = arena;
        this.name = name;
        this.center = center;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.radiusType = radiusType;
    }

    /**
     * Loads a Zone from a ConfigurationSection.
     *
     * @param ultimateGames        The {@link me.ampayne2.ultimategames.api.UltimateGames} instance.
     * @param arena                The arena of the zone.
     * @param configurationSection The ConfigurationSection.
     */
    public UZone(UltimateGames ultimateGames, Arena arena, ConfigurationSection configurationSection) {
        this.ultimateGames = ultimateGames;
        this.arena = arena;
        this.name = configurationSection.getName();

        this.radius = configurationSection.getInt("Radius");
        this.radiusSquared = radius * radius;
        this.radiusType = RadiusType.valueOf(configurationSection.getString("RadiusType"));

        List<String> location = configurationSection.getStringList("Center");
        Double x = Double.valueOf(location.get(0));
        Double y = Double.valueOf(location.get(1));
        Double z = Double.valueOf(location.get(2));
        this.center = new Location(arena.getRegion().getWorld(), x, y, z);
        this.center.setPitch(Float.valueOf(location.get(3)));
        this.center.setYaw(Float.valueOf(location.get(4)));
    }

    @Override
    public Arena getArena() {
        return arena;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getCenterLocation() {
        return center;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public void setRadius(int radius) {
        if (radius != this.radius) {
            this.radius = radius;
            this.radiusSquared = radius * radius;

            getSection().set("Radius", radius);
            ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
        }
    }

    @Override
    public RadiusType getRadiusType() {
        return radiusType;
    }

    @Override
    public void setRadiusType(RadiusType radiusType) {
        if (!radiusType.equals(this.radiusType)) {
            this.radiusType = radiusType;

            getSection().set("RadiusType", radiusType.toString());
            ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
        }
    }

    @Override
    public boolean isLocationInZone(Location location) {
        double x = center.getX() - location.getX();
        double y = center.getY() - location.getY();
        double z = center.getZ() - location.getZ();
        switch (radiusType) {
            case RECTANGLE:
                return Math.abs(x) <= radius && Math.abs(z) <= radius;
            case CYLINDER:
                return (x * x) + (z * z) <= radiusSquared;
            case CUBE:
                return Math.abs(x) <= radius && Math.abs(y) <= radius && Math.abs(z) <= radius;
            case SPHERE:
                return (x * x) + (y * y) + (z * z) <= radiusSquared;
            default:
                return false;
        }
    }

    @Override
    public ConfigurationSection getSection() {
        ConfigurationSection arenaSection = arena.getSection();
        String zonePath = "Zones." + name;
        return arenaSection.contains(zonePath) ? arenaSection.getConfigurationSection(zonePath) : arenaSection.createSection(zonePath);
    }

    public void save() {
        ConfigurationSection section = getSection();
        section.set("Radius", radius);
        section.set("RadiusType", radiusType.toString());

        List<Object> location = new ArrayList<>();
        location.add(center.getX());
        location.add(center.getY());
        location.add(center.getZ());
        location.add(center.getPitch());
        location.add(center.getYaw());
        section.set("Center", location);

        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.ARENA).saveConfig();
    }
}
