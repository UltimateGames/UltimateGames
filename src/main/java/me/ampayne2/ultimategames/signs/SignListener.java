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
import me.ampayne2.ultimategames.enums.SignType;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    private UltimateGames ultimateGames;

    public SignListener(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    /**
     * Handles UGSign Creation.
     * @param event The event thrown when a sign is written on.
     */
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (!event.getPlayer().hasPermission("ultimategames.sign.create")) {
            return;
        }
        String signPrefix = event.getLine(0);
        String gameName = event.getLine(1);
        String arenaName = event.getLine(2);
        String label = event.getLine(3);
        if (signPrefix.equals("") || gameName.equals("") || arenaName.equals("")) {
            return;
        }
        if (!ultimateGames.getGameManager().gameExists(gameName) || !ultimateGames.getArenaManager().arenaExists(arenaName, gameName)) {
            return;
        }
        FileConfiguration config = ultimateGames.getConfig();
        SignType signType;
        if (signPrefix.equalsIgnoreCase(config.getString("LobbySignPrefix"))) {
            signType = SignType.LOBBY;
        } else if (signPrefix.equalsIgnoreCase(config.getString("ClickInputSignPrefix"))) {
            signType = SignType.CLICK_INPUT;
        } else if (signPrefix.equalsIgnoreCase(config.getString("RedstoneInputSignPrefix"))) {
            signType = SignType.REDSTONE_INPUT;
        } else if (signPrefix.equalsIgnoreCase(config.getString("TextOutputSignPrefix"))) {
            signType = SignType.TEXT_OUTPUT;
        } else if (signPrefix.equalsIgnoreCase(config.getString("RedstoneOutputSignPrefix"))) {
            signType = SignType.REDSTONE_OUTPUT;
        } else {
            return;
        }
        UGSign ugSign = ultimateGames.getUGSignManager().createUGSign(label, (Sign) event.getBlock().getState(), ultimateGames.getArenaManager().getArena(arenaName, gameName), signType);
        String[] lines = ugSign.getUpdatedLines();
        for (int i = 0; i < 4; i++) {
            event.setLine(i, lines[i]);
        }
    }

    /**
     * Handles UGSign Clicking.
     * @param event The event thrown when a sign is right or left clicked.
     */
    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        } else if (event.getClickedBlock().getType() != Material.WALL_SIGN && event.getClickedBlock().getType() != Material.SIGN_POST) {
            return;
        }
        Sign sign = (Sign) event.getClickedBlock().getState();
        UGSign ugSign = ultimateGames.getUGSignManager().getUGSign(sign);
        if (ugSign == null) {
            return;
        }
        if (ugSign instanceof LobbySign) {
            ugSign.onSignTrigger(event);
        } else if (ugSign instanceof ClickInputSign) {
            ugSign.onSignTrigger(event);
        }
    }

    /**
     * Handles Redstone Input Signs.
     * @param event The event thrown when a block is powered.
     */
    @EventHandler
    public void onSignPower(BlockRedstoneEvent event) {
        if (event.getBlock().getType() == Material.SIGN_POST || event.getBlock().getType() == Material.WALL_SIGN) {
            Sign sign = (Sign) event.getBlock().getState();
            if (ultimateGames.getUGSignManager().isRedstoneInputSign(sign)) {
                RedstoneInputSign redstoneInputSign = ultimateGames.getUGSignManager().getRedstoneInputSign(sign);
                if ((redstoneInputSign.isPowered() && event.getNewCurrent() == 0) || (!redstoneInputSign.isPowered() && event.getNewCurrent() > 0)) {
                    redstoneInputSign.setPowered(event.getNewCurrent() > 0 ? true : false);
                    redstoneInputSign.onSignTrigger(event);
                }
            }
        }
    }

    /**
     * Handles UGSign Breaking.
     * @param event The event thrown when a sign is broken.
     */
    @EventHandler
    public void onSignBreak(BlockBreakEvent event) {
        List<Sign> signs = new ArrayList<Sign>();
        if (event.getBlock().getType() == Material.WALL_SIGN || event.getBlock().getType() == Material.SIGN_POST) {
            signs.add((Sign) event.getBlock().getState());
        }
        if (event.getBlock().getRelative(BlockFace.UP).getType() == Material.SIGN_POST) {
            signs.add((Sign) event.getBlock().getRelative(BlockFace.UP).getState());
        }
        if (event.getBlock().getRelative(BlockFace.EAST).getType() == Material.WALL_SIGN) {
            signs.add((Sign) event.getBlock().getRelative(BlockFace.EAST).getState());
        }
        if (event.getBlock().getRelative(BlockFace.NORTH).getType() == Material.WALL_SIGN) {
            signs.add((Sign) event.getBlock().getRelative(BlockFace.NORTH).getState());
        }
        if (event.getBlock().getRelative(BlockFace.SOUTH).getType() == Material.WALL_SIGN) {
            signs.add((Sign) event.getBlock().getRelative(BlockFace.SOUTH).getState());
        }
        if (event.getBlock().getRelative(BlockFace.WEST).getType() == Material.WALL_SIGN) {
            signs.add((Sign) event.getBlock().getRelative(BlockFace.WEST).getState());
        }
        for (Sign sign : signs) {
            UGSign ugSign = ultimateGames.getUGSignManager().getUGSign(sign);
            if (ugSign == null) {
                return;
            }
            // TODO: Permission check
            ultimateGames.getUGSignManager().removeUGSign(sign);
        }
    }

    @EventHandler
    public void onRedstoneBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.REDSTONE_BLOCK && ultimateGames.getUGSignManager().isRedstoneOutputSign(event.getBlock().getLocation())) {
            // TODO: Permission check
            RedstoneOutputSign redstoneOutputSign = ultimateGames.getUGSignManager().getRedstoneOutputSign(event.getBlock().getLocation());
            redstoneOutputSign.setPowered(false);
            ultimateGames.getUGSignManager().removeUGSign(redstoneOutputSign.getSign());
        }
    }
}
