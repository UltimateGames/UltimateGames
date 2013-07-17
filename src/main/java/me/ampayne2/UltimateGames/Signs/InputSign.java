package me.ampayne2.UltimateGames.Signs;

import me.ampayne2.UltimateGames.Arenas.Arena;

import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;

public class InputSign extends UGSign{

	private String label;
	private Sign sign;
	private Arena arena;
	private String[] lines;
	
	public InputSign(String label, Sign sign, Arena arena) {
		super(sign, arena);
		this.label = label;
		this.sign = sign;
		this.arena = arena;
		arena.getGame().getGamePlugin().handleInputSignCreate(arena, sign, label);
	}
	
	@Override
	public void onSignClick(PlayerInteractEvent event) {
		//TODO: Permission Check
		arena.getGame().getGamePlugin().handleInputSignClick(arena, sign, label, event);
	}

	@Override
	public String[] getUpdatedLines() {
		return lines;
	}
	
	public void setLines(String[] lines) {
		this.lines = lines;
	}

}
