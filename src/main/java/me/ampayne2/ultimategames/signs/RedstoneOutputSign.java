package me.ampayne2.ultimategames.signs;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.SignType;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;

public class RedstoneOutputSign extends UGSign {

	private String label;
	private String[] lines = new String[4];
	private Boolean powered;
	
	public RedstoneOutputSign(String label, Sign sign, Arena arena) {
		super(sign, arena);
		this.label = label;
		powered = false;
		arena.getGame().getGamePlugin().handleUGSignCreate(this, SignType.getSignTypeFromClass(this.getClass()));
		update();
	}

	@Override
	public void onSignTrigger(Event event) {
		
	}

	@Override
	public String[] getUpdatedLines() {
		return lines;
	}
	
    public void setLines(String[] lines) {
    	this.lines = lines;
    }
	
	public String getLabel() {
		return label;
	}
	
	public Boolean isPowered() {
		return powered;
	}
	
	public void setPowered(Boolean powered) {
		this.powered = powered;
		if (powered) {
			getSign().getLocation().getBlock().setType(Material.REDSTONE_BLOCK);
		} else {
			getSign().getLocation().getBlock().setType(getSign().getBlock().getType());
			getSign().getLocation().getBlock().getState().setData((org.bukkit.material.Sign) getSign().getData());
			getSign().update();
		}
	}

}
