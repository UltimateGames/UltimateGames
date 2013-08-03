package me.ampayne2.ultimategames.signs;

import me.ampayne2.ultimategames.arenas.Arena;
import me.ampayne2.ultimategames.enums.SignType;

import org.bukkit.block.Sign;
import org.bukkit.event.Event;

public class RedstoneInputSign extends UGSign {

	private String label;
	private String[] lines = new String[4];
	
	public RedstoneInputSign(String label, Sign sign, Arena arena) {
		super(sign, arena);
		this.label = label;
	}

	@Override
	public void onSignTrigger(Event event) {
		getArena().getGame().getGamePlugin().handleInputSignTrigger(this, SignType.REDSTONE_INPUT, event);
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

}
