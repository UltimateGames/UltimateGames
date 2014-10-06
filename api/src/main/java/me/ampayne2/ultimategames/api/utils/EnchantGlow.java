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

import net.canarymod.api.inventory.Enchantment;
import net.canarymod.api.inventory.Item;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;

import java.lang.reflect.Field;

/**
 * A utility to add enchantment glow effects to {@link org.bukkit.inventory.ItemStack}s.
 */
public final class EnchantGlow extends EnchantmentWrapper {
    private static final Enchantment glow;

    private EnchantGlow(int id) {
        super(id);
    }

    @Override
    public boolean canEnchantItem(Item item) {
        return true;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public String getName() {
        return "Glow";
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    /**
     * Adds an enchantment glow to an {@link org.bukkit.inventory.ItemStack}.
     *
     * @param item The {@link org.bukkit.inventory.ItemStack}.
     */
    public static void addGlow(Item item) {
        item.addEnchantment(glow, 1);
    }

    static {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            // Enchantment glow broke
        }

        glow = new EnchantGlow(255);
        Enchantment.registerEnchantment(glow);
    }
}
