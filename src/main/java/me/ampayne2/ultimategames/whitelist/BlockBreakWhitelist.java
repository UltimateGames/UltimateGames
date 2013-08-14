package me.ampayne2.ultimategames.whitelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.games.Game;

public class BlockBreakWhitelist extends Whitelist {
    
    private UltimateGames ultimateGames;
    private Map<Game, List<Material>> blocks;
    
    /**
     * The Block Break Whitelist.
     * @param ultimateGames The UltimateGames instance.
     */
    public BlockBreakWhitelist(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        reload();
    }
    
    public void reload() {
        blocks = new HashMap<Game, List<Material>>();
        FileConfiguration blockBreakWhitelistConfig = ultimateGames.getConfigManager().getBlockBreakWhitelistConfig().getConfig();
        for (Game game : ultimateGames.getGameManager().getGames()) {
            if (blockBreakWhitelistConfig.contains(game.getGameDescription().getName())) {
                List<String> materialNames = blockBreakWhitelistConfig.getStringList(game.getGameDescription().getName());
                List<Material> materials = new ArrayList<Material>();
                for (String materialName : materialNames) {
                    materials.add(Material.valueOf(materialName));
                }
                blocks.put(game, materials);
            } else {
                FileConfiguration gameConfig = ultimateGames.getConfigManager().getGameConfig(game).getConfig();
                if (gameConfig.contains("BlockBreakWhitelist")) {
                    List<String> materialNames = gameConfig.getStringList("BlockBreakWhitelist");
                    List<Material> materials = new ArrayList<Material>();
                    for (String materialName : materialNames) {
                        materials.add(Material.valueOf(materialName));
                    }
                    blocks.put(game, materials);
                    blockBreakWhitelistConfig.set(game.getGameDescription().getName(), materialNames);
                }
            }
        }
    }
    
    /**
     * Checks if a certain material can be broken in a certain game.
     * @param game The game.
     * @param material The material.
     * @return True if the game has a whitelist and the material is whitelisted, else false.
     */
    public boolean canBreakMaterial(Game game, Material material) {
        return blocks.containsKey(game) && blocks.get(game).contains(material);
    }

}
