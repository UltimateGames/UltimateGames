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
package me.ampayne2.ultimategames.players.classes;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;
import me.ampayne2.ultimategames.utils.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages player classes for games.
 */
public class GameClassManager {
    private final Map<Game, Set<GameClass>> gameClasses = new HashMap<Game, Set<GameClass>>();

    /**
     * Checks if a GameClass is registered.
     *
     * @param game      The game.
     * @param gameClass The GameClass.
     * @return True if the GameClass is registered, else false.
     */
    public boolean isRegistered(Game game, GameClass gameClass) {
        return gameClasses.containsKey(game) && gameClasses.get(game).contains(gameClass);
    }

    /**
     * Gets a GameClass of a game.
     *
     * @param game The game.
     * @param name The name of the GameClass.
     * @return The GameClass, null if none by the name exist.
     */
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

    /**
     * Gets the GameClasses of a game.
     *
     * @param game The game.
     * @return The GameClasses of a game.
     */
    public Set<GameClass> getGameClasses(Game game) {
        return gameClasses.containsKey(game) ? new HashSet<GameClass>(gameClasses.get(game)) : new HashSet<GameClass>();
    }

    /**
     * Registers a GameClass.
     *
     * @param gameClass The GameClass.
     * @return True if the GameClass was registered successfully, else false.
     */
    public GameClassManager registerGameClass(GameClass gameClass) {
        Game game = gameClass.getGame();
        if (gameClasses.containsKey(game)) {
            Set<GameClass> classes = gameClasses.get(game);
            for (GameClass gClass : classes) {
                if (gameClass.equals(gClass)) {
                    return this;
                }
            }
            classes.add(gameClass);
        } else {
            Set<GameClass> classes = new HashSet<GameClass>();
            classes.add(gameClass);
            gameClasses.put(game, classes);
        }
        return this;
    }

    /**
     * Unregisters a GameClass.
     *
     * @param gameClass The GameClass.
     */
    public GameClassManager unregisterGameClass(GameClass gameClass) {
        gameClass.removePlayers();
        Game game = gameClass.getGame();
        if (gameClasses.containsKey(game)) {
            gameClasses.get(game).remove(gameClass);
        }
        return this;
    }

    /**
     * Unregisters all of a game's GameClasses.
     *
     * @param game The Game.
     */
    public GameClassManager unregisterGameClasses(Game game) {
        if (gameClasses.containsKey(game)) {
            for (GameClass gameClass : gameClasses.get(game)) {
                gameClass.removePlayers();
            }
            gameClasses.remove(game);
        }
        return this;
    }

    /**
     * Gets a player's GameClass.
     *
     * @param game       The game of the player.
     * @param playerName The player's name.
     * @return The GameClass, null if the player isn't in one.
     */
    public GameClass getPlayerClass(Game game, String playerName) {
        for (GameClass gameClass : getGameClasses(game)) {
            if (gameClass.hasPlayer(playerName)) {
                return gameClass;
            }
        }
        return null;
    }

    public IconMenu getMenu(UltimateGames ultimateGames, Game game, Player player) {
        Set<GameClass> gameClasses = getGameClasses(game);
        IconMenu menu = new IconMenu(game.getName() + " classes", ((int)Math.ceil(gameClasses.size() / 9.0)) * 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {

            }
        },ultimateGames);
        //TODO: Don't make that haxed badly
        Iterator<GameClass> gameClassIterator = gameClasses.iterator();
        for (int i = 0; i < gameClasses.size(); i++) {
            GameClass gameClass = gameClassIterator.next();
            menu.setOption(i, gameClass.getClassIcon(), gameClass.getName(), gameClass.haveAccess(player) ? ChatColor.GREEN + "Unlocked": ChatColor.DARK_RED + "Locked");
        }
        return menu;
    }
}
