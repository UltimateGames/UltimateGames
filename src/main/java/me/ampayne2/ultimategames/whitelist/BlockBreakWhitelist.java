package me.ampayne2.ultimategames.whitelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
    
    /**
     * Checks if a certain block can be broken in a certain game.
     * @param game The game.
     * @param block The block.
     * @return True if the game has a whitelist and the block's material is whitelisted, else false.
     */
    public boolean canBreakMaterial(Game game, Block block) {
        return blocks.containsKey(game) && blocks.get(game).contains(block.getType());
    }
    
    /**
     * Gets the materials that can be broken in a certain game out of a list of materials.
     * @param game The game.
     * @param materials The materials.
     * @return The materials that can be broken.
     */
    public List<Material> materialsWhitelisted(Game game, List<Material> materials) {
        List<Material> whitelistedMaterials = new ArrayList<Material>();
        if (blocks.containsKey(game)) {
            List<Material> whitelistedTypes = blocks.get(game);
            for (Material material : materials) {
                if (whitelistedTypes.contains(material)) {
                    whitelistedMaterials.add(material);
                }
            }
        }
        return whitelistedMaterials;
    }
    
    /**
     * Gets the blocks that can be broken in a certain game out of a list of blocks.
     * @param game The game.
     * @param materials The materials.
     * @return The blocks that can be broken.
     */
    public List<Block> blocksWhitelisted(Game game, List<Block> materials) {
        List<Block> whitelistedBlocks = new ArrayList<Block>();
        if (blocks.containsKey(game)) {
            List<Material> whitelistedTypes = blocks.get(game);
            for (Block block : materials) {
                if (whitelistedTypes.contains(block.getType())) {
                    whitelistedBlocks.add(block);
                }
            }
        }
        return whitelistedBlocks;
    }

}
