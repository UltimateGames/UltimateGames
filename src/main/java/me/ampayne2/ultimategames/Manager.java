package me.ampayne2.ultimategames;

/**
 * Base for all manager classes.
 */
public interface Manager {

    /**
     * Loads the manager. Loads what the manager does, not the class, which is already instantiated.
     * @return If the manager loaded successfully.
     */
    boolean load();

    /**
     * Reloads the manager. Reloads what the manager does, doesn't instantiate a new class.
     * @return If the manager reloaded successfully.
     */
    boolean reload();

    /**
     * Unloads the manager.
     */
    void unload();
    
    /**
     * Checks to see if the manager is loaded.
     * @return If the manager is loaded.
     */
    boolean isLoaded();

}
