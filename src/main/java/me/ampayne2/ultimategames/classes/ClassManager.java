package me.ampayne2.ultimategames.classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.games.Game;

public class ClassManager implements Manager {
    
    private boolean enabled = false;
    private Map<Game, Set<GameClass>> gameClasses = new HashMap<Game, Set<GameClass>>();

    @Override
    public boolean load() {
        enabled = true;
        return true;
    }

    @Override
    public boolean reload() {
        Iterator<Entry<Game, Set<GameClass>>> it = gameClasses.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Game, Set<GameClass>> entry = it.next();
            for (GameClass gameClass : entry.getValue()) {
                removeGameClass(gameClass);
            }
        }
        return true;
    }

    @Override
    public void unload() {
        Iterator<Entry<Game, Set<GameClass>>> it = gameClasses.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Game, Set<GameClass>> entry = it.next();
            for (GameClass gameClass : entry.getValue()) {
                removeGameClass(gameClass);
            }
        }
        enabled = false;
    }

    @Override
    public boolean isLoaded() {
        return enabled;
    }
    
    /**
     * Checks if a game has a certain GameClass.
     * @param game The game.
     * @param gameClass The GameClass.
     * @return True if the game has the GameClass, else false.
     */
    public boolean hasGameClass(Game game, GameClass gameClass) {
        return enabled && gameClasses.containsKey(game) && gameClasses.get(game).contains(gameClass);
    }
    
    /**
     * Gets the classes of a game.
     * @param game The game.
     * @return The classes of a game.
     */
    public Set<GameClass> getGameClasses(Game game) {
        return enabled && gameClasses.containsKey(game) ? gameClasses.get(game) : new HashSet<GameClass>();
    }
    
    /**
     * Adds a GameClass to the list of game classes.
     * @param game The game.
     * @param gameClass The GameClass.
     * @return True if the GameClass was added successfully, else false.
     */
    public boolean addGameClass(GameClass gameClass) {
        if (enabled) {
            Game game = gameClass.getGame();
            Set<GameClass> classes = gameClasses.containsKey(game) ? gameClasses.get(game) : new HashSet<GameClass>();
            if (classes.add(gameClass)) {
                gameClasses.put(game, classes);
                return true;
            }
            
        }
        return false;
    }
    
    /**
     * Removes a GameClass from the list of game classes.
     * @param game The game.
     * @param gameClass The GameClass.
     */
    public void removeGameClass(GameClass gameClass) {
        if (enabled) {
            gameClass.removePlayersFromClass();
            Game game = gameClass.getGame();
            if (gameClasses.containsKey(game)) {
                gameClasses.get(game).remove(gameClass);
            }
        }
    }

}
