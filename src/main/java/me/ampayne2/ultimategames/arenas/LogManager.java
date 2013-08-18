package me.ampayne2.ultimategames.arenas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import me.ampayne2.ultimategames.UltimateGames;
import me.ampayne2.ultimategames.database.tables.BlockChangeTable;

public class LogManager {

    private UltimateGames ultimateGames;
    private Integer logTask;
    private Map<Arena, Integer> rollbackTask = new HashMap<Arena, Integer>();
    private List<BlockChange> pendingChanges = new ArrayList<BlockChange>();
    private List<BlockChange> savingChanges = new ArrayList<BlockChange>();
    private Boolean logging = false;

    public LogManager(UltimateGames ultimateGames) {
        this.ultimateGames = ultimateGames;
        startLogSaving();
    }

    public void logBlockChange(Arena arena, Material material, byte data, Location location) {
        pendingChanges.add(new BlockChange(arena, material, data, location));
    }

    public void logBlockChanges(Arena arena, Set<Block> blocks) {
        for (Block block : blocks) {
            pendingChanges.add(new BlockChange(arena, block.getType(), block.getData(), block.getLocation()));
        }
    }

    public void startLogSaving() {
        logTask = Bukkit.getScheduler().scheduleSyncRepeatingTask(ultimateGames, new Runnable() {
            @Override
            public void run() {
                if (!pendingChanges.isEmpty() && !logging) {
                    logging = true;
                    savingChanges.addAll(pendingChanges);
                    pendingChanges.clear();
                    for (BlockChange blockChange : savingChanges) {
                        Arena arena = blockChange.arena;
                        Material material = blockChange.material;
                        byte data = blockChange.data;
                        Location location = blockChange.location;
                        if (!((material == Material.WOODEN_DOOR || material == Material.IRON_DOOR_BLOCK) && (data & 0x8) == 0x8)) {
                            String gameName = arena.getGame().getGameDescription().getName();
                            String arenaName = arena.getName();
                            double x = location.getX();
                            double y = location.getY();
                            double z = location.getZ();
                            if (ultimateGames.getDatabaseManager().getDatabase().select(BlockChangeTable.class).where().equal("gameName", gameName).and().equal("arenaName", arenaName).and().equal(
                                    "x", x).and().equal("y", y).and().equal("z", z).execute().findOne() == null) {
                                BlockChangeTable table = new BlockChangeTable();
                                table.gameName = gameName;
                                table.arenaName = arenaName;
                                table.x = x;
                                table.y = y;
                                table.z = z;
                                table.material = material.name();
                                table.data = data;
                                ultimateGames.getDatabaseManager().getDatabase().save(table);
                                System.out.println("Saved Block");
                            }
                        }
                    }
                    savingChanges.clear();
                    logging = false;
                }
            }
        }, 0L, 5L);
    }

    public void stopLogSaving() {
        Bukkit.getScheduler().cancelTask(logTask);
        logTask = null;
    }

    public void rollbackArena(Arena arena) {
        if (!rollbackTask.containsKey(arena)) {
            startRollingBack(arena);
        }
    }

    public void startRollingBack(final Arena arena) {
        rollbackTask.put(arena, Bukkit.getScheduler().scheduleSyncRepeatingTask(ultimateGames, new Runnable() {
            @Override
            public void run() {
                List<BlockChangeTable> changes = ultimateGames.getDatabaseManager().getDatabase().select(BlockChangeTable.class).where().equal("gameName",
                        arena.getGame().getGameDescription().getName()).and().equal("arenaName", arena.getName()).execute().find();
                if (changes.isEmpty()) {
                    stopRollingBack(arena);
                } else {
                    for (int i = 0; i < 50; i++) {
                        if ((changes.size() > i)) {
                            return;
                        }
                        BlockChangeTable entry = changes.get(i);
                        Location location = new Location(arena.getWorld(), entry.x, entry.y, entry.z);
                        Material material = Material.valueOf(entry.material);
                        if (!(location.getBlock().getType() == material && location.getBlock().getData() == entry.data)) {
                            if (material == Material.WOODEN_DOOR || material == Material.IRON_DOOR_BLOCK) {
                                location.getBlock().setTypeIdAndData(material.getId(), entry.data, false);
                                location.getBlock().getRelative(BlockFace.UP).setTypeIdAndData(material.getId(), (byte) (entry.data | 0x8), false);
                            } else {
                                location.getBlock().setType(material);
                                location.getBlock().setData(entry.data);
                            }
                        }
                        ultimateGames.getDatabaseManager().getDatabase().remove(
                                ultimateGames.getDatabaseManager().getDatabase().select(BlockChangeTable.class).where().equal("gameName", arena.getGame().getGameDescription().getName()).and().equal(
                                        "arenaName", arena.getName()).and().equal("x", entry.x).and().equal("y", entry.y).and().equal("z", entry.z).execute().findOne());
                    }
                }
            }
        }, 0L, 100L));
    }

    public void stopRollingBack(Arena arena) {
        Bukkit.getScheduler().cancelTask(rollbackTask.get(arena));
        rollbackTask.remove(arena);
    }
}
