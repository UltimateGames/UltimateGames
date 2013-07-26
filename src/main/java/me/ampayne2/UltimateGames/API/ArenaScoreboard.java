package me.ampayne2.UltimateGames.API;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ArenaScoreboard {
	
	private Scoreboard scoreboard;
	private String name;
	
	public ArenaScoreboard(String name) {
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective(name, "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.scoreboard = scoreboard;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addPlayer(String playerName) {
		Bukkit.getPlayer(playerName).setScoreboard(scoreboard);
	}
	
	public void addPlayers(ArrayList<String> playerNames) {
		for (String playerName : playerNames) {
			Bukkit.getPlayer(playerName).setScoreboard(scoreboard);
		}
	}
	
	public void removePlayer(String playerName) {
		Bukkit.getPlayer(playerName).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	public void removePlayers(ArrayList<String> playerNames) {
		for (String playerName : playerNames) {
			Bukkit.getPlayer(playerName).setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
	
	public void reset() {
		scoreboard.getObjective(name).unregister();
		Objective objective = scoreboard.registerNewObjective(name, "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public void setVisible(Boolean visible) {
		if (visible) {
			scoreboard.getObjective(name).setDisplaySlot(DisplaySlot.SIDEBAR);
		} else {
			scoreboard.clearSlot(DisplaySlot.SIDEBAR);
		}
	}
	
	public Integer getScore(String name) {
		return scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name)).getScore();
	}
	
	public void setScore(String name, Integer score) {
		scoreboard.getObjective(this.name).getScore(Bukkit.getOfflinePlayer(name)).setScore(score);
	}
	
	public void resetScore(String name) {
		scoreboard.resetScores(Bukkit.getOfflinePlayer(name));
	}

}
