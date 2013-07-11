package me.ampayne2.UltimateGames.Database.Tables;

import java.util.HashMap;

import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

import org.bukkit.inventory.ItemStack;

@Table("playerData")
public class PlayerData {

	@Id
	public int id;

	@Field
	public String playerName;

	@Field
	public ItemStack[] inventory;

	@Field
	public ItemStack[] armorInventory;
	
	@Field
	public Integer gamemode;
	
	@Field
	public Integer experience;
	
	@Field
	public HashMap<String, Integer> effects;

}
