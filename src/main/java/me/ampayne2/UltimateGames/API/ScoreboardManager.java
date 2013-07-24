package me.ampayne2.UltimateGames.API;

import java.util.ArrayList;
import java.util.HashMap;

import me.ampayne2.UltimateGames.Arenas.Arena;

public class ScoreboardManager {
	
	private HashMap<Arena, ArrayList<ArenaScoreboard>> scoreboards = new HashMap<Arena, ArrayList<ArenaScoreboard>>();
	
	public ArrayList<ArenaScoreboard> getArenaScoreboards(Arena arena) {
		if (scoreboards.containsKey(arena)) {
			return scoreboards.get(arena);
		} else {
			return null;
		}
	}
	
	public ArenaScoreboard createArenaScoreboard(Arena arena, String name) {
		ArenaScoreboard scoreboard = new ArenaScoreboard(name);
		if (scoreboards.containsKey(arena)) {
			scoreboards.get(arena).add(scoreboard);
		} else {
			ArrayList<ArenaScoreboard> newScoreboards = new ArrayList<ArenaScoreboard>();
			newScoreboards.add(scoreboard);
			scoreboards.put(arena, newScoreboards);
		}
		return scoreboard;
	}
	
	public void removeArenaScoreboard(Arena arena, String name) {
		if (scoreboards.containsKey(arena)) {
			for (ArenaScoreboard scoreboard : new ArrayList<ArenaScoreboard>(scoreboards.get(arena))) {
				if (name.equals(scoreboard.getName())) {
					scoreboard.reset();
					scoreboards.get(arena).remove(scoreboard);
				}
			}
		}
	}

}
