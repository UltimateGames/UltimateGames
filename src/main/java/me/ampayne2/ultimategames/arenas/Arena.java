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
package me.ampayne2.ultimategames.arenas;

import java.util.ArrayList;
import java.util.List;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.enums.ArenaStatus;
import me.ampayne2.ultimategames.enums.PlayerType;
import me.ampayne2.ultimategames.games.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Arena implements Listener {
    
    private UltimateGames ultimateGames;
    private Game game;
    private String arenaName;
    private ArenaStatus arenaStatus;
    private List<String> players = new ArrayList<String>();
    private Integer minPlayers;
    private Integer maxPlayers;
    private Boolean storeInventory;
    private Boolean storeArmor;
    private Boolean storeExp;
    private Boolean storeEffects;
    private Boolean storeGamemode;
    private Boolean resetAfterMatch;
    private Boolean allowExplosionDamage;
    private Boolean allowExplosionBlockBreaking;
    private World arenaWorld;
    private Double minX;
    private Double maxX;
    private Double minZ;
    private Double maxZ;
    private Integer timesPlayed;

    public Arena(UltimateGames ultimateGames, Game game, String arenaName, Location corner1, Location corner2) {
        this.ultimateGames = ultimateGames;
        this.arenaName = arenaName;
        this.game = game;
        arenaStatus = ArenaStatus.OPEN;
        FileConfiguration gamesConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        String arenaPath = "Arenas." + game.getGameDescription().getName() + "." + arenaName;
        //Get all arena information. Tries to get from arena config, if doesn't exist there then gets from default game settings, if doesn't exist there then is set specifically to true/false
        storeInventory = arenaConfig.getBoolean(arenaPath + ".Players.Store-Inventory", gamesConfig.getBoolean("DefaultSettings.Store-Inventory", true));
        storeArmor = arenaConfig.getBoolean(arenaPath + ".Players.Store-Armor", gamesConfig.getBoolean("DefaultSettings.Store-Armor", true));
        storeExp = arenaConfig.getBoolean(arenaPath + ".Players.Store-Exp", gamesConfig.getBoolean("DefaultSettings.Store-Exp", true));
        storeEffects = arenaConfig.getBoolean(arenaPath + ".Players.Store-Effects", gamesConfig.getBoolean("DefaultSettings.Store-Effects", true));
        storeGamemode = arenaConfig.getBoolean(arenaPath + ".Players.Store-Gamemode", gamesConfig.getBoolean("DefaultSettings.Store-Gamemode", true));
        resetAfterMatch = arenaConfig.getBoolean(arenaPath + ".Reset-After-Match", gamesConfig.getBoolean("DefaultSettings.Reset-After-Match", true));
        allowExplosionDamage = arenaConfig.getBoolean(arenaPath + ".Allow-Explosion-Damage", gamesConfig.getBoolean("DefaultSettings.Allow-Explosion-Damage", false));
        allowExplosionBlockBreaking = arenaConfig.getBoolean(arenaPath + ".Allow-Explosion-Block-Breaking", gamesConfig.getBoolean("DefaultSettings.Allow-Explosion-Block-Breaking", false));
        minPlayers = arenaConfig.getInt(arenaPath + ".Min-Players", gamesConfig.getInt("DefaultSettings.MinPlayers", 8));
        if (game.getGameDescription().getPlayerType() == PlayerType.SINGLE_PLAYER) {
            maxPlayers = 1;
        } else if (game.getGameDescription().getPlayerType() == PlayerType.TWO_PLAYER) {
            maxPlayers = 2;
        } else if (game.getGameDescription().getPlayerType() == PlayerType.CONFIGUREABLE) {
            maxPlayers = arenaConfig.getInt(arenaPath + ".Max-Players", gamesConfig.getInt("DefaultSettings.MaxPlayers", 8));
        }
        //takes the 2 corners and turns them into minLocation and maxLocation
        arenaWorld = corner1.getWorld();
        minX = corner1.getX() <= corner2.getX() ? corner1.getX() : corner2.getX();
        maxX = corner2.getX() <= corner1.getX() ? corner1.getX() : corner2.getX();
        minZ = corner1.getZ() <= corner2.getZ() ? corner1.getZ() : corner2.getZ();
        maxZ = corner2.getZ() <= corner1.getZ() ? corner1.getZ() : corner2.getZ();

        //create the arena in the config if it doesn't exist
        if (arenaConfig.getConfigurationSection(arenaPath) == null) {
            arenaConfig.set(arenaPath + ".Status", "ARENA_STOPPED");
            arenaConfig.set(arenaPath + ".Max-Players", maxPlayers);
            arenaConfig.set(arenaPath + ".Min-Players", minPlayers);
            arenaConfig.set(arenaPath + ".Players.Store-Inventory", storeInventory);
            arenaConfig.set(arenaPath + ".Players.Store-Armor", storeArmor);
            arenaConfig.set(arenaPath + ".Players.Store-Exp", storeExp);
            arenaConfig.set(arenaPath + ".Players.Store-Effects", storeEffects);
            arenaConfig.set(arenaPath + ".Players.Store-Gamemode", storeGamemode);
            arenaConfig.set(arenaPath + ".Reset-After-Match", resetAfterMatch);
            arenaConfig.set(arenaPath + ".Allow-Explosion-Damage", allowExplosionDamage);
            arenaConfig.set(arenaPath + ".Allow-Explosion-Block-Breaking", allowExplosionBlockBreaking);
            arenaConfig.set(arenaPath + ".Arena-Location.world", arenaWorld.getName());
            arenaConfig.set(arenaPath + ".Arena-Location.minx", minX);
            arenaConfig.set(arenaPath + ".Arena-Location.maxx", maxX);
            arenaConfig.set(arenaPath + ".Arena-Location.minz", minZ);
            arenaConfig.set(arenaPath + ".Arena-Location.maxz", maxZ);
        }
        ultimateGames.getConfigManager().getGameConfig(game).saveConfig();
        ultimateGames.getConfigManager().getArenaConfig().saveConfig();
        timesPlayed = 0;
    }

    /**
     * Gets the name of an arena.
     * @return The name.
     */
    public String getName() {
        return arenaName;
    }

    /**
     * Gets the game of an arena.
     * @return The game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Adds a player to the arena's player list.
     * @param playerName The player's name.
     * @return If it was successful.
     */
    public boolean addPlayer(String playerName) {
        if (game.getGameDescription().getPlayerType() != PlayerType.INFINITE && players.size() >= maxPlayers) {
            return false;
        } else if (players.contains(playerName)) {
            return false;
        } else {
            players.add(playerName);
            return true;
        }
    }

    /**
     * Removes a player from the arena's player list.
     * @param playerName The player's name.
     */
    public void removePlayer(String playerName) {
        if (players.contains(playerName)) {
            players.remove(playerName);
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
        }
    }

    /**
     * Removes all of the arena's players.
     */
    public void removePlayers() {
        if (!players.isEmpty()) {
            players.clear();
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
        }
    }

    /**
     * Checks to see if the arena has a certain player.
     * @param playerName The player's name.
     * @return If the arena has the player.
     */
    public boolean hasPlayer(String playerName) {
        return !players.isEmpty() && players.contains(playerName);
    }

    /**
     * Gets the players in the arena.
     * @return The players.
     */
    public List<String> getPlayers() {
        return new ArrayList<String>(players);
    }

    /**
     * Gets the min players of the arena.
     * @return The minimum amount of players.
     */
    public Integer getMinPlayers() {
        return minPlayers;
    }

    /**
     * Gets the max players of the arena.
     * @return The maximum amount of players.
     */
    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Gets the status of the arena.
     * @return The status.
     */
    public ArenaStatus getStatus() {
        return arenaStatus;
    }
    
    /**
     * Gets the storeInventory setting.
     * @return True if the game stores your inventory else false.
     */
    public Boolean storeInventory() {
        return storeInventory;
    }
    
    /**
     * Gets the storeArmor setting.
     * @return True if the game stores your armor else false.
     */
    public Boolean storeArmor() {
        return storeArmor;
    }
    
    /**
     * Gets the storeExp setting.
     * @return True if the game stores your exp else false.
     */
    public Boolean storeExp() {
        return storeExp;
    }
    
    /**
     * Gets the storeEffects setting.
     * @return True if the game stores your effects else false.
     */
    public Boolean storeEffects() {
        return storeEffects;
    }
    
    /**
     * Gets the storeGamemode setting.
     * @return True if the game stores your gamemode else false.
     */
    public Boolean storeGamemode() {
        return storeGamemode;
    }
    
    /**
     * Gets the resetAfterMatch setting.
     * @return True if the game resets after each match else false.
     */
    public Boolean resetAfterMatch() {
        return resetAfterMatch;
    }
    
    /**
     * Gets the allowExplosionDamage setting.
     * @return True if the game allows explosion damage else false.
     */
    public Boolean allowExplosionDamage() {
        return allowExplosionDamage;
    }
    
    /**
     * Gets the allowExplosionBlockBreaking setting.
     * @return True if the game allows explosion block breaking else false.
     */
    public Boolean allowExplosionBlockBreaking() {
        return allowExplosionBlockBreaking;
    }
    
    /**
     * Sets the storeInventory setting.
     * @param storeInventory Whether or not the game should store a player's inventory.
     */
    public void storeInventory(Boolean storeInventory) {
        this.storeInventory = storeInventory;
    }
    
    /**
     * Sets the storeArmor setting.
     * @param storeArmor Whether or not the game should store a player's armor.
     */
    public void storeArmor(Boolean storeArmor) {
        this.storeArmor = storeArmor;
    }
    
    /**
     * Sets the storeExp setting.
     * @param storeExp Whether or not the game should store a player's exp.
     */
    public void storeExp(Boolean storeExp) {
        this.storeExp = storeExp;
    }
    
    /**
     * Sets the storeEffects setting.
     * @param storeEffects Whether or not the game should store a player's effects.
     */
    public void storeEffects(Boolean storeEffects) {
        this.storeEffects = storeEffects;
    }
    
    /**
     * Sets the storeGamemode setting.
     * @param storeGamemode Whether or not the game should store a player's gamemode.
     */
    public void storeGamemode(Boolean storeGamemode) {
        this.storeGamemode = storeGamemode;
    }
    
    /**
     * Sets the resetAfterMatch setting.
     * @param resetAfterMatch Whether or not the game should reset after each match.
     */
    public void resetAfterMatch(Boolean resetAfterMatch) {
        this.resetAfterMatch = resetAfterMatch;
    }
    
    /**
     * Sets the allowExplosionDamage setting.
     * @param allowExplosionDamage Whether or not the game should allow explosion damage.
     */
    public void allowExplosionDamage(Boolean allowExplosionDamage) {
        this.allowExplosionDamage = allowExplosionDamage;
    }
    
    /**
     * Sets the allowExplosionBlockBreaking setting.
     * @param allowExplosionBlockBreaking Whether or not the game should allow explosion block breaking.
     */
    public void allowExplosionBlockBreaking(Boolean allowExplosionBlockBreaking) {
        this.allowExplosionBlockBreaking = allowExplosionBlockBreaking;
    }

    /**
     * Gets the world of the arena.
     * @return The world.
     */
    public World getWorld() {
        return arenaWorld;
    }
    
    /**
     * Gets the amount of times played.
     * @return The amount of times played.
     */
    public Integer getTimesPlayed() {
        return timesPlayed;
    }

    /**
     * Sets the status of the arena.
     * @param status The status.
     */
    public void setStatus(ArenaStatus status) {
        arenaStatus = status;
        FileConfiguration arenaConfig = ultimateGames.getConfigManager().getArenaConfig().getConfig();
        arenaConfig.set("Arenas." + game.getGameDescription().getName() + "." + arenaName + ".Status", status.toString());
        ultimateGames.getConfigManager().getArenaConfig().saveConfig();
        if (ultimateGames.getUGSignManager() != null) {
            ultimateGames.getUGSignManager().updateLobbySignsOfArena(this);
        }
        if (status == ArenaStatus.RUNNING) {
            timesPlayed += 1;
        }
    }

    /**
     * Checks to see if a location is inside the arena.
     * @param location The location.
     * @return If the location is inside the arena or not.
     */
    public Boolean locationIsInArena(Location location) {
        return location.getWorld().equals(arenaWorld) && location.getX() >= minX && location.getX() <= maxX && location.getZ() >= minZ && location.getZ() <= maxZ;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (players.contains(event.getPlayer().getName()) && !locationIsInArena(event.getTo())) {
            event.getPlayer().getVelocity().multiply(-1);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (players.contains(event.getPlayer().getName()) && !locationIsInArena(event.getTo())) {
            event.setCancelled(true);
        }
    }
}
