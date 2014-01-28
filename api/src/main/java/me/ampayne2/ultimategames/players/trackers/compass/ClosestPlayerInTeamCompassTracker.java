/*
 * This file is part of UltimateGames API.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames API.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.players.trackers.compass;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.players.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * A tracker that tracks the closest player from a team with the compass.
 */
public class ClosestPlayerInTeamCompassTracker extends ClosestPlayerCompassTracker {
    private Team team;

    /**
     * Creates a new ClosestPlayerInTeamCompassTracker.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UltimateGames} instance.
     * @param player        The tracking player.
     * @param arena         The tracking player's arena.
     * @param team          The initial target team.
     */
    public ClosestPlayerInTeamCompassTracker(UltimateGames ultimateGames, Player player, Arena arena, Team team) {
        super(ultimateGames, player, arena);
        this.team = team;
    }

    @Override
    public Location getTarget() {
        Set<Player> players = new HashSet<>();
        for (String playerName : team.getPlayers()) {
            players.add(Bukkit.getPlayerExact(playerName));
        }
        super.setTargetPlayers(players);
        return super.getTarget();
    }

    /**
     * Sets the target team of the tracker.
     *
     * @param team The target team.
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Gets the target team of the tracker.
     *
     * @return The target team.
     */
    public Team getTeam() {
        return team;
    }
}
