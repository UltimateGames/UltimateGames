package me.ampayne2.UltimateGames.Players;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;

import me.ampayne2.UltimateGames.UltimateGames;
import me.ampayne2.UltimateGames.Arenas.Arena;
import me.ampayne2.UltimateGames.Utils.SpawnPoint;

public class SpawnpointManager {

	private UltimateGames ultimateGames;
	private HashMap<Arena, ArrayList<SpawnPoint>> spawnPoints;
	
	public SpawnpointManager(UltimateGames ultimateGames) {
		this.ultimateGames = ultimateGames;
	}
	
	/**
	 * Adds a spawnpoint to the manager.
	 * 
	 * @param arena Arena to add the spawnpoint to.
	 * @param location Location of the spawnpoint.
	 */
	public void addSpawnPoint(SpawnPoint spawnPoint) {
		
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
		return null;
	}
	
	/**
	 * Get a random spawnpoint of an arena.
	 * 
	 * @param arena The arena.
	 * @return The spawnpoint.
	 */
	public SpawnPoint getRandomSpawnPoint(Arena arena) {
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
		return null;
	}
	
	/**
	 * Get a certain amount of spawnpoints distributed evenly among the available spawnpoints.
	 * Example: You have 8 spawn points. You ask for 4. It gives you the spawnpoints at indexes 1, 3, 5, and 7.
	 * Example 2: You have 16 spawn points. You ask for 2. It gives you the spawnpoints at indexes 1 and 9.
	 * Useful for spawnpoint setups similar to survival games where players need to be spaced out.
	 * 
	 * @param arena The arena.
	 * @param amount The amount of spawnpoints to get.
	 * @return The spawnpoints.
	 */
	public SpawnPoint[] getDistributedSpawnPoints(Arena arena, Integer amount) {
		return null;
	}
	
	/**
	 * Removes the spawnpoint at a certain index.
	 * 
	 * @param arena The arena.
	 * @param index The index.
	 */
	public void removeSpawnPoint(Arena arena, Integer index) {
		
	}
	
	/**
	 * Removes the spawnpoints at the indexes.
	 * 
	 * @param arena The arena.
	 * @param index The indexes.
	 */
	public void removeSpawnPoints(Arena arena, Integer... index) {
		
	}
	
	/**
	 * Removes all the spawnpoints of an arena.
	 * 
	 * @param arena The arena.
	 */
	public void removeAllSpawnPoints(Arena arena) {
		
	}
	
}
