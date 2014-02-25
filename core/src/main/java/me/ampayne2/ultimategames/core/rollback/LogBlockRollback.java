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
package me.ampayne2.ultimategames.core.rollback;

import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.worldedit.RegionContainer;
import me.ampayne2.ultimategames.api.arenas.Arena;
import me.ampayne2.ultimategames.api.arenas.ArenaStatus;
import me.ampayne2.ultimategames.api.rollback.RollbackManager;
import me.ampayne2.ultimategames.core.UG;
import me.ampayne2.ultimategames.core.arenas.UArena;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class LogBlockRollback implements RollbackManager {

    private LogBlock logBlock;
    public LogBlockRollback(Plugin plugin) {
        this.logBlock = (LogBlock) plugin;
        UG.getInstance().getLogger().info("LogBlock found! Enabling rollback support");
    }

    @Override
    public void rollbackArena(final Arena arena) {
        final QueryParams params = new QueryParams(logBlock);
        params.setSelection(RegionContainer.fromCorners(arena.getRegion().getWorld(), arena.getRegion().getMinimumLocation(), new Location(arena.getRegion().getWorld(), arena.getRegion().getMaxX(), 256, arena.getRegion().getMaxZ())));
        params.silent = true;
        UG.getInstance().getServer().getScheduler().runTaskAsynchronously(UG.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    logBlock.getCommandsHandler().new CommandRollback(UG.getInstance().getServer().getConsoleSender(), params, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((UArena) arena).setStatus(ArenaStatus.ARENA_STOPPED);
                UG.getInstance().getArenaManager().openArena(arena);
            }
        });
    }
}
