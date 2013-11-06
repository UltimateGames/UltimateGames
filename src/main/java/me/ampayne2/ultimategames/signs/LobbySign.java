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
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.ArenaStatus;
import me.ampayne2.ultimategames.games.PlayerType;
import me.ampayne2.ultimategames.players.QueueManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class LobbySign extends UGSign {
    private final UltimateGames ultimateGames;
    private final Arena arena;

    public LobbySign(UltimateGames ultimateGames, Sign sign, Arena arena) {
        super(sign, arena, SignType.LOBBY);
        this.ultimateGames = ultimateGames;
        this.arena = arena;
        arena.getGame().getGamePlugin().handleUGSignCreate(this, getSignType());
        update();
    }

    @Override
    public void onSignTrigger(Event event) {
        // TODO: Permission check
        PlayerInteractEvent interactEvent = (PlayerInteractEvent) event;
        Player player = interactEvent.getPlayer();
        ArenaStatus arenaStatus = arena.getStatus();
        if (arenaStatus == ArenaStatus.OPEN || arenaStatus == ArenaStatus.STARTING) {
            // TODO: Save and clear player data (inventory, armor, levels, gamemode, effects)
            ultimateGames.getPlayerManager().addPlayerToArena(player, arena, true);
        } else if (arenaStatus == ArenaStatus.RUNNING || arenaStatus == ArenaStatus.ENDING || arenaStatus == ArenaStatus.RESETTING || arena.getPlayers().size() >= arena.getMaxPlayers()) {
            QueueManager queue = ultimateGames.getQueueManager();
            if (queue.isPlayerInQueue(player.getName(), arena)) {
                queue.removePlayerFromQueues(player);
            } else {
                queue.addPlayerToQueue(player, arena);
            }
        }
    }

    @Override
    public List<String> getUpdatedLines() {
        List<String> lines = new ArrayList<String>();

        ArenaStatus arenaStatus = arena.getStatus();
        ChatColor statusColor = arenaStatus.getColor();

        lines.add(statusColor + "[" + arenaStatus.toString() + "]");
        lines.add(arena.getGame().getName());
        lines.add(arena.getName());

        PlayerType playerType = arena.getGame().getPlayerType();
        if (playerType == PlayerType.INFINITE) {
            lines.add("");
        } else {
            lines.add(statusColor + Integer.toString(arena.getPlayers().size()) + " / " + Integer.toString(arena.getMaxPlayers()));
        }

        return lines;
    }
}
