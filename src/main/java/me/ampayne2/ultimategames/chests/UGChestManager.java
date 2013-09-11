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
package me.ampayne2.ultimategames.chests;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.ChestType;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.chests.RandomChest;
import me.ampayne2.ultimategames.chests.StaticChest;
import me.ampayne2.ultimategames.chests.UGChest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Manages the UG chests for arenas.
 */
@SuppressWarnings("unchecked")
public class UGChestManager implements Manager {

    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private List<RandomChest> randomChests = new ArrayList<RandomChest>();
    private List<StaticChest> staticChests = new ArrayList<StaticChest>();
    private static final int WORLD_INDEX = 0;
    private static final int X_INDEX = 1;
    private static final int Y_INDEX = 2;
    private static final int Z_INDEX = 3;
    private static final int LABEL_INDEX = 4;
    private static final String PATH_SEPARATOR = ".";

    /**
     * Creates a new Chest Manager.
     * @param ultimateGames A reference to the UltimateGames instance.
     */
    public UGChestManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean load() {
        return reload();
    }

    @Override
    public boolean reload() {
        loadUGChests();
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        randomChests.clear();
        staticChests.clear();
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Gets the ChestType of a UG Chest.
     * @param chest The UG Chest.
     * @return The ChestType.
     */
    public ChestType getChestType(UGChest ugChest) {
        return ChestType.getChestTypeFromClass(ugChest.getClass());
    }

    /**
     * Checks to see if a chest is an Ultimate Game chest.
     * @param chest The chest to check.
     * @return If the chest is an Ultimate Game chest.
     */
    public boolean isUGChest(Chest chest) {
        return isRandomChest(chest) || isStaticChest(chest);
    }

    /**
     * Checks to see if a chest is a Random Chest.
     * @param chest The chest to check.
     * @return If the chest is a Random Chest.
     */
    public boolean isRandomChest(Chest chest) {
        for (RandomChest randomChest : randomChests) {
            if (chest.equals(randomChest.getChest())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a chest is a Click Input Chest.
     * @param chest The chest to check.
     * @return If the chest is an Input Chest.
     */
    public boolean isStaticChest(Chest chest) {
        for (StaticChest staticChest : staticChests) {
            if (chest.equals(staticChest.getChest())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the Ultimate Game chest of a chest.
     * @param chest The chest.
     * @return The Ultimate Game chest.
     */
    public UGChest getUGChest(Chest chest) {
        RandomChest randomChest = getRandomChest(chest);
        StaticChest staticChest = getStaticChest(chest);
        if (randomChest != null) {
            return randomChest;
        } else if (staticChest != null) {
            return staticChest;
        } else {
            return null;
        }
    }

    /**
     * Gets the Random Chest of a chest.
     * @param chest The chest.
     * @return The Random Chest.
     */
    public RandomChest getRandomChest(Chest chest) {
        for (RandomChest schest : randomChests) {
            if (chest.equals(schest.getChest())) {
                return schest;
            }
        }
        return null;
    }

    /**
     * Gets the Click Input Chest of a chest.
     * @param chest The chest.
     * @return The Click Input Chest.
     */
    public StaticChest getStaticChest(Chest chest) {
        for (StaticChest staticChest : staticChests) {
            if (chest.equals(staticChest.getChest())) {
                return staticChest;
            }
        }
        return null;
    }

    /**
     * Gets the Ultimate Game chests of an arena.
     * @param arena The arena.
     * @return The Ultimate Game Chests.
     */
    public List<UGChest> getUGChestsOfArena(Arena arena) {
        List<RandomChest> rChests = getRandomChestsOfArena(arena);
        List<StaticChest> sChests = getStaticChestsOfArena(arena);
        List<UGChest> ugChests = new ArrayList<UGChest>();
        ugChests.addAll(rChests);
        ugChests.addAll(sChests);
        return ugChests;
    }

    /**
     * Gets the Ultimate Game chests of a game.
     * @param game The game.
     * @return The Ultimate Game Chests.
     */
    public List<UGChest> getUGChestsOfGame(Game game) {
        List<RandomChest> rChests = getRandomChestsOfGame(game);
        List<StaticChest> sChests = getStaticChestsOfGame(game);
        List<UGChest> ugChests = new ArrayList<UGChest>();
        ugChests.addAll(rChests);
        ugChests.addAll(sChests);
        return ugChests;
    }

    /**
     * Gets the Random Chests of an arena.
     * @param arena The arena.
     * @return The Random Chests.
     */
    public List<RandomChest> getRandomChestsOfArena(Arena arena) {
        List<RandomChest> arenaChests = new ArrayList<RandomChest>();
        for (RandomChest randomChest : randomChests) {
            if (arena.equals(randomChest.getArena())) {
                arenaChests.add(randomChest);
            }
        }
        return arenaChests;
    }

    /**
     * Gets the Random Chests of a game.
     * @param game The game.
     * @return The Random Chests.
     */
    public List<RandomChest> getRandomChestsOfGame(Game game) {
        List<RandomChest> gameChests = new ArrayList<RandomChest>();
        for (RandomChest randomChest : randomChests) {
            if (game.equals(randomChest.getArena().getGame())) {
                gameChests.add(randomChest);
            }
        }
        return gameChests;
    }

    /**
     * Gets the Click Input Chests of an arena.
     * @param arena The arena.
     * @return The Input Chests.
     */
    public List<StaticChest> getStaticChestsOfArena(Arena arena) {
        List<StaticChest> arenaChests = new ArrayList<StaticChest>();
        for (StaticChest staticChest : staticChests) {
            if (arena.equals(staticChest.getArena())) {
                arenaChests.add(staticChest);
            }
        }
        return arenaChests;
    }

    /**
     * Gets the Click Input Chests of a game.
     * @param game The game.
     * @return The Input Chests.
     */
    public List<StaticChest> getStaticChestsOfGame(Game game) {
        List<StaticChest> gameChests = new ArrayList<StaticChest>();
        for (StaticChest staticChest : staticChests) {
            if (game.equals(staticChest.getArena().getGame())) {
                gameChests.add(staticChest);
            }
        }
        return gameChests;
    }

    /**
     * Creates a UG Chest and adds it to the manager.
     * @param ugChest The UG Chest to add.
     */
    public UGChest createUGChest(String label, Chest chest, Arena arena, ChestType chestType) {
        if (loaded) {
            FileConfiguration ugChestConfig = ultimateGames.getConfigManager().getUGChestConfig().getConfig();
            String chestPath = chestType.toString() + PATH_SEPARATOR + arena.getGame().getName() + PATH_SEPARATOR + arena.getName();
            List<String> chestInfo = new ArrayList<String>();
            chestInfo.add(WORLD_INDEX, chest.getWorld().getName());
            chestInfo.add(X_INDEX, Integer.toString(chest.getX()));
            chestInfo.add(Y_INDEX, Integer.toString(chest.getY()));
            chestInfo.add(Z_INDEX, Integer.toString(chest.getZ()));
            if (chestType.hasLabel()) {
                chestInfo.add(LABEL_INDEX, label);
            }
            List<List<String>> ugChests;
            if (ugChestConfig.contains(chestPath)) {
                ugChests = (List<List<String>>) ugChestConfig.getList(chestPath);
                ugChests.add(chestInfo);
            } else {
                ugChests = new ArrayList<List<String>>();
                ugChests.add(chestInfo);
                ugChestConfig.createSection(chestPath);
            }
            ugChestConfig.set(chestPath, ugChests);
            ultimateGames.getConfigManager().getUGChestConfig().saveConfig();
            switch (chestType) {
                case RANDOM:
                    RandomChest randomChest = new RandomChest(ultimateGames, chest, arena);
                    randomChests.add(randomChest);
                    return randomChest;
                case STATIC:
                    StaticChest staticChest = new StaticChest(ultimateGames, label, chest, arena);
                    staticChests.add(staticChest);
                    return staticChest;
            }
        }
        return null;
    }

    /**
     * Removes a UG Chest from the manager and config.
     * @param chest The chest of the UG Chest to remove.
     */
    public void removeUGChest(Chest chest) {
        UGChest ugChest = getUGChest(chest);
        if (loaded && ugChest != null) {
            FileConfiguration ugChestConfig = ultimateGames.getConfigManager().getUGChestConfig().getConfig();
            String world = chest.getWorld().getName();
            Integer x = chest.getX();
            Integer y = chest.getY();
            Integer z = chest.getZ();
            ChestType chestType = getChestType(ugChest);
            String gamePath = chestType.toString() + PATH_SEPARATOR + ugChest.getArena().getGame().getName();
            String arenaPath = gamePath + PATH_SEPARATOR + ugChest.getArena().getName();
            if (ugChestConfig.contains(arenaPath)) {
                List<List<String>> ugChests = (List<List<String>>) ugChestConfig.getList(arenaPath);
                List<List<String>> newUGChests = new ArrayList<List<String>>(ugChests);
                for (List<String> chestInfo : ugChests) {
                    if (world.equals(chestInfo.get(WORLD_INDEX)) && x.equals(Integer.parseInt(chestInfo.get(X_INDEX))) && y.equals(Integer.parseInt(chestInfo.get(Y_INDEX)))
                            && z.equals(Integer.parseInt(chestInfo.get(Z_INDEX)))) {
                        newUGChests.remove(chestInfo);
                    }
                }
                ugChestConfig.set(arenaPath, newUGChests);
                if (ugChestConfig.getList(arenaPath).isEmpty()) {
                    ugChestConfig.set(arenaPath, null);
                }
                if (ugChestConfig.getConfigurationSection(gamePath).getKeys(true).isEmpty()) {
                    ugChestConfig.set(gamePath, null);
                }
                ultimateGames.getConfigManager().getUGChestConfig().saveConfig();
            }
            switch (chestType) {
                case RANDOM:
                    randomChests.remove(ugChest);
                    break;
                case STATIC:
                    staticChests.remove(ugChest);
                    break;
            }
        }
    }

    /**
     * Loads all of the Ultimate Game chests.
     */
    public void loadUGChests() {
        randomChests.clear();
        staticChests.clear();
        FileConfiguration ugChestConfig = ultimateGames.getConfigManager().getUGChestConfig().getConfig();
        for (ChestType chestType : EnumSet.allOf(ChestType.class)) {
            String chestTypeName = chestType.toString();
            if (ugChestConfig.getConfigurationSection(chestTypeName) != null) {
                for (String gameKey : ugChestConfig.getConfigurationSection(chestTypeName).getKeys(false)) {
                    if (ultimateGames.getGameManager().gameExists(gameKey)) {
                        String gamePath = chestTypeName + PATH_SEPARATOR + gameKey;
                        for (String arenaKey : ugChestConfig.getConfigurationSection(gamePath).getKeys(false)) {
                            if (ultimateGames.getArenaManager().arenaExists(arenaKey, gameKey)) {
                                String arenaPath = gamePath + PATH_SEPARATOR + arenaKey;
                                List<List<String>> ugChests = (List<List<String>>) ugChestConfig.getList(arenaPath);
                                for (List<String> chestInfo : ugChests) {
                                    World world = Bukkit.getWorld(chestInfo.get(WORLD_INDEX));
                                    Integer x = Integer.parseInt(chestInfo.get(X_INDEX));
                                    Integer y = Integer.parseInt(chestInfo.get(Y_INDEX));
                                    Integer z = Integer.parseInt(chestInfo.get(Z_INDEX));
                                    Block locBlock = new Location(world, x, y, z).getBlock();
                                    if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
                                        Arena arena = ultimateGames.getArenaManager().getArena(arenaKey, gameKey);
                                        switch (chestType) {
                                            case RANDOM:
                                                randomChests.add(new RandomChest(ultimateGames, (Chest) locBlock.getState(), arena));
                                                break;
                                            case STATIC:
                                                staticChests.add(new StaticChest(ultimateGames, chestInfo.get(LABEL_INDEX), (Chest) locBlock.getState(), arena));
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
