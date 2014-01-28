/*
 * This file is part of UltimateGames Core.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
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
package me.ampayne2.ultimategames.misc;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.arenas.ArenaStatus;
import me.ampayne2.ultimategames.games.Game;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;

import java.io.IOException;

/**
 * Manages the ultimate games metrics and custom graphs.
 */
public class MetricsManager {
    private final UG ultimateGames;
    private Metrics metrics;
    private Graph gamesLoadedGraph;
    private Graph totalArenasPlayedGraph;
    private Graph arenasBeingPlayedGraph;
    private Graph totalPlayersInArenasGraph;
    private Graph playersInArenasGraph;

    /**
     * Creates a new MetricManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public MetricsManager(final UG ultimateGames) {
        this.ultimateGames = ultimateGames;
        try {
            metrics = new Metrics(ultimateGames);

            gamesLoadedGraph = metrics.createGraph("Games Loaded");
            totalArenasPlayedGraph = metrics.createGraph("Total Arenas Played");
            arenasBeingPlayedGraph = metrics.createGraph("Arenas Currently Being Played");
            playersInArenasGraph = metrics.createGraph("Players Currently In Arenas");

            metrics.start();
        } catch (IOException e) {
            ultimateGames.getMessenger().debug(e);
        }
    }

    public void addGame(final Game game) {
        gamesLoadedGraph.addPlotter(new Metrics.Plotter(game.getName()) {
            @Override
            public int getValue() {
                return 1;
            }
        });
    }

    public void removeGame(final Game game) {
        gamesLoadedGraph.removePlotter(new Metrics.Plotter(game.getName()) {
            @Override
            public int getValue() {
                return 1;
            }
        });
    }

    public void addTotalPlayersGraph() {
        totalPlayersInArenasGraph = metrics.createGraph("Total Players In Arenas");
        totalPlayersInArenasGraph.addPlotter(new Metrics.Plotter("Total Players In Arenas") {
            @Override
            public int getValue() {
                Integer playersInArenas = 0;
                for (Arena arena : ultimateGames.getArenaManager().getArenas()) {
                    playersInArenas += arena.getPlayers().size();
                }
                return playersInArenas;
            }
        });
    }

    public void removeTotalPlayersGraph() {
        totalPlayersInArenasGraph.removePlotter(new Metrics.Plotter("Total Players In Arenas") {
            @Override
            public int getValue() {
                Integer playersInArenas = 0;
                for (Arena arena : ultimateGames.getArenaManager().getArenas()) {
                    playersInArenas += arena.getPlayers().size();
                }
                return playersInArenas;
            }
        });
    }

    public void addArena(final Arena arena) {
        totalArenasPlayedGraph.addPlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getTimesPlayed();
            }
        });
        arenasBeingPlayedGraph.addPlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        playersInArenasGraph.addPlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getPlayers().size();
            }
        });
    }

    public void removeArena(final Arena arena) {
        totalArenasPlayedGraph.removePlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getTimesPlayed();
            }
        });
        arenasBeingPlayedGraph.removePlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                if (arena.getStatus() == ArenaStatus.RUNNING) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        playersInArenasGraph.removePlotter(new Metrics.Plotter(arena.getGame().getName() + " : " + arena.getName()) {
            @Override
            public int getValue() {
                return arena.getPlayers().size();
            }
        });
    }
}
