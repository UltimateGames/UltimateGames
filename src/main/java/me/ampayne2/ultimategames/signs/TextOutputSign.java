package me.ampayne2.ultimategames.signs;

import me.ampayne2.ultimategames.arenas.Arena;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;

public class TextOutputSign extends UGSign {

	private String label;
	private String[] lines = new String[4];
	
	public TextOutputSign(String label, Sign sign, Arena arena) {
		super(sign, arena);
		this.label = label;
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

}
