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
package me.ampayne2.ultimategames.games;

import java.util.Map;

public class GameScoreContainer {
    private Map<String, Integer> integerScores;
    private Map<String, Boolean> booleanScores;
    private Map<String, Integer> integerSecondaryScores;
    private Map<String, Boolean> booleanSecondaryScores;

    public GameScoreContainer() {

    }

    public Map<String, Integer> getIntegerScores() {
        return integerScores;
    }

    public Map<String, Boolean> getBooleanScores() {
        return booleanScores;
    }

    public Map<String, Integer> getIntegerSecondaryScores() {
        return integerSecondaryScores;
    }

    public Map<String, Boolean> getBooleanSecondaryScores() {
        return booleanSecondaryScores;
    }

    public void setIntegerScores(Map<String, Integer> scores) {
        integerScores = scores;
    }

    public void setBooleanScores(Map<String, Boolean> scores) {
        booleanScores = scores;
    }

    public void setIntegerSecondaryScores(Map<String, Integer> scores) {
        integerSecondaryScores = scores;
    }

    public void setBooleanSecondaryScores(Map<String, Boolean> scores) {
        booleanSecondaryScores = scores;
    }
}
