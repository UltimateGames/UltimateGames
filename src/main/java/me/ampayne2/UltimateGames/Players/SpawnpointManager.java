package me.ampayne2.UltimateGames.Players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;

public class SpawnpointManager {

	private UltimateGames ultimateGames;
	private HashMap<Arena, ArrayList<SpawnPoint>> spawnPoints = new HashMap<Arena, ArrayList<SpawnPoint>>();
	
	public SpawnpointManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	/**
	 * Adds a spawnpoint to the manager.
	 * 
	 * @param spawnPoint The spawnpoint.
	 */
	public void addSpawnPoint(SpawnPoint spawnPoint) {
		if (spawnPoints.containsKey(spawnPoint.getArena())) {
			spawnPoints.get(spawnPoint.getArena()).add(spawnPoint);
		} else {
			ArrayList<SpawnPoint> spawn = new ArrayList<SpawnPoint>();
			spawn.add(spawnPoint);
			spawnPoints.put(spawnPoint.getArena(), spawn);
		}
	}
	
	/**
	 * Creates a spawnpoint.
	 * The spawnpoint is not added to the manager.
	 * 
	 * @param arena The Arena.
	 * @param location The location.
	 * @param locked If the spawnpoint prevents the player from moving off of it.
	 * @return The spawnpoint created.
	 */
	public SpawnPoint createSpawnPoint(Arena arena, Location location, Boolean locked) {
		return new SpawnPoint(arena, location, locked);
	}
	
	/**
	 * Get a specific spawnpoint of an arena.
	 * 
	 * @param arena The arena.
	 * @param index The spawnpoint index.
	 * @return The spawnpoint.
	 */
	public SpawnPoint getSpawnPoint(Arena arena, Integer index) {
		if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() >= index) {
			return spawnPoints.get(arena).get(index);
		}
		return null;
	}
	
	/**
	 * Get a random spawnpoint of an arena.
	 * 
	 * @param arena The arena.
	 * @return The spawnpoint.
	 */
	public SpawnPoint getRandomSpawnPoint(Arena arena) {
		if (spawnPoints.containsKey(arena)) {
			Random generator = new Random();
			Integer index = generator.nextInt(spawnPoints.get(arena).size());
			return spawnPoints.get(arena).get(index);
		}
		return null;
	}
	
	/**
	 * Get a random spawnpoint of an arena at or above the minIndex.
	 * Useful if there are certain spawnpoints only used for specific things.
	 * 
	 * @param arena The arena.
	 * @param minIndex The minimum index.
	 * @return The spawnpoint.
	 */
	public SpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex) {
		if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() > minIndex) {
			Random generator = new Random();
			Integer index = generator.nextInt(spawnPoints.get(arena).size() - minIndex) + minIndex;
			return spawnPoints.get(arena).get(index);
		}
		return null;
	}
	
	/**
	 * Get a random spawnpoint of an arena within the minIndex and maxIndex.
	 * Useful if there are certain spawnpoints only used for specific things.
	 * 
	 * @param arena The arena.
	 * @param minIndex The minimum index.
	 * @param maxIndex The maximum index.
	 * @return The spawnpoint.
	 */
	public SpawnPoint getRandomSpawnPoint(Arena arena, Integer minIndex, Integer maxIndex) {
		if (spawnPoints.containsKey(arena) && minIndex < maxIndex && spawnPoints.get(arena).size() > maxIndex) {
			Random generator = new Random();
			Integer index = generator.nextInt(maxIndex - minIndex + 1) + minIndex;
			return spawnPoints.get(arena).get(index);
		}
		return null;
	}
	
	/**
	 * Get a certain amount of spawnpoints distributed evenly among the available spawnpoints.
	 * Example: You have 8 spawn points. You ask for 4. It gives you the spawnpoints at indexes 0, 2, 4, and 6.
	 * Example 2: You have 16 spawn points. You ask for 2. It gives you the spawnpoints at indexes 0 and 8.
	 * Useful for spawnpoint setups similar to survival games where players need to be spaced out.
	 * 
	 * @param arena The arena.
	 * @param amount The amount of spawnpoints to get.
	 * @return The spawnpoints.
	 */
	public ArrayList<SpawnPoint> getDistributedSpawnPoints(Arena arena, Integer amount) {
		if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() >= amount) {
			ArrayList<SpawnPoint> distributedSpawnPoints = new ArrayList<SpawnPoint>();
			Integer size = spawnPoints.get(arena).size();
			Double multiple = (double)size / (double)amount;
			for(int i=0; i < amount; i++) {
				Integer index = (int) Math.round(i*multiple);
				distributedSpawnPoints.add(spawnPoints.get(arena).get(index));
			}
			return distributedSpawnPoints;
		}
		return null;
	}
	
	/**
	 * Gets all the spawnpoints of an arena.
	 * 
	 * @param arena The arena.
	 * @return The spawnpoints.
	 */
	public ArrayList<SpawnPoint> getSpawnPointsOfArena(Arena arena) {
		if (spawnPoints.containsKey(arena)) {
			return spawnPoints.get(arena);
		} else {
			return null;
		}
	}
	
	/**
	 * Removes the spawnpoint at a certain index.
	 * 
	 * @param arena The arena.
	 * @param index The index.
	 */
	public void removeSpawnPoint(Arena arena, Integer index) {
		if (spawnPoints.containsKey(arena) && spawnPoints.get(arena).size() >= index) {
			spawnPoints.get(arena).remove(index);
		}
	}
	
	/**
	 * Removes the spawnpoints at the indexes.
	 * 
	 * @param arena The arena.
	 * @param indexes The indexes.
	 */
	public void removeSpawnPoints(Arena arena, Integer... indexes) {
		if (spawnPoints.containsKey(arena)) {
			ArrayList<SpawnPoint> remove = new ArrayList<SpawnPoint>();
			for (Integer index : indexes) {
				if (spawnPoints.get(arena).size() >= index) {
					remove.add(spawnPoints.get(arena).get(index));	
				}
			}
			if (remove != null) {
				spawnPoints.get(arena).removeAll(remove);
			}
		}
	}
	
	/**
	 * Removes all the spawnpoints of an arena.
	 * 
	 * @param arena The arena.
	 */
	public void removeAllSpawnPoints(Arena arena) {
		if (spawnPoints.containsKey(arena)) {
			spawnPoints.remove(arena);
		}
	}
	
}
