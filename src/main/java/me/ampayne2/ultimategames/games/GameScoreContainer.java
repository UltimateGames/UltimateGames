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

import java.util.HashMap;

public class GameScoreContainer {
	private HashMap<String, Integer> integerScores;
	private HashMap<String, Boolean> booleanScores;
	private HashMap<String, Integer> integerSecondaryScores;
	private HashMap<String, Boolean> booleanSecondaryScores;

	public GameScoreContainer() {

	}

	public HashMap<String, Integer> getIntegerScores() {
		return integerScores;
	}

	public HashMap<String, Boolean> getBooleanScores() {
		return booleanScores;
	}

	public HashMap<String, Integer> getIntegerSecondaryScores() {
		return integerSecondaryScores;
	}

	public HashMap<String, Boolean> getBooleanSecondaryScores() {
		return booleanSecondaryScores;
	}

	public void setIntegerScores(HashMap<String, Integer> scores) {
		integerScores = scores;
	}

	public void setBooleanScores(HashMap<String, Boolean> scores) {
		booleanScores = scores;
	}

	public void setIntegerSecondaryScores(HashMap<String, Integer> scores) {
		integerSecondaryScores = scores;
	}

	public void setBooleanSecondaryScores(HashMap<String, Boolean> scores) {
		booleanSecondaryScores = scores;
	}
}
