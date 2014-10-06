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
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;

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
                return player.getInventory().getHelmetSlot() == null;
            case CHESTPLATE:
                return player.getInventory().getChestplateSlot() == null;
            case LEGGINGS:
                return player.getInventory().getLeggingsSlot() == null;
            case BOOTS:
                return player.getInventory().getBootsSlot() == null;
            default:
                return false;
        }
    }

    /**
     * @param material The ArmorMaterial of the armor.
     *
     * @return The armor itemstack.
     */
    public Item getArmor(ArmorMaterial material) {
        return Canary.factory().getItemFactory().newItem(material.name() + "_" + name());
    }

    /**
     * Checks if a material is a piece of armor.
     *
     * @param material The material.
     * @return True if the material is a piece of armor, else false.
     */
    public static boolean isArmor(ItemType material) {
        for (ArmorType armorType : ArmorType.class.getEnumConstants()) {
            if (material.getMachineName().contains(armorType.name())) { //TODO wat
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
    public static ArmorType getArmorType(ItemType material) {
        for (ArmorType armorType : ArmorType.class.getEnumConstants()) {
            if (material.getMachineName().contains(armorType.name())) {
                return armorType;
            }
        }
        return null;
    }
}
