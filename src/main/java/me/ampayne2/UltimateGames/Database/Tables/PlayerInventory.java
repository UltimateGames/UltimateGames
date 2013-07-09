package me.ampayne2.UltimateGames.Database.Tables;

import com.alta189.simplesave.Field;
import com.alta189.simplesave.Id;
import com.alta189.simplesave.Table;

import org.bukkit.inventory.ItemStack;

@Table("playerInv")
public class PlayerInventory {

	@Id
	public int id;

	@Field
	public String playerName;

	@Field
	public ItemStack[] inventory;

	@Field
	public ItemStack[] armorInventory;

}
