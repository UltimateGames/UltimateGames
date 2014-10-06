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
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;

/**
 * Materials of armor.
 */
public enum ArmorMaterial {
    LEATHER,
    CHAINMAIL,
    IRON,
    GOLD,
    DIAMOND;

    /**
     * @param type The ArmorType of the armor.
     *
     * @return The armor itemstack.
     */
    public Item getArmor(ArmorType type) {
        return Canary.factory().getItemFactory().newItem(name() + "_" + type.name());
    }

    /**
     * Gets the ArmorMaterial of a piece of armor.
     *
     * @param material The piece of armor.
     * @return The ArmorMaterial.
     */
    public static ArmorMaterial getArmorMaterial(ItemType material) {
        for (ArmorMaterial armorMaterial : ArmorMaterial.class.getEnumConstants()) {
            if (material.getMachineName().contains(armorMaterial.name())) {
                return armorMaterial;
            }
        }
        return null;
    }
}
