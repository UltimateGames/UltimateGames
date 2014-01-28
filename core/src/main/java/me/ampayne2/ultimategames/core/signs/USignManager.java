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
package me.ampayne2.ultimategames.core.signs;

import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.config.ConfigType;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.signs.Sign;
import me.ampayne2.ultimategames.api.signs.SignManager;
import me.ampayne2.ultimategames.api.signs.SignType;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
public class USignManager implements SignManager {
    private final UG ultimateGames;
    private final Map<SignType, List<USign>> ugSigns = new HashMap<>();

    public USignManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        loadSigns();
    }

    @Override
    public boolean isSign(org.bukkit.block.Sign sign, SignType signType) {
        for (USign ugSign : ugSigns.get(signType)) {
            if (sign.equals(ugSign.getSign())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSign(org.bukkit.block.Sign sign) {
        for (Entry<SignType, List<USign>> entry : ugSigns.entrySet()) {
            for (USign ugSign : entry.getValue()) {
                if (sign.equals(ugSign.getSign())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public RedstoneOutputSign getRedstoneOutputSign(Location location) {
        for (USign ugSign : ugSigns.get(SignType.REDSTONE_OUTPUT)) {
            if (ugSign.getSign().getLocation().equals(location)) {
                return (RedstoneOutputSign) ugSign;
            }
        }
        return null;
    }

    @Override
    public USign getSign(org.bukkit.block.Sign sign, SignType signType) {
        for (USign ugSign : ugSigns.get(signType)) {
            if (sign.equals(ugSign.getSign())) {
                return ugSign;
            }
        }
        return null;
    }

    @Override
    public USign getSign(org.bukkit.block.Sign sign) {
        for (Entry<SignType, List<USign>> entry : ugSigns.entrySet()) {
            for (USign ugSign : entry.getValue()) {
                if (sign.equals(ugSign.getSign())) {
                    return ugSign;
                }
            }
        }
        return null;
    }

    @Override
    public List<Sign> getSignsOfArena(Arena arena, SignType signType) {
        List<Sign> signs = new ArrayList<>();
        for (USign sign : ugSigns.get(signType)) {
            if (sign.getArena().equals(arena)) {
                signs.add(sign);
            }
        }
        return signs;
    }

    @Override
    public List<Sign> getSignsOfArena(Arena arena) {
        List<Sign> signs = new ArrayList<>();
        for (Entry<SignType, List<USign>> entry : ugSigns.entrySet()) {
            for (USign sign : entry.getValue()) {
                if (sign.getArena().equals(arena)) {
                    signs.add(sign);
                }
            }
        }
        return signs;
    }

    @Override
    public List<Sign> getSignsOfGame(Game game, SignType signType) {
        List<Sign> signs = new ArrayList<>();
        for (USign sign : ugSigns.get(signType)) {
            if (sign.getArena().getGame().equals(game)) {
                signs.add(sign);
            }
        }
        return signs;
    }

    @Override
    public List<Sign> getSignsOfGame(Game game) {
        List<Sign> signs = new ArrayList<>();
        for (Entry<SignType, List<USign>> entry : ugSigns.entrySet()) {
            for (USign sign : entry.getValue()) {
                if (sign.getArena().getGame().equals(game)) {
                    signs.add(sign);
                }
            }
        }
        return signs;
    }

    @Override
    public void updateSignsOfArena(Arena arena, SignType signType) {
        for (Sign sign : getSignsOfArena(arena, signType)) {
            sign.update();
        }
        ultimateGames.getMessenger().debug("Updated signs of arena " + arena.getName() + " of game " + arena.getGame().getName() + " of type " + signType.name());
    }

    @Override
    public void updateSignsOfArena(Arena arena) {
        for (Sign sign : getSignsOfArena(arena)) {
            sign.update();
        }
        ultimateGames.getMessenger().debug("Updated signs of arena " + arena.getName() + " of game " + arena.getGame().getName());
    }

    @Override
    public void updateSignsOfGame(Game game, SignType signType) {
        for (Sign sign : getSignsOfGame(game, signType)) {
            sign.update();
        }
    }

    @Override
    public void updateSignsOfGame(Game game) {
        for (Sign sign : getSignsOfGame(game)) {
            sign.update();
        }
    }

    public void addSign(USign sign, SignType signType) {
        ugSigns.get(signType).add(sign);
    }

    /**
     * Creates a UG Sign and adds it to the manager.
     *
     * @param label    The sign's label.
     * @param sign     The physical sign.
     * @param arena    The arena the sign is in.
     * @param signType The sign's type.
     * @return The UG Sign created.
     */
    public USign createSign(String label, org.bukkit.block.Sign sign, Arena arena, SignType signType) {
        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getConfig(ConfigType.SIGN);
        String signPath = signType.toString() + "." + arena.getGame().getName() + "." + arena.getName();
        List<String> signInfo = new ArrayList<>();
        signInfo.add(0, sign.getWorld().getName());
        signInfo.add(1, Integer.toString(sign.getX()));
        signInfo.add(2, Integer.toString(sign.getY()));
        signInfo.add(3, Integer.toString(sign.getZ()));
        if (signType.hasLabel()) {
            signInfo.add(4, label);
        }
        List<List<String>> ugSigns;
        if (ugSignConfig.contains(signPath)) {
            ugSigns = (List<List<String>>) ugSignConfig.getList(signPath);
            ugSigns.add(signInfo);
        } else {
            ugSigns = new ArrayList<>();
            ugSigns.add(signInfo);
            ugSignConfig.createSection(signPath);
        }
        ugSignConfig.set(signPath, ugSigns);
        ultimateGames.getConfigManager().getConfigAccessor(ConfigType.SIGN).saveConfig();
        USign ugSign = null;
        switch (signType) {
            case LOBBY:
                ugSign = new LobbySign(ultimateGames, sign, arena);
                break;
            case CLICK_INPUT:
                ugSign = new ClickInputSign(label, sign, arena);
                break;
            case REDSTONE_INPUT:
                ugSign = new RedstoneInputSign(label, sign, arena);
                break;
            case TEXT_OUTPUT:
                ugSign = new TextOutputSign(label, sign, arena);
                break;
            case REDSTONE_OUTPUT:
                ugSign = new RedstoneOutputSign(label, sign, arena);
                break;
        }
        addSign(ugSign, signType);
        return ugSign;
    }

    /**
     * Removes a UG Sign from the manager and config.
     *
     * @param sign The sign of the UG Sign to remove.
     */
    public void removeSign(org.bukkit.block.Sign sign) {
        USign ugSign = getSign(sign);
        if (ugSign != null) {
            FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getConfig(ConfigType.SIGN);
            String world = sign.getWorld().getName();
            Integer x = sign.getX();
            Integer y = sign.getY();
            Integer z = sign.getZ();
            SignType signType = ugSign.getSignType();
            String gamePath = signType.toString() + "." + ugSign.getArena().getGame().getName();
            String arenaPath = gamePath + "." + ugSign.getArena().getName();
            if (ugSignConfig.contains(arenaPath)) {
                List<List<String>> ugSigns = (List<List<String>>) ugSignConfig.getList(arenaPath);
                List<List<String>> newUGSigns = new ArrayList<>(ugSigns);
                for (List<String> signInfo : ugSigns) {
                    if (world.equals(signInfo.get(0)) && x.equals(Integer.parseInt(signInfo.get(1))) && y.equals(Integer.parseInt(signInfo.get(2))) && z.equals(Integer.parseInt(signInfo.get(3)))) {
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
                ultimateGames.getConfigManager().getConfigAccessor(ConfigType.SIGN).saveConfig();
            }
            ugSigns.get(signType).remove(ugSign);
        }
    }

    /**
     * Loads all of the Ultimate Game signs.
     */
    public void loadSigns() {
        ugSigns.clear();
        FileConfiguration ugSignConfig = ultimateGames.getConfigManager().getConfig(ConfigType.SIGN);
        for (SignType signType : EnumSet.allOf(SignType.class)) {
            ugSigns.put(signType, new ArrayList<USign>());
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
                                    World world = Bukkit.getWorld(signInfo.get(0));
                                    Integer x = Integer.parseInt(signInfo.get(1));
                                    Integer y = Integer.parseInt(signInfo.get(2));
                                    Integer z = Integer.parseInt(signInfo.get(3));
                                    Block locBlock = new Location(world, x, y, z).getBlock();
                                    if (locBlock.getType() == Material.WALL_SIGN || locBlock.getType() == Material.SIGN_POST) {
                                        Arena arena = ultimateGames.getArenaManager().getArena(arenaKey, gameKey);
                                        USign ugSign = null;
                                        switch (signType) {
                                            case LOBBY:
                                                ugSign = new LobbySign(ultimateGames, (org.bukkit.block.Sign) locBlock.getState(), arena);
                                                break;
                                            case CLICK_INPUT:
                                                ugSign = new ClickInputSign(signInfo.get(4), (org.bukkit.block.Sign) locBlock.getState(), arena);
                                                break;
                                            case REDSTONE_INPUT:
                                                ugSign = new RedstoneInputSign(signInfo.get(4), (org.bukkit.block.Sign) locBlock.getState(), arena);
                                                break;
                                            case TEXT_OUTPUT:
                                                ugSign = new TextOutputSign(signInfo.get(4), (org.bukkit.block.Sign) locBlock.getState(), arena);
                                                break;
                                            case REDSTONE_OUTPUT:
                                                ugSign = new RedstoneOutputSign(signInfo.get(4), (org.bukkit.block.Sign) locBlock.getState(), arena);
                                                break;
                                        }
                                        addSign(ugSign, signType);
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
