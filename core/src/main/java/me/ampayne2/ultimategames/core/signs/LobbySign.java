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
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.arenas.QueueManager;
import me.ampayne2.ultimategames.api.games.PlayerType;
import me.ampayne2.ultimategames.api.signs.SignType;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class LobbySign extends USign {
    private final UG ultimateGames;
    private final Arena arena;

    public LobbySign(UG ultimateGames, org.bukkit.block.Sign sign, Arena arena) {
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
        } else if (arenaStatus == ArenaStatus.RUNNING || arenaStatus == ArenaStatus.ENDING || arena.getPlayers().size() >= arena.getMaxPlayers()) {
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
        List<String> lines = new ArrayList<>();

        ArenaStatus arenaStatus = arena.getStatus();
        ChatColor statusColor = arenaStatus.getColor();

        lines.add(statusColor + "[" + arenaStatus.getDisplayName() + "]");
        lines.add(arena.getGame().getName());
        lines.add(arena.getName());

        PlayerType playerType = arena.getGame().getPlayerType();
        if (playerType == PlayerType.INFINITE) {
            lines.add("");
        } else {
            lines.add(statusColor + "" + arena.getPlayers().size() + " / " + arena.getMaxPlayers());
        }

        return lines;
    }
}
