package me.ampayne2.ultimategames.whitelist;

import me.ampayne2.ultimategames.UltimateGames;

public class WhitelistManager {
    
    private BlockPlaceWhitelist blockPlaceWhitelist;
    private BlockBreakWhitelist blockBreakWhitelist;
    
    /**
     * The manager for all ultimategames whitelists.
     * @param ultimateGames The UltimateGames instance.
     */
    public WhitelistManager(UltimateGames ultimateGames) {
        blockPlaceWhitelist = new BlockPlaceWhitelist(ultimateGames);
        blockBreakWhitelist = new BlockBreakWhitelist(ultimateGames);
    }
    
    /**
     * Gets the Block Place Whitelist.
     * @return The Block Place Whitelist.
     */
    public BlockPlaceWhitelist getBlockPlaceWhitelist() {
        return blockPlaceWhitelist;
    }
    
    /**
     * Gets the Block Break Whitelist.
     * @return The Block Break Whitelist.
     */
    public BlockBreakWhitelist getBlockBreakWhitelist() {
        return blockBreakWhitelist;
    }

}
