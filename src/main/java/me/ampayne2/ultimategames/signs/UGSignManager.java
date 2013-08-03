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
package me.ampayne2.ultimategames.signs;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings("unchecked")
public class UGSignManager {
    private UltimateGames ultimateGames;
    private ArrayList<LobbySign> lobbySigns;
    private ArrayList<InputSign> inputSigns;

    public UGSignManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        loadUGSigns();
    }

    /**
     * Checks to see if a sign is an Ultimate Game sign.
     * @param sign The sign to check.
     * @return If the sign is an Ultimate Game sign.
     */
    public boolean isUGSign(Sign sign) {
        return isLobbySign(sign) || isInputSign(sign);
    }

    /**
     * Checks to see if a sign is a Lobby Sign.
     * @param sign The sign to check.
     * @return If the sign is a Lobby Sign.
     */
    public boolean isLobbySign(Sign sign) {
        return getLobbySign(sign) == null;
    }

    /**
     * Checks to see if a sign is an Input Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isInputSign(Sign sign) {
        return getInputSign(sign) == null;
    }

    /**
     * Gets the Ultimate Game sign of a sign.
     * @param sign The sign.
     * @return The Ultimate Game sign.
     */
    public UGSign getUGSign(Sign sign) {
        LobbySign lobbySign = getLobbySign(sign);
        InputSign inputSign = getInputSign(sign);
        if (lobbySign != null) {
            return lobbySign;
        } else if (inputSign != null) {
            return inputSign;
        } else {
            return null;
        }
    }

    /**
     * Gets the Lobby Sign of a sign.
     * @param sign The sign.
     * @return The Lobby Sign.
     */
    public LobbySign getLobbySign(Sign sign) {
        for (LobbySign ssign : lobbySigns) {
            if (sign.equals(ssign.getSign())) {
                return ssign;
            }
        }
        return null;
    }

    /**
     * Gets the Input Sign of a sign.
     * @param sign The sign.
     * @return The Input Sign.
     */
    public InputSign getInputSign(Sign sign) {
        for (InputSign ssign : inputSigns) {
            if (sign.equals(ssign.getSign())) {
                return ssign;
            }
        }
        return null;
    }

    /**
     * Gets the Ultimate Game signs of an arena.
     * @param arena The arena.
     * @return The Ultimate Game Signs.
     */
    public ArrayList<UGSign> getUGSignsOfArena(Arena arena) {
        ArrayList<LobbySign> lobbySigns = getLobbySignsOfArena(arena);
        ArrayList<InputSign> inputSigns = getInputSignsOfArena(arena);
        ArrayList<UGSign> ugSigns = new ArrayList<UGSign>();
        ugSigns.addAll(lobbySigns);
        ugSigns.addAll(inputSigns);
        return ugSigns;
    }

    /**
     * Gets the Ultimate Game signs of a game.
     * @param game The game.
     * @return The Ultimate Game Signs.
     */
    public ArrayList<UGSign> getUGSignsOfGame(Game game) {
        ArrayList<LobbySign> lobbySigns = getLobbySignsOfGame(game);
        ArrayList<InputSign> inputSigns = getInputSignsOfGame(game);
        ArrayList<UGSign> ugSigns = new ArrayList<UGSign>();
        ugSigns.addAll(lobbySigns);
        ugSigns.addAll(inputSigns);
        return ugSigns;
    }

    /**
     * Gets the Lobby Signs of an arena.
     * @param arena The arena.
     * @return The Lobby Signs.
     */
    public ArrayList<LobbySign> getLobbySignsOfArena(Arena arena) {
        ArrayList<LobbySign> arenaSigns = new ArrayList<LobbySign>();
        for (LobbySign lobbySign : lobbySigns) {
            if (arena.equals(lobbySign.getArena())) {
                arenaSigns.add(lobbySign);
            }
        }
        return arenaSigns;
    }

    /**
     * Gets the Lobby Signs of a game.
     * @param game The game.
     * @return The Lobby Signs.
     */
    public ArrayList<LobbySign> getLobbySignsOfGame(Game game) {
        ArrayList<LobbySign> gameSigns = new ArrayList<LobbySign>();
        for (LobbySign lobbySign : lobbySigns) {
            if (game.equals(lobbySign.getArena().getGame())) {
                gameSigns.add(lobbySign);
            }
        }
        return gameSigns;
    }

    /**
     * Gets the Input Signs of an arena.
     * @param arena The arena.
     * @return The Input Signs.
     */
    public ArrayList<InputSign> getInputSignsOfArena(Arena arena) {
        ArrayList<InputSign> arenaSigns = new ArrayList<InputSign>();
        for (InputSign inputSign : inputSigns) {
            if (arena.equals(inputSign.getArena())) {
                arenaSigns.add(inputSign);
            }
        }
        return inputSigns;
    }

    /**
     * Gets the Input Signs of a game.
     * @param game The game.
     * @return The Input Signs.
     */
    public ArrayList<InputSign> getInputSignsOfGame(Game game) {
        ArrayList<InputSign> gameSigns = new ArrayList<InputSign>();
        for (InputSign inputSign : inputSigns) {
            if (game.equals(inputSign.getArena().getGame())) {
                gameSigns.add(inputSign);
            }
        }
        return gameSigns;
    }

    /**
     * Updates the Ultimate Game signs of an arena.
     * @param arena The arena.
     */
    public void updateUGSignsOfArena(Arena arena) {
        updateLobbySignsOfArena(arena);
        updateInputSignsOfArena(arena);
    }

    /**
     * Updates the Ultimate Game signs of a game.
     * @param game The game.
     */
    public void updateUGSignsOfGame(Game game) {
        updateLobbySignsOfGame(game);
        updateInputSignsOfGame(game);
    }

    /**
     * Updates the Lobby Signs of an arena.
     * @param arena The arena.
     */
    public void updateLobbySignsOfArena(Arena arena) {
        ArrayList<LobbySign> arenaSigns = getLobbySignsOfArena(arena);
        for (LobbySign lobbySign : arenaSigns) {
            lobbySign.update();
        }
    }

    /**
     * Updates the Lobby Signs of a game.
     * @param game The game.
     */
    public void updateLobbySignsOfGame(Game game) {
        ArrayList<LobbySign> gameSigns = getLobbySignsOfGame(game);
        for (LobbySign lobbySign : gameSigns) {
            lobbySign.update();
        }
    }

    /**
     * Updates the Input Signs of an arena.
     * @param arena The arena.
     */
    public void updateInputSignsOfArena(Arena arena) {
        ArrayList<InputSign> inputSigns = getInputSignsOfArena(arena);
        for (InputSign inputSign : inputSigns) {
            inputSign.update();
        }
    }

    /**
     * Updates the Input Signs of a game.
     * @param game The game.
     */
    public void updateInputSignsOfGame(Game game) {
        ArrayList<InputSign> inputSigns = getInputSignsOfGame(game);
        for (InputSign inputSign : inputSigns) {
            inputSign.update();
        }
    }

    /**
     * Adds a Lobby Sign to the manager.
     * @param lobbySign The Lobby Sign to add.
     */
    public void addLobbySign(LobbySign lobbySign) {
        if (!lobbySigns.contains(lobbySign)) {
            lobbySigns.add(lobbySign);
        }
    }

    /**
     * Adds an Input Sign to the manager.
     * @param inputSign The Input Sign to add.
     */
    public void addInputSign(InputSign inputSign) {
        if (!inputSigns.contains(inputSign)) {
            inputSigns.add(inputSign);
        }
    }

    /**
     * Creates a Lobby Sign from the Sign for the arena and adds it to the manager and config.
     * @param sign The sign to turn into a Lobby Sign.
     * @param arena The arena the Lobby Sign will be created for.
     * @return The Lobby Sign created.
     */
    public LobbySign createLobbySign(Sign sign, Arena arena) {
        if (isLobbySign(sign)) {
            // already a lobby sign here
            return null;
        }
        LobbySign lobbySign = new LobbySign(ultimateGames, sign, arena);
        addLobbySign(lobbySign);

        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
        String arenaPath = "LobbySigns." + arena.getGame().getGameDescription().getName() + "." + arena.getName();
        List<String> signInfo = new ArrayList<String>();
        signInfo.add(0, sign.getWorld().getName());
        signInfo.add(1, Integer.toString(sign.getX()));
        signInfo.add(2, Integer.toString(sign.getY()));
        signInfo.add(3, Integer.toString(sign.getZ()));
        List<List<String>> lobbySigns;
        if (ugSignConfig.contains(arenaPath)) {
            lobbySigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
            lobbySigns.add(signInfo);
        } else {
            lobbySigns = new ArrayList<List<String>>();
            lobbySigns.add(signInfo);
            ugSignConfig.createSection(arenaPath);
        }
        ugSignConfig.set(arenaPath, lobbySigns);
        ultimateGames.getConfigManager().getUGSignConfig().saveConfig();

        return lobbySign;
    }

    /**
     * Creates a Input Sign from the Sign for the arena and adds it to the manager and config.
     * @param sign The sign to turn into a Input Sign.
     * @param arena The arena the Input Sign will be created for.
     * @return The Input Sign created.
     */
    public InputSign createInputSign(String label, Sign sign, Arena arena) {
        if (isInputSign(sign)) {
            // already an input sign here
            return null;
        }
        InputSign inputSign = new InputSign(label, sign, arena);
        addInputSign(inputSign);

        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
        String arenaPath = "InputSigns." + arena.getGame().getGameDescription().getName() + "." + arena.getName();
        List<String> signInfo = new ArrayList<String>();
        signInfo.add(0, sign.getWorld().getName());
        signInfo.add(1, Integer.toString(sign.getX()));
        signInfo.add(2, Integer.toString(sign.getY()));
        signInfo.add(3, Integer.toString(sign.getZ()));
        signInfo.add(4, label);
        List<List<String>> inputSigns;
        if (ugSignConfig.contains(arenaPath)) {
            inputSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
            inputSigns.add(signInfo);
        } else {
            inputSigns = new ArrayList<List<String>>();
            inputSigns.add(signInfo);
            ugSignConfig.createSection(arenaPath);
        }
        ugSignConfig.set(arenaPath, inputSigns);
        ultimateGames.getConfigManager().getUGSignConfig().saveConfig();

        return inputSign;
    }

    /**
     * Removes a Lobby Sign from the manager and config.
     * @param sign The sign of the Lobby Sign to remove.
     * @return Whether or not the remove was successful.
     */
    public boolean removeLobbySign(Sign sign) {
        if (isLobbySign(sign)) {
            FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
            String world = sign.getWorld().getName();
            Integer x = sign.getX();
            Integer y = sign.getY();
            Integer z = sign.getZ();
            LobbySign lobbySign = getLobbySign(sign);
            String gamePath = "LobbySigns." + lobbySign.getArena().getGame().getGameDescription().getName();
            String arenaPath = gamePath + "." + lobbySign.getArena().getName();
            if (ugSignConfig.contains(arenaPath)) {
                List<List<String>> lobbySigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
                List<List<String>> newLobbySigns = new ArrayList<List<String>>(lobbySigns);
                for (List<String> signInfo : lobbySigns) {
                    if (world.equals(signInfo.get(0)) && x.equals(Integer.parseInt(signInfo.get(1))) && y.equals(Integer.parseInt(signInfo.get(2))) && z.equals(Integer.parseInt(signInfo.get(3)))) {
                        newLobbySigns.remove(signInfo);
                    }
                }
                ugSignConfig.set(arenaPath, newLobbySigns);
                if (ugSignConfig.getList(arenaPath).isEmpty()) {
                    ugSignConfig.set(arenaPath, null);
                }
                if (ugSignConfig.getConfigurationSection(gamePath).getKeys(true).isEmpty()) {
                    ugSignConfig.set(gamePath, null);
                }
                ultimateGames.getConfigManager().getUGSignConfig().saveConfig();
            }
            lobbySigns.remove(lobbySign);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an Input Sign from the manager and config.
     * @param sign The sign of the Input Sign to remove.
     * @return Whether or not the remove was successful.
     */
    public boolean removeInputSign(Sign sign) {
        if (isInputSign(sign)) {
            FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
            String world = sign.getWorld().getName();
            Integer x = sign.getX();
            Integer y = sign.getY();
            Integer z = sign.getZ();
            InputSign inputSign = getInputSign(sign);
            String gamePath = "InputSigns." + inputSign.getArena().getGame().getGameDescription().getName();
            String arenaPath = gamePath + "." + inputSign.getArena().getName();
            if (ugSignConfig.contains(arenaPath)) {
                List<List<String>> inputSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
                List<List<String>> newInputSigns = new ArrayList<List<String>>(inputSigns);
                for (List<String> signInfo : inputSigns) {
                    if (world.equals(signInfo.get(0)) && x.equals(Integer.parseInt(signInfo.get(1))) && y.equals(Integer.parseInt(signInfo.get(2))) && z.equals(Integer.parseInt(signInfo.get(3)))) {
                        newInputSigns.remove(signInfo);
                    }
                }
                ugSignConfig.set(arenaPath, newInputSigns);
                if (ugSignConfig.getList(arenaPath).isEmpty()) {
                    ugSignConfig.set(arenaPath, null);
                }
                if (ugSignConfig.getConfigurationSection(gamePath).getKeys(true).isEmpty()) {
                    ugSignConfig.set(gamePath, null);
                }
                ultimateGames.getConfigManager().getUGSignConfig().saveConfig();
            }
            inputSigns.remove(inputSign);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Loads all of the Ultimate Game signs.
     */
    public void loadUGSigns() {
        loadLobbySigns();
        loadInputSigns();
    }

    /**
     * Loads or reloads all of the Lobby Signs.
     */
    public void loadLobbySigns() {
        lobbySigns = new ArrayList<LobbySign>();
        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
        if (ugSignConfig.getConfigurationSection("LobbySigns") == null) {
            return;
        }
        for (String gameKey : ugSignConfig.getConfigurationSection("LobbySigns").getKeys(false)) {
            if (ultimateGames.getGameManager().gameExists(gameKey)) {
                String gamePath = "LobbySigns." + gameKey;
                for (String arenaKey : ugSignConfig.getConfigurationSection(gamePath).getKeys(false)) {
                    if (ultimateGames.getArenaManager().arenaExists(arenaKey, gameKey)) {
                        String arenaPath = gamePath + "." + arenaKey;
                        List<List<String>> lobbySigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
                        for (List<String> signInfo : lobbySigns) {
                            String world = signInfo.get(0);
                            String x = signInfo.get(1);
                            String y = signInfo.get(2);
                            String z = signInfo.get(3);
                            if (world != null && x != null && y != null && z != null) {
                                World signWorld = Bukkit.getWorld(world);
                                if (signWorld != null) {
                                    Block locBlock = new Location(signWorld, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)).getBlock();
                                    if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
                                        addLobbySign(new LobbySign(ultimateGames, (Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Loads or reloads all of the Input Signs.
     */
    public void loadInputSigns() {
        inputSigns = new ArrayList<InputSign>();
        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
        if (ugSignConfig.getConfigurationSection("InputSigns") == null) {
            return;
        }
        for (String gameKey : ugSignConfig.getConfigurationSection("InputSigns").getKeys(false)) {
            if (ultimateGames.getGameManager().gameExists(gameKey)) {
                String gamePath = "InputSigns." + gameKey;
                for (String arenaKey : ugSignConfig.getConfigurationSection(gamePath).getKeys(false)) {
                    if (ultimateGames.getArenaManager().arenaExists(arenaKey, gameKey)) {
                        String arenaPath = gamePath + "." + arenaKey;
                        List<List<String>> inputSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
                        for (List<String> signInfo : inputSigns) {
                            String world = signInfo.get(0);
                            String x = signInfo.get(1);
                            String y = signInfo.get(2);
                            String z = signInfo.get(3);
                            String label = signInfo.get(4);
                            if (world != null && x != null && y != null && z != null && label != null) {
                                World signWorld = Bukkit.getWorld(world);
                                if (signWorld != null) {
                                    Block locBlock = new Location(signWorld, Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)).getBlock();
                                    if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
                                        addInputSign(new InputSign(label, (Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
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
