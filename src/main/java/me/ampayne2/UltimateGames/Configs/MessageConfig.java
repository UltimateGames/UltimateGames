package me.ampayne2.UltimateGames.Configs;

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

	private FileConfiguration msgConfig;
	private File msgConfigFile;

	public MessageConfig(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		File dataFolder = plugin.getDataFolder();
		this.msgConfigFile = new File(dataFolder, fileName);
		reloadMessageConfig();
	}

	public void reloadMessageConfig() {
		msgConfig = YamlConfiguration.loadConfiguration(msgConfigFile);
		InputStream defConfigStream = plugin.getResource(fileName);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			msgConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getMessageConfig() {
		if (msgConfig == null) {
			this.reloadMessageConfig();
		}
		return msgConfig;
	}

	public void saveMessageConfig() {
		if (msgConfig == null || msgConfigFile == null) {
			return;
		}
		try {
			getMessageConfig().save(msgConfigFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + msgConfigFile + ".");
		}
	}

	public void saveDefaultMessageConfig() {
		if (!msgConfigFile.exists()) {
			this.plugin.saveResource(fileName, false);
		}
	}
}
