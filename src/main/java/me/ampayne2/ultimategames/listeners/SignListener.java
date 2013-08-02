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
package me.ampayne2.ultimategames.listeners;

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.signs.InputSign;
import me.ampayne2.ultimategames.signs.LobbySign;
import me.ampayne2.ultimategames.signs.UGSign;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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
        //TODO: Permission check
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
        String[] lines;
        if (signPrefix.equalsIgnoreCase(ultimateGames.getConfig().getString("LobbySignPrefix"))) {
            LobbySign lobbySign = ultimateGames.getUGSignManager().createLobbySign((Sign) event.getBlock().getState(), ultimateGames.getArenaManager().getArena(arenaName, gameName));
            lines = lobbySign.getUpdatedLines();
        } else if (signPrefix.equalsIgnoreCase(ultimateGames.getConfig().getString("InputSignPrefix"))) {
            InputSign inputSign = ultimateGames.getUGSignManager().createInputSign(label, (Sign) event.getBlock().getState(), ultimateGames.getArenaManager().getArena(arenaName, gameName));
            String[] inputSignLines = {"", label, "", ""};
            inputSign.setLines(inputSignLines);
            lines = inputSign.getUpdatedLines();
        } else {
            return;
        }
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
            // not right click or left click
            return;
        } else if (event.getClickedBlock().getType() != Material.WALL_SIGN && event.getClickedBlock().getType() != Material.SIGN_POST) {
            // not a sign
            return;
        }
        Sign sign = (Sign) event.getClickedBlock().getState();
        UGSign ugSign;
        if ((ugSign = ultimateGames.getUGSignManager().getUGSign(sign)) == null) {
            // not a UGSign
            return;
        }
        if (ugSign instanceof LobbySign) {
            LobbySign lobbySign = (LobbySign) ugSign;
            lobbySign.onSignClick(event);
        } else if (ugSign instanceof InputSign) {
            InputSign inputSign = (InputSign) ugSign;
            inputSign.onSignClick(event);
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
            UGSign ugSign;
            if ((ugSign = ultimateGames.getUGSignManager().getUGSign(sign)) == null) {
                // not a UGSign
                return;
            }
            //TODO: Permission check
            if (ugSign instanceof LobbySign) {
                ultimateGames.getUGSignManager().removeLobbySign(sign);
            } else if (ugSign instanceof InputSign) {
                ultimateGames.getUGSignManager().removeInputSign(sign);
            }
        }
    }
}
