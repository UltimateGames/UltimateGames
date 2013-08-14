package me.ampayne2.ultimategames.whitelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class BlockPlaceWhitelist extends Whitelist {
    
    private UltimateGames ultimateGames;
    private Map<Game, List<Material>> blocks;
    
    /**
     * The Block Place Whitelist.
     * @param ultimateGames The UltimateGames instance.
     */
    public BlockPlaceWhitelist(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        reload();
    }
    
    public void reload() {
        blocks = new HashMap<Game, List<Material>>();
        FileConfiguration blockPlaceWhitelistConfig = ultimateGames.getConfigManager().getBlockPlaceWhitelistConfig().getConfig();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            if (blockPlaceWhitelistConfig.contains("Games." + game.getGameDescription().getName())) {
                List<String> materialNames = blockPlaceWhitelistConfig.getStringList("Games." + game.getGameDescription().getName());
                List<Material> materials = new ArrayList<Material>();
                for (String materialName : materialNames) {
                    materials.add(Material.valueOf(materialName));
                }
                blocks.put(game, materials);
            } else {
                FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
                if (gameConfig.contains("BlockPlaceWhitelist")) {
                    List<String> materialNames = gameConfig.getStringList("BlockPlaceWhitelist");
                    List<Material> materials = new ArrayList<Material>();
                    for (String materialName : materialNames) {
                        materials.add(Material.valueOf(materialName));
                    }
                    blocks.put(game, materials);
                    blockPlaceWhitelistConfig.set(game.getGameDescription().getName(), materialNames);
                }
            }
        }
    }
    
    /**
     * Checks if a certain material can be placed in a certain game.
     * @param game The game.
     * @param material The material.
     * @return True if the game has a whitelist and the material is whitelisted, else false.
     */
    public boolean canPlaceMaterial(Game game, Material material) {
        return blocks.containsKey(game) && blocks.get(game).contains(material);
    }

}
