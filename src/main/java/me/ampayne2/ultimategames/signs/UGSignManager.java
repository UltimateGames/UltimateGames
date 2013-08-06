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
import java.util.EnumSet;
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
    private static final Integer WORLD_INDEX = 0;
    private static final Integer X_INDEX = 1;
    private static final Integer Y_INDEX = 2;
    private static final Integer Z_INDEX = 3;
    private static final Integer LABEL_INDEX = 4;

    public UGSignManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        loadUGSigns();
    }

    /**
     * Gets the SignType of a UG Sign.
     * @param sign The UG Sign.
     * @return The SignType.
     */
    public SignType getSignType(UGSign sign) {
        return SignType.getSignTypeFromClass(sign.getClass());
    }

    /**
     * Checks to see if a sign is an Ultimate Game sign.
     * @param sign The sign to check.
     * @return If the sign is an Ultimate Game sign.
     */
    public boolean isUGSign(Sign sign) {
        return isLobbySign(sign) || isClickInputSign(sign) || isRedstoneInputSign(sign) || isTextOutputSign(sign) || isRedstoneOutputSign(sign.getLocation());
    }

    /**
     * Checks to see if a sign is a Lobby Sign.
     * @param sign The sign to check.
     * @return If the sign is a Lobby Sign.
     */
    public boolean isLobbySign(Sign sign) {
        for (LobbySign lobbySign : lobbySigns) {
            if (sign.equals(lobbySign.getSign())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a sign is a Click Input Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isClickInputSign(Sign sign) {
        for (ClickInputSign clickInputSign : clickInputSigns) {
            if (sign.equals(clickInputSign.getSign())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a sign is a Redstone Input Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isRedstoneInputSign(Sign sign) {
        for (RedstoneInputSign redstoneInputSign : redstoneInputSigns) {
            if (sign.equals(redstoneInputSign.getSign())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a sign is a Text Output Sign.
     * @param sign The sign to check.
     * @return If the sign is an Input Sign.
     */
    public boolean isTextOutputSign(Sign sign) {
        for (TextOutputSign textOutputSign : textOutputSigns) {
            if (sign.equals(textOutputSign.getSign())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if a location is a Redstone Output Sign or block.
     * @param location The location to check.
     * @return If the location is a Redstone Output Sign or block.
     */
    public boolean isRedstoneOutputSign(Location location) {
        for (RedstoneOutputSign redstoneOutputSign : redstoneOutputSigns) {
            if (location.equals(redstoneOutputSign.getSign().getLocation())) {
                return true;
            }
        }
        return false;
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
        RedstoneOutputSign redstoneOutputSign = getRedstoneOutputSign(sign.getLocation());
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
     * @return The Click Input Sign.
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
     * @return The Redstone Input Sign.
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
     * @return The Text Output Sign.
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
     * Gets the Redstone Output Sign of a location.
     * @param location The location.
     * @return The Redstone Output Sign.
     */
    public RedstoneOutputSign getRedstoneOutputSign(Location location) {
        for (RedstoneOutputSign redstoneOutputSign : redstoneOutputSigns) {
            if (location.equals(redstoneOutputSign.getSign().getLocation())) {
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
        List<LobbySign> lSigns = getLobbySignsOfArena(arena);
        List<ClickInputSign> ciSigns = getClickInputSignsOfArena(arena);
        List<RedstoneInputSign> riSigns = getRedstoneInputSignsOfArena(arena);
        List<TextOutputSign> toSigns = getTextOutputSignsOfArena(arena);
        List<RedstoneOutputSign> roSigns = getRedstoneOutputSignsOfArena(arena);
        List<UGSign> ugSigns = new ArrayList<UGSign>();
        ugSigns.addAll(lSigns);
        ugSigns.addAll(ciSigns);
        ugSigns.addAll(riSigns);
        ugSigns.addAll(toSigns);
        ugSigns.addAll(roSigns);
        return ugSigns;
    }

    /**
     * Gets the Ultimate Game signs of a game.
     * @param game The game.
     * @return The Ultimate Game Signs.
     */
    public List<UGSign> getUGSignsOfGame(Game game) {
        List<LobbySign> lSigns = getLobbySignsOfGame(game);
        List<ClickInputSign> ciSigns = getClickInputSignsOfGame(game);
        List<RedstoneInputSign> riSigns = getRedstoneInputSignsOfGame(game);
        List<TextOutputSign> toSigns = getTextOutputSignsOfGame(game);
        List<RedstoneOutputSign> roSigns = getRedstoneOutputSignsOfGame(game);
        List<UGSign> ugSigns = new ArrayList<UGSign>();
        ugSigns.addAll(lSigns);
        ugSigns.addAll(ciSigns);
        ugSigns.addAll(riSigns);
        ugSigns.addAll(toSigns);
        ugSigns.addAll(roSigns);
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
        List<ClickInputSign> ciSigns = getClickInputSignsOfArena(arena);
        for (ClickInputSign ciSign : ciSigns) {
            ciSign.update();
        }
    }

    /**
     * Updates the Click Input Signs of a game.
     * @param game The game.
     */
    public void updateClickInputSignsOfGame(Game game) {
        List<ClickInputSign> ciSigns = getClickInputSignsOfGame(game);
        for (ClickInputSign ciSign : ciSigns) {
            ciSign.update();
        }
    }

    /**
     * Updates the Redstone Input Signs of an arena.
     * @param arena The arena.
     */
    public void updateRedstoneInputSignsOfArena(Arena arena) {
        List<RedstoneInputSign> riSigns = getRedstoneInputSignsOfArena(arena);
        for (RedstoneInputSign riSign : riSigns) {
            riSign.update();
        }
    }

    /**
     * Updates the Redstone Input Signs of a game.
     * @param arena The arena.
     */
    public void updateRedstoneInputSignsOfGame(Game game) {
        List<RedstoneInputSign> riSigns = getRedstoneInputSignsOfGame(game);
        for (RedstoneInputSign riSign : riSigns) {
            riSign.update();
        }
    }

    /**
     * Updates the Text Output Signs of an arena.
     * @param arena The arena.
     */
    public void updateTextOutputSignsOfArena(Arena arena) {
        List<TextOutputSign> toSigns = getTextOutputSignsOfArena(arena);
        for (TextOutputSign toSign : toSigns) {
            toSign.update();
        }
    }

    /**
     * Updates the Text Output Signs of a game.
     * @param arena The arena.
     */
    public void updateTextOutputSignsOfGame(Game game) {
        List<TextOutputSign> toSigns = getTextOutputSignsOfGame(game);
        for (TextOutputSign toSign : toSigns) {
            toSign.update();
        }
    }

    /**
     * Updates the Redstone Output Signs of an arena.
     * @param arena The arena.
     */
    public void updateRedstoneOutputSignsOfArena(Arena arena) {
        List<RedstoneOutputSign> roSigns = getRedstoneOutputSignsOfArena(arena);
        for (RedstoneOutputSign roSign : roSigns) {
            roSign.update();
        }
    }

    /**
     * Updates the Redstone Output Signs of an arena.
     * @param arena The arena.
     */
    public void updateRedstoneOutputSignsOfGame(Game game) {
        List<RedstoneOutputSign> roSigns = getRedstoneOutputSignsOfGame(game);
        for (RedstoneOutputSign roSign : roSigns) {
            roSign.update();
        }
    }

    /**
     * Creates a UG Sign and adds it to the manager.
     * @param ugSign The UG Sign to add.
     */
    public UGSign createUGSign(String label, Sign sign, Arena arena, SignType signType) {
        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
        String signPath = signType.toString() + "." + arena.getGame().getGameDescription().getName() + "." + arena.getName();
        List<String> signInfo = new ArrayList<String>();
        signInfo.add(WORLD_INDEX, sign.getWorld().getName());
        signInfo.add(X_INDEX, Integer.toString(sign.getX()));
        signInfo.add(Y_INDEX, Integer.toString(sign.getY()));
        signInfo.add(Z_INDEX, Integer.toString(sign.getZ()));
        if (signType.hasLabel()) {
            signInfo.add(LABEL_INDEX, label);
        }
        List<List<String>> ugSigns;
        if (ugSignConfig.contains(signPath)) {
            ugSigns = (List<List<String>>) ugSignConfig.getList(signPath);
            ugSigns.add(signInfo);
        } else {
            ugSigns = new ArrayList<List<String>>();
            ugSigns.add(signInfo);
            ugSignConfig.createSection(signPath);
        }
        ugSignConfig.set(signPath, ugSigns);
        ultimateGames.getConfigManager().getUGSignConfig().saveConfig();
        switch (signType) {
        case LOBBY:
            LobbySign lobbySign = new LobbySign(ultimateGames, sign, arena);
            lobbySigns.add(lobbySign);
            return lobbySign;
        case CLICK_INPUT:
            ClickInputSign clickInputSign = new ClickInputSign(label, sign, arena);
            clickInputSigns.add(clickInputSign);
            return clickInputSign;
        case REDSTONE_INPUT:
            RedstoneInputSign redstoneInputSign = new RedstoneInputSign(label, sign, arena);
            redstoneInputSigns.add(redstoneInputSign);
            return redstoneInputSign;
        case TEXT_OUTPUT:
            TextOutputSign textOutputSign = new TextOutputSign(label, sign, arena);
            textOutputSigns.add(textOutputSign);
            return textOutputSign;
        case REDSTONE_OUTPUT:
            RedstoneOutputSign redstoneOutputSign = new RedstoneOutputSign(label, sign, arena);
            redstoneOutputSigns.add(redstoneOutputSign);
            return redstoneOutputSign;
        }
        return null;
    }

    /**
     * Removes a UG Sign from the manager and config.
     * @param sign The sign of the UG Sign to remove.
     */
    public void removeUGSign(Sign sign) {
        UGSign ugSign = getUGSign(sign);
        if (ugSign != null) {
            FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
            String world = sign.getWorld().getName();
            Integer x = sign.getX();
            Integer y = sign.getY();
            Integer z = sign.getZ();
            SignType signType = getSignType(ugSign);
            String gamePath = signType.toString() + "." + ugSign.getArena().getGame().getGameDescription().getName();
            String arenaPath = gamePath + "." + ugSign.getArena().getName();
            if (ugSignConfig.contains(arenaPath)) {
                List<List<String>> ugSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
                List<List<String>> newUGSigns = new ArrayList<List<String>>(ugSigns);
                for (List<String> signInfo : ugSigns) {
                    if (world.equals(signInfo.get(WORLD_INDEX)) && x.equals(Integer.parseInt(signInfo.get(X_INDEX))) && y.equals(Integer.parseInt(signInfo.get(Y_INDEX)))
                            && z.equals(Integer.parseInt(signInfo.get(Z_INDEX)))) {
                        newUGSigns.remove(signInfo);
                    }
                }
                ugSignConfig.set(arenaPath, newUGSigns);
                if (ugSignConfig.getList(arenaPath).isEmpty()) {
                    ugSignConfig.set(arenaPath, null);
                }
                if (ugSignConfig.getConfigurationSection(gamePath).getKeys(true).isEmpty()) {
                    ugSignConfig.set(gamePath, null);
                }
                ultimateGames.getConfigManager().getUGSignConfig().saveConfig();
            }
            switch (signType) {
            case LOBBY:
                lobbySigns.remove(ugSign);
                break;
            case CLICK_INPUT:
                clickInputSigns.remove(ugSign);
                break;
            case REDSTONE_INPUT:
                redstoneInputSigns.remove(ugSign);
                break;
            case TEXT_OUTPUT:
                textOutputSigns.remove(ugSign);
                break;
            case REDSTONE_OUTPUT:
                redstoneOutputSigns.remove(ugSign);
                break;
            }
        }
    }

    /**
     * Loads all of the Ultimate Game signs.
     */
    public void loadUGSigns() {
        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getUGSignConfig().getConfig();
        for (SignType signType : EnumSet.allOf(SignType.class)) {
            String signTypeName = signType.toString();
            if (ugSignConfig.getConfigurationSection(signTypeName) != null) {
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
                                        Arena arena = ultimateGames.getArenaManager().getArena(arenaKey, gameKey);
                                        switch (signType) {
                                        case LOBBY:
                                            lobbySigns.add(new LobbySign(ultimateGames, (Sign) locBlock.getState(), arena));
                                            break;
                                        case CLICK_INPUT:
                                            clickInputSigns.add(new ClickInputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), arena));
                                            break;
                                        case REDSTONE_INPUT:
                                            redstoneInputSigns.add(new RedstoneInputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), arena));
                                            break;
                                        case TEXT_OUTPUT:
                                            textOutputSigns.add(new TextOutputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), arena));
                                            break;
                                        case REDSTONE_OUTPUT:
                                            redstoneOutputSigns.add(new RedstoneOutputSign(signInfo.get(LABEL_INDEX), (Sign) locBlock.getState(), arena));
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
