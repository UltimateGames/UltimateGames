package me.ampayne2.ultimategames.whitelist;

import me.ampayne2.ultimategames.Manager;
import me.ampayne2.ultimategames.UltimateGames;

public class WhitelistManager implements Manager {
    
    private boolean loaded = false;
    private UltimateGames ultimateGames;
    private BlockPlaceWhitelist blockPlaceWhitelist;
    private BlockBreakWhitelist blockBreakWhitelist;
    
    /**
     * The manager for all ultimategames whitelists.
     * @param ultimateGames The UltimateGames instance.
     */
    public WhitelistManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
    }
    
    @Override
    public boolean load() {
        return reload();
    }

    @Override
    public boolean reload() {
        blockPlaceWhitelist = new BlockPlaceWhitelist(ultimateGames);
        blockPlaceWhitelist.reload();
        blockBreakWhitelist = new BlockBreakWhitelist(ultimateGames);
        blockBreakWhitelist.reload();
        loaded = true;
        return true;
    }

    @Override
    public void unload() {
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
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
