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

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Types of armor.
 */
public enum ArmorType {
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS;

    /**
     * Checks if the slot of the ArmorType is empty.
     *
     * @param player The player.
     * @return True if the slot is empty, else false.
     */
    public boolean canEquip(Player player) {
        switch (this) {
            case HELMET:
                return player.getInventory().getHelmet() == null;
            case CHESTPLATE:
                return player.getInventory().getChestplate() == null;
            case LEGGINGS:
                return player.getInventory().getLeggings() == null;
            case BOOTS:
                return player.getInventory().getBoots() == null;
            default:
                return false;
        }
    }

    /**
     * Checks if a material is a piece of armor.
     *
     * @param material The material.
     * @return True if the material is a piece of armor, else false.
     */
    public static boolean isArmor(Material material) {
        for (ArmorType armorType : ArmorType.class.getEnumConstants()) {
            if (material.name().contains(armorType.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ArmorType of a piece of armor.
     *
     * @param material The piece of armor.
     * @return The ArmorType.
     */
    public static ArmorType getArmorType(Material material) {
        for (ArmorType armorType : ArmorType.class.getEnumConstants()) {
            if (material.name().contains(armorType.name())) {
                return armorType;
            }
        }
        return null;
    }
}
