package me.ampayne2.ultimategames.utils;

import me.ampayne2.ultimategames.api.PointManager;
import me.ampayne2.ultimategames.games.Game;

public class NullPointManager implements PointManager{
    @Override
    public void addPoint(Game game, String playerName, String valueName, int amount) {}

    @Override
    public boolean hasPerk(Game game, String playerName, String valueName) {
        return false;
    }
}
