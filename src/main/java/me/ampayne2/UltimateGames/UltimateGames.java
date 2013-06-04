package me.ampayne2.UltimateGames;

import org.bukkit.plugin.java.JavaPlugin;

public class UltimateGames extends JavaPlugin {
	private JavaPlugin plugin;
	private MessageConfig msgConfig;
	private Message msg;

	public void onEnable() {
		plugin = this;
		msgConfig = new MessageConfig(this, "MessageConfig.yml");
		msgConfig.saveMessageConfig();
		msg = new Message(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void onDisable() {

	}
	
	public JavaPlugin getPlugin(){
		return plugin;
	}
	
	public Message getMessage() {
		return msg;
	}
	
	public MessageConfig getMessageConfig() {
		return msgConfig;
	}
}
