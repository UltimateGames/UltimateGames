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
package me.ampayne2.ultimategames;

/**
 * Base for all manager classes.
 */
public interface Manager {

    /**
     * Loads the manager. Loads what the manager does, not the class, which is already instantiated.
     * @return If the manager loaded successfully.
     */
    boolean load();

    /**
     * Reloads the manager. Reloads what the manager does, doesn't instantiate a new class.
     * @return If the manager reloaded successfully.
     */
    boolean reload();

    /**
     * Unloads the manager.
     */
    void unload();
    
    /**
     * Checks to see if the manager is loaded.
     * @return If the manager is loaded.
     */
    boolean isLoaded();

}
