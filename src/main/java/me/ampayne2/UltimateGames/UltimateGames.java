package me.ampayne2.UltimateGames;

import org.bukkit.plugin.java.JavaPlugin;

public class UltimateGames extends JavaPlugin {
	private JavaPlugin Plugin;
	private MessageConfig MessageConfig;
	private Message Message;

	public void onEnable() {
		Plugin = this;
		MessageConfig = new MessageConfig(this, "MessageConfig.yml");
		MessageConfig.saveMC();
		Message = new Message(this);
		
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void onDisable() {

	}
	
	public JavaPlugin getPlugin(){
		return Plugin;
	}
	
	public Message getMessage() {
		return Message;
	}
	
	public MessageConfig getMessageConfig() {
		return MessageConfig;
	}
}
