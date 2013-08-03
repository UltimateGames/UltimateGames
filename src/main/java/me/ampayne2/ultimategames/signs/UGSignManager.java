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
import me.ampayne2.ultimategames.enums.SignType;
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
    private List<LobbySign> lobbySigns = new ArrayList<LobbySign>();
    private List<ClickInputSign> clickInputSigns = new ArrayList<ClickInputSign>();
    private List<RedstoneInputSign> redstoneInputSigns = new ArrayList<RedstoneInputSign>();
    private List<TextOutputSign> textOutputSigns = new ArrayList<TextOutputSign>();
    private List<RedstoneOutputSign> redstoneOutputSigns = new ArrayList<RedstoneOutputSign>();
    private final Integer WORLD_INDEX = 0;
    private final Integer X_INDEX = 1;
    private final Integer Y_INDEX = 2;
    private final Integer Z_INDEX = 3;
    private final Integer LABEL_INDEX = 4;

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
        return isLobbySign(sign) || isClickInputSign(sign) || isRedstoneInputSign(sign) || isTextOutputSign(sign) || isRedstoneOutputSign(sign);
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
     * Checks to see if a sign is a Click Input Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isClickInputSign(Sign sign) {
        return getClickInputSign(sign) == null;
    }
    
    /**
     * Checks to see if a sign is a Redstone Input Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isRedstoneInputSign(Sign sign) {
        return getRedstoneInputSign(sign) == null;
    }
    
    /**
     * Checks to see if a sign is a Text Output Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isTextOutputSign(Sign sign) {
        return getTextOutputSign(sign) == null;
    }
    
    /**
     * Checks to see if a sign is a Redstone Output Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isRedstoneOutputSign(Sign sign) {
        return getRedstoneOutputSign(sign) == null;
    }

    /**
     * Gets the Ultimate Game sign of a sign.
     * @param sign The sign.
     * @return The Ultimate Game sign.
     */
    public UGSign getUGSign(Sign sign) {
        LobbySign lobbySign = getLobbySign(sign);
        ClickInputSign clickInputSign = getClickInputSign(sign);
        RedstoneInputSign redstoneInputSign = getRedstoneInputSign(sign);
        TextOutputSign textOutputSign = getTextOutputSign(sign);
        RedstoneOutputSign redstoneOutputSign = getRedstoneOutputSign(sign);
        if (lobbySign != null) {
            return lobbySign;
        } else if (clickInputSign != null) {
            return clickInputSign;
        } else if (redstoneInputSign != null) {
        	return redstoneInputSign;
        } else if (textOutputSign != null) {
        	return textOutputSign;
        } else if (redstoneOutputSign != null) {
        	return redstoneOutputSign;
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
     * Gets the Click Input Sign of a sign.
     * @param sign The sign.
     * @return The Input Sign.
     */
    public ClickInputSign getClickInputSign(Sign sign) {
        for (ClickInputSign clickInputSign : clickInputSigns) {
            if (sign.equals(clickInputSign.getSign())) {
                return clickInputSign;
            }
        }
        return null;
    }
    
    /**
     * Gets the Redstone Input Sign of a sign.
     * @param sign The sign.
     * @return The Input Sign.
     */
    public RedstoneInputSign getRedstoneInputSign(Sign sign) {
        for (RedstoneInputSign redstoneInputSign : redstoneInputSigns) {
            if (sign.equals(redstoneInputSign.getSign())) {
                return redstoneInputSign;
            }
        }
        return null;
    }
    
    /**
     * Gets the Text Output Sign of a sign.
     * @param sign The sign.
     * @return The Input Sign.
     */
    public TextOutputSign getTextOutputSign(Sign sign) {
        for (TextOutputSign textOutputSign : textOutputSigns) {
            if (sign.equals(textOutputSign.getSign())) {
                return textOutputSign;
            }
        }
        return null;
    }
    
    /**
     * Gets the Redstone Output Sign of a sign.
     * @param sign The sign.
     * @return The Input Sign.
     */
    public RedstoneOutputSign getRedstoneOutputSign(Sign sign) {
        for (RedstoneOutputSign redstoneOutputSign : redstoneOutputSigns) {
            if (sign.equals(redstoneOutputSign.getSign())) {
                return redstoneOutputSign;
            }
        }
        return null;
    }

    /**
     * Gets the Ultimate Game signs of an arena.
     * @param arena The arena.
     * @return The Ultimate Game Signs.
     */
    public List<UGSign> getUGSignsOfArena(Arena arena) {
        List<LobbySign> lobbySigns = getLobbySignsOfArena(arena);
        List<ClickInputSign> clickInputSigns = getClickInputSignsOfArena(arena);
        List<RedstoneInputSign> redstoneInputSigns = getRedstoneInputSignsOfArena(arena);
        List<TextOutputSign> textOutputSigns = getTextOutputSignsOfArena(arena);
        List<RedstoneOutputSign> redstoneOutputSigns = getRedstoneOutputSignsOfArena(arena);
        List<UGSign> ugSigns = new ArrayList<UGSign>();
        ugSigns.addAll(lobbySigns);
        ugSigns.addAll(clickInputSigns);
        ugSigns.addAll(redstoneInputSigns);
        ugSigns.addAll(textOutputSigns);
        ugSigns.addAll(redstoneOutputSigns);
        return ugSigns;
    }

    /**
     * Gets the Ultimate Game signs of a game.
     * @param game The game.
     * @return The Ultimate Game Signs.
     */
    public List<UGSign> getUGSignsOfGame(Game game) {
        List<LobbySign> lobbySigns = getLobbySignsOfGame(game);
        List<ClickInputSign> clickInputSigns = getClickInputSignsOfGame(game);
        List<RedstoneInputSign> redstoneInputSigns = getRedstoneInputSignsOfGame(game);
        List<TextOutputSign> textOutputSigns = getTextOutputSignsOfGame(game);
        List<RedstoneOutputSign> redstoneOutputSigns = getRedstoneOutputSignsOfGame(game);
        List<UGSign> ugSigns = new ArrayList<UGSign>();
        ugSigns.addAll(lobbySigns);
        ugSigns.addAll(clickInputSigns);
        ugSigns.addAll(redstoneInputSigns);
        ugSigns.addAll(textOutputSigns);
        ugSigns.addAll(redstoneOutputSigns);
        return ugSigns;
    }

    /**
     * Gets the Lobby Signs of an arena.
     * @param arena The arena.
     * @return The Lobby Signs.
     */
    public List<LobbySign> getLobbySignsOfArena(Arena arena) {
        List<LobbySign> arenaSigns = new ArrayList<LobbySign>();
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
    public List<LobbySign> getLobbySignsOfGame(Game game) {
        List<LobbySign> gameSigns = new ArrayList<LobbySign>();
        for (LobbySign lobbySign : lobbySigns) {
            if (game.equals(lobbySign.getArena().getGame())) {
                gameSigns.add(lobbySign);
            }
        }
        return gameSigns;
    }

    /**
     * Gets the Click Input Signs of an arena.
     * @param arena The arena.
     * @return The Input Signs.
     */
    public List<ClickInputSign> getClickInputSignsOfArena(Arena arena) {
        List<ClickInputSign> arenaSigns = new ArrayList<ClickInputSign>();
        for (ClickInputSign clickInputSign : clickInputSigns) {
            if (arena.equals(clickInputSign.getArena())) {
                arenaSigns.add(clickInputSign);
            }
        }
        return arenaSigns;
    }

    /**
     * Gets the Click Input Signs of a game.
     * @param game The game.
     * @return The Input Signs.
     */
    public List<ClickInputSign> getClickInputSignsOfGame(Game game) {
        List<ClickInputSign> gameSigns = new ArrayList<ClickInputSign>();
        for (ClickInputSign clickInputSign : clickInputSigns) {
            if (game.equals(clickInputSign.getArena().getGame())) {
                gameSigns.add(clickInputSign);
            }
        }
        return gameSigns;
    }
    
    /**
     * Gets the Redstone Input Signs of an arena.
     * @param arena The arena.
     * @return The Input Signs.
     */
    public List<RedstoneInputSign> getRedstoneInputSignsOfArena(Arena arena) {
        List<RedstoneInputSign> arenaSigns = new ArrayList<RedstoneInputSign>();
        for (RedstoneInputSign redstoneInputSign : redstoneInputSigns) {
            if (arena.equals(redstoneInputSign.getArena())) {
                arenaSigns.add(redstoneInputSign);
            }
        }
        return arenaSigns;
    }

    /**
     * Gets the Redstone Input Signs of a game.
     * @param game The game.
     * @return The Input Signs.
     */
    public List<RedstoneInputSign> getRedstoneInputSignsOfGame(Game game) {
        List<RedstoneInputSign> gameSigns = new ArrayList<RedstoneInputSign>();
        for (RedstoneInputSign redstoneInputSign : redstoneInputSigns) {
            if (game.equals(redstoneInputSign.getArena().getGame())) {
                gameSigns.add(redstoneInputSign);
            }
        }
        return gameSigns;
    }
    
    /**
     * Gets the Text Output Signs of an arena.
     * @param arena The arena.
     * @return The Input Signs.
     */
    public List<TextOutputSign> getTextOutputSignsOfArena(Arena arena) {
        List<TextOutputSign> arenaSigns = new ArrayList<TextOutputSign>();
        for (TextOutputSign textOutputSign : textOutputSigns) {
            if (arena.equals(textOutputSign.getArena())) {
                arenaSigns.add(textOutputSign);
            }
        }
        return arenaSigns;
    }

    /**
     * Gets the Text Output Signs of a game.
     * @param game The game.
     * @return The Input Signs.
     */
    public List<TextOutputSign> getTextOutputSignsOfGame(Game game) {
        List<TextOutputSign> gameSigns = new ArrayList<TextOutputSign>();
        for (TextOutputSign textOutputSign : textOutputSigns) {
            if (game.equals(textOutputSign.getArena().getGame())) {
                gameSigns.add(textOutputSign);
            }
        }
        return gameSigns;
    }
    
    /**
     * Gets the Redstone Output Signs of an arena.
     * @param arena The arena.
     * @return The Input Signs.
     */
    public List<RedstoneOutputSign> getRedstoneOutputSignsOfArena(Arena arena) {
        List<RedstoneOutputSign> arenaSigns = new ArrayList<RedstoneOutputSign>();
        for (RedstoneOutputSign redstoneOutputSign : redstoneOutputSigns) {
            if (arena.equals(redstoneOutputSign.getArena())) {
                arenaSigns.add(redstoneOutputSign);
            }
        }
        return arenaSigns;
    }

    /**
     * Gets the Redstone Output Signs of a game.
     * @param game The game.
     * @return The Input Signs.
     */
    public List<RedstoneOutputSign> getRedstoneOutputSignsOfGame(Game game) {
        List<RedstoneOutputSign> gameSigns = new ArrayList<RedstoneOutputSign>();
        for (RedstoneOutputSign redstoneOutputSign : redstoneOutputSigns) {
            if (game.equals(redstoneOutputSign.getArena().getGame())) {
                gameSigns.add(redstoneOutputSign);
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
        updateClickInputSignsOfArena(arena);
        updateRedstoneInputSignsOfArena(arena);
        updateTextOutputSignsOfArena(arena);
        updateRedstoneOutputSignsOfArena(arena);
    }

    /**
     * Updates the Ultimate Game signs of a game.
     * @param game The game.
     */
    public void updateUGSignsOfGame(Game game) {
        updateLobbySignsOfGame(game);
        updateClickInputSignsOfGame(game);
        updateRedstoneInputSignsOfGame(game);
        updateTextOutputSignsOfGame(game);
        updateRedstoneOutputSignsOfGame(game);
    }

    /**
     * Updates the Lobby Signs of an arena.
     * @param arena The arena.
     */
    public void updateLobbySignsOfArena(Arena arena) {
        List<LobbySign> arenaSigns = getLobbySignsOfArena(arena);
        for (LobbySign lobbySign : arenaSigns) {
            lobbySign.update();
        }
    }

    /**
     * Updates the Lobby Signs of a game.
     * @param game The game.
     */
    public void updateLobbySignsOfGame(Game game) {
        List<LobbySign> gameSigns = getLobbySignsOfGame(game);
        for (LobbySign lobbySign : gameSigns) {
            lobbySign.update();
        }
    }

    /**
     * Updates the Click Input Signs of an arena.
     * @param arena The arena.
     */
    public void updateClickInputSignsOfArena(Arena arena) {
        List<ClickInputSign> clickInputSigns = getClickInputSignsOfArena(arena);
        for (ClickInputSign clickInputSign : clickInputSigns) {
            clickInputSign.update();
        }
    }

    /**
     * Updates the Click Input Signs of a game.
     * @param game The game.
     */
    public void updateClickInputSignsOfGame(Game game) {
        List<ClickInputSign> clickInputSigns = getClickInputSignsOfGame(game);
        for (ClickInputSign clickInputSign : clickInputSigns) {
            clickInputSign.update();
        }
    }
    
    /**
     * Updates the Redstone Input Signs of an arena.
     * @param arena The arena.
     */
    public void updateRedstoneInputSignsOfArena(Arena arena) {
        List<RedstoneInputSign> redstoneInputSigns = getRedstoneInputSignsOfArena(arena);
        for (RedstoneInputSign redstoneInputSign : redstoneInputSigns) {
            redstoneInputSign.update();
        }
    }
    
    /**
     * Updates the Redstone Input Signs of a game.
     * @param arena The arena.
     */
    public void updateRedstoneInputSignsOfGame(Game game) {
        List<RedstoneInputSign> redstoneInputSigns = getRedstoneInputSignsOfGame(game);
        for (RedstoneInputSign redstoneInputSign : redstoneInputSigns) {
            redstoneInputSign.update();
        }
    }
    
    /**
     * Updates the Text Output Signs of an arena.
     * @param arena The arena.
     */
    public void updateTextOutputSignsOfArena(Arena arena) {
        List<TextOutputSign> textOutputSigns = getTextOutputSignsOfArena(arena);
        for (TextOutputSign textOutputSign : textOutputSigns) {
            textOutputSign.update();
        }
    }
    
    /**
     * Updates the Text Output Signs of a game.
     * @param arena The arena.
     */
    public void updateTextOutputSignsOfGame(Game game) {
        List<TextOutputSign> textOutputSigns = getTextOutputSignsOfGame(game);
        for (TextOutputSign textOutputSign : textOutputSigns) {
            textOutputSign.update();
        }
    }
    
    /**
     * Updates the Redstone Output Signs of an arena.
     * @param arena The arena.
     */
    public void updateRedstoneOutputSignsOfArena(Arena arena) {
        List<RedstoneOutputSign> redstoneOutputSigns = getRedstoneOutputSignsOfArena(arena);
        for (RedstoneOutputSign redstoneOutputSign : redstoneOutputSigns) {
            redstoneOutputSign.update();
        }
    }
    
    /**
     * Updates the Redstone Output Signs of an arena.
     * @param arena The arena.
     */
    public void updateRedstoneOutputSignsOfGame(Game game) {
        List<RedstoneOutputSign> redstoneOutputSigns = getRedstoneOutputSignsOfGame(game);
        for (RedstoneOutputSign redstoneOutputSign : redstoneOutputSigns) {
            redstoneOutputSign.update();
        }
    }
    
    /**
     * Adds a UG Sign to the manager.
     * @param ugSign The UG Sign to add.
     */
    public void addUGSign(UGSign ugSign) {
    	
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
        addUGSign(lobbySign);

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
    public ClickInputSign createInputSign(String label, Sign sign, Arena arena) {
        if (isClickInputSign(sign)) {
            // already an input sign here
            return null;
        }
        ClickInputSign inputSign = new ClickInputSign(label, sign, arena);
        addUGSign(inputSign);

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
        if (isClickInputSign(sign)) {
            FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
            String world = sign.getWorld().getName();
            Integer x = sign.getX();
            Integer y = sign.getY();
            Integer z = sign.getZ();
            ClickInputSign inputSign = getClickInputSign(sign);
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
            clickInputSigns.remove(inputSign);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Loads all of the Ultimate Game signs.
     */
    public void loadUGSigns() {
        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
        for (SignType signType : SignType.values()) {
        	String signTypeName = signType.toString();
        	if (ugSignConfig.getConfigurationSection(signTypeName) == null) {
        		return;
        	} else {
        		for (String gameKey : ugSignConfig.getConfigurationSection(signTypeName).getKeys(false)) {
        			if (ultimateGames.getGameManager().gameExists(gameKey)) {
        				String gamePath = signTypeName + "." + gameKey;
        				for (String arenaKey : ugSignConfig.getConfigurationSection(gamePath).getKeys(false)) {
        					if (ultimateGames.getArenaManager().arenaExists(arenaKey, gameKey)) {
        						String arenaPath = gamePath + "." + arenaKey;
        						List<List<String>> ugSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
        						for (List<String> signInfo : ugSigns) {
        							World world = Bukkit.getWorld(signInfo.get(WORLD_INDEX));
        							Integer x = Integer.parseInt(signInfo.get(X_INDEX));
        							Integer y = Integer.parseInt(signInfo.get(Y_INDEX));
        							Integer z = Integer.parseInt(signInfo.get(Z_INDEX));
                                    Block locBlock = new Location(world, x, y, z).getBlock();
                                    if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
                                    	switch (signType) {
                                    		case LOBBY:
                                    			lobbySigns.add(new LobbySign(ultimateGames, (Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
                                    			break;
                                    		case CLICK_INPUT:
                                    			clickInputSigns.add(new ClickInputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
                                    			break;
                                    		case REDSTONE_INPUT:
                                    			redstoneInputSigns.add(new RedstoneInputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
                                    			break;
                                    		case TEXT_OUTPUT:
                                    			textOutputSigns.add(new TextOutputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
                                    			break;
                                    		case REDSTONE_OUTPUT:
                                    			redstoneOutputSigns.add(new RedstoneOutputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), ultimateGames.getArenaManager().getArena(arenaKey, gameKey)));
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
