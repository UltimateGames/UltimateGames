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
package me.ampayne2.ultimategames.core.players.classes;

import me.ampayne2.ultimategames.api.events.players.PlayerPostJoinEvent;
import me.ampayne2.ultimategames.api.games.Game;
import me.ampayne2.ultimategames.api.message.UGMessage;
import me.ampayne2.ultimategames.api.players.classes.GameClass;
import me.ampayne2.ultimategames.api.players.classes.GameClassManager;
import me.ampayne2.ultimategames.api.players.classes.TieredClass;
import me.ampayne2.ultimategames.api.utils.IconMenu;
import me.ampayne2.ultimategames.core.UG;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UGameClassManager implements GameClassManager, Listener {
    private final UG ultimateGames;
    private final Map<Game, List<GameClass>> gameClasses = new HashMap<>();
    private final Map<String, Map<Game, IconMenu>> classSelectors = new HashMap<>();

    /**
     * Creates a new GameClassManager.
     *
     * @param ultimateGames The {@link me.ampayne2.ultimategames.core.UG} instance.
     */
    public UGameClassManager(UG ultimateGames) {
        this.ultimateGames = ultimateGames;

        Bukkit.getServer().getPluginManager().registerEvents(this, ultimateGames.getPlugin());
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
        IconMenu menu = new IconMenu(game.getName() + " Classes", ((int) Math.ceil(gameClasses.size() / 9.0)) * 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                GameClass gameClass = gameClasses.get(event.getPosition());
                if (gameClass.hasAccess(event.getPlayer())) {
                    gameClass.addPlayer(event.getPlayer());
                } else {
                    ultimateGames.getMessenger().sendMessage(event.getPlayer(), UGMessage.CLASS_NOACCESS, gameClass.getName());
                }
            }
        }, ultimateGames);
        menu.setSpecificTo(player);
        for (int i = 0; i < gameClasses.size(); i++) {
            GameClass gameClass = gameClasses.get(i);
            String name = gameClass.getName();
            if (gameClass instanceof TieredClass) {
                name = name + " " + ((TieredClass) gameClass).getTier(player);
            }
            menu.setOption(i, gameClass.getClassIcon(), ChatColor.BOLD.toString() + ChatColor.AQUA + name, gameClass.hasAccess(player) ? ChatColor.GREEN + "Unlocked" : ChatColor.DARK_RED + "Locked");
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
