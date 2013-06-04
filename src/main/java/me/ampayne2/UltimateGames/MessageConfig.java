package me.ampayne2.UltimateGames;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig {
	private final JavaPlugin plugin;
	private final String fileName;

	private FileConfiguration MC;
	private File MCFile;

	public MessageConfig(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		File dataFolder = plugin.getDataFolder();
		this.MCFile = new File(dataFolder, fileName);
		reloadMC();
	}

	public void reloadMC() {
		MC = YamlConfiguration.loadConfiguration(MCFile);
		InputStream defConfigStream = plugin.getResource(fileName);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			MC.setDefaults(defConfig);
		}
	}

	public FileConfiguration getMC() {
		if (MC == null) {
			this.reloadMC();
		}
		return MC;
	}

	public void saveMC() {
		if (MC == null || MCFile == null) {
			return;
		}
		try {
			getMC().save(MCFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + MCFile + ".");
		}
	}

	public void saveDefaultMC() {
		if (!MCFile.exists()) {
			this.plugin.saveResource(fileName, false);
		}
	}
}
