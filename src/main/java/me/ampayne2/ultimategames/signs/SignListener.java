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

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.SignType;
import me.ampayne2.ultimategames.utils.UGUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SignListener implements Listener {
	private final UltimateGames ultimateGames;

	public SignListener(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}

	/**
	 * Handles UGSign Creation.
	 *
	 * @param event The event thrown when a sign is written on.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
		List<String> lines = ugSign.getUpdatedLines();
		for (int i = 0; i < 4; i++) {
			if (lines.size() > i) {
				event.setLine(i, lines.get(i));
			}
		}
	}

	/**
	 * Handles UGSign Clicking.
	 *
	 * @param event The event thrown when a sign is right or left clicked.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onSignClick(PlayerInteractEvent event) {
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
			return;
		} else if (!(event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST)) {
			return;
		}
		Sign sign = (Sign) event.getClickedBlock().getState();
		UGSign ugSign = ultimateGames.getUGSignManager().getUGSign(sign);
		if (ugSign != null && (ugSign instanceof LobbySign || ugSign instanceof ClickInputSign)) {
			ugSign.onSignTrigger(event);
		}
	}

	/**
	 * Handles Redstone Input Signs.
	 *
	 * @param event The event thrown when a block is powered.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSignPower(BlockRedstoneEvent event) {
		if (event.getBlock().getType() == Material.SIGN_POST || event.getBlock().getType() == Material.WALL_SIGN) {
			Sign sign = (Sign) event.getBlock().getState();
			if (ultimateGames.getUGSignManager().isUGSign(sign, SignType.REDSTONE_INPUT)) {
				RedstoneInputSign redstoneInputSign = (RedstoneInputSign) ultimateGames.getUGSignManager().getUGSign(sign, SignType.REDSTONE_INPUT);
				if ((redstoneInputSign.isPowered() && event.getNewCurrent() == 0) || (!redstoneInputSign.isPowered() && event.getNewCurrent() > 0)) {
					redstoneInputSign.setPowered(event.getNewCurrent() > 0);
					redstoneInputSign.onSignTrigger(event);
				}
			}
		}
	}

	/**
	 * Handles UGSign Breaking.
	 *
	 * @param event The event thrown when a sign is broken.
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Set<Sign> signs = new HashSet<Sign>();
		if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
			signs.add((Sign) event.getBlock().getState());
		}
		signs.addAll(UGUtils.getAttachedSigns(block, true));
		for (Sign sign : signs) {
			UGSign ugSign = ultimateGames.getUGSignManager().getUGSign(sign);
			if (ugSign != null) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onRedstoneBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getType() == Material.REDSTONE_BLOCK) {
			RedstoneOutputSign redstoneOutputSign = ultimateGames.getUGSignManager().getRedstoneOutputSign(event.getBlock().getLocation());
			if (redstoneOutputSign != null) {
				// TODO: Permission check
				redstoneOutputSign.setPowered(false);
				ultimateGames.getUGSignManager().removeUGSign(redstoneOutputSign.getSign());
			}
		}
	}
}
