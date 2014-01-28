/*
 * This file is part of UltimateGames ENGINE.
 *
 * Copyright (c) 2013-2013, UltimateGames <http://github.com/ampayne2/>
 *
 * UltimateGames ENGINE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UltimateGames ENGINE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with UltimateGames ENGINE.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.ultimategames.players.classes;

import me.ampayne2.ultimategames.UG;
import me.ampayne2.ultimategames.events.players.PlayerPostJoinEvent;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.utils.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UGameClassManager implements GameClassManager {
    private final UG ultimateGames;
    private final Map<Game, List<GameClass>> gameClasses = new HashMap<>();
    private final Map<String, Map<Game, IconMenu>> classSelectors = new HashMap<>();

    /**
     * Creates a new GameClassManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.UG} instance.
     */
    public UGameClassManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;
    }

    @Override
    public boolean isRegistered(Game game, GameClass gameClass) {
        return gameClasses.containsKey(game) && gameClasses.get(game).contains(gameClass);
    }

    @Override
    public GameClass getGameClass(Game game, String name) {
        if (gameClasses.containsKey(game)) {
            for (GameClass gameClass : gameClasses.get(game)) {
                if (gameClass.getName().equals(name)) {
                    return gameClass;
                }
            }
        }
        return null;
    }

    @Override
    public List<GameClass> getGameClasses(Game game) {
        return gameClasses.containsKey(game) ? new ArrayList<>(gameClasses.get(game)) : new ArrayList<GameClass>();
    }

    @Override
    public GameClassManager registerGameClass(GameClass gameClass) {
        Game game = gameClass.getGame();
        if (gameClasses.containsKey(game)) {
            List<GameClass> classes = gameClasses.get(game);
            if (classes.contains(gameClass)) {
                return this;
            }
            classes.add(gameClass);
        } else {
            List<GameClass> classes = new ArrayList<>();
            classes.add(gameClass);
            gameClasses.put(game, classes);
        }
        return this;
    }

    @Override
    public GameClassManager unregisterGameClass(GameClass gameClass) {
        gameClass.removePlayers();
        Game game = gameClass.getGame();
        if (gameClasses.containsKey(game)) {
            gameClasses.get(game).remove(gameClass);
        }
        return this;
    }

    @Override
    public GameClassManager unregisterGameClasses(Game game) {
        if (gameClasses.containsKey(game)) {
            for (GameClass gameClass : gameClasses.get(game)) {
                gameClass.removePlayers();
            }
            gameClasses.remove(game);
        }
        return this;
    }

    @Override
    public GameClass getPlayerClass(Game game, String playerName) {
        for (GameClass gameClass : getGameClasses(game)) {
            if (gameClass.hasPlayer(playerName)) {
                return gameClass;
            }
        }
        return null;
    }

    @Override
    public IconMenu getClassSelector(Game game, final Player player) {
        String playerName = player.getName();
        if (classSelectors.containsKey(playerName) && classSelectors.get(playerName).containsKey(game)) {
            return classSelectors.get(playerName).get(game);
        }

        final List<GameClass> gameClasses = getGameClasses(game);
        IconMenu menu = new IconMenu(playerName + "'s " + " Classes", ((int) Math.ceil(gameClasses.size() / 9.0)) * 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                GameClass gameClass = gameClasses.get(event.getPosition());
                if (gameClass.hasAccess(event.getPlayer())) {
                    gameClass.addPlayer(event.getPlayer());
                } else {
                    ultimateGames.getMessenger().sendMessage(event.getPlayer(), "classes.noaccess", gameClass.getName());
                }
            }
        }, ultimateGames);
        for (int i = 0; i < gameClasses.size(); i++) {
            GameClass gameClass = gameClasses.get(i);
            String name = gameClass.getName();
            if (gameClass instanceof TieredClass) {
                name = name + " " + ((TieredClass) gameClass).getTier(player);
            }
            menu.setOption(i, gameClass.getClassIcon(), name, gameClass.hasAccess(player) ? ChatColor.GREEN + "Unlocked" : ChatColor.DARK_RED + "Locked");
        }
        return menu;
    }

    @EventHandler
    public void onPlayerJoin(PlayerPostJoinEvent event) {
        Game game = event.getArena().getGame();
        if (getGameClasses(game).size() > 0) {
            String playerName = event.getPlayer().getName();
            if (classSelectors.containsKey(playerName)) {
                if (classSelectors.get(playerName).containsKey(game)) {
                    classSelectors.get(playerName).get(game).destroy();
                }
                classSelectors.get(playerName).put(game, getClassSelector(game, event.getPlayer()));
            } else {
                Map<Game, IconMenu> playerGameClassSelectors = new HashMap<>();
                playerGameClassSelectors.put(game, getClassSelector(game, event.getPlayer()));
                classSelectors.put(playerName, playerGameClassSelectors);
            }
        }
    }
}
