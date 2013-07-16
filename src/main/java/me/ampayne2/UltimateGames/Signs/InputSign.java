package me.ampayne2.UltimateGames.Signs;

import me.ampayne2.UltimateGames.Arenas.Arena;

import org.bukkit.block.Sign;
import org.bukkit.event.player.PlayerInteractEvent;

public class InputSign extends UGSign{

	private Sign sign;
	private Arena arena;
	private String[] lines;
	
	public InputSign(Sign sign, Arena arena) {
		super(sign, arena);
		this.sign = sign;
		this.arena = arena;
	}
	
	@Override
	public void onSignClick(PlayerInteractEvent event) {
		//TODO: Permission Check
		arena.getGame().getGamePlugin().handleInputSignClick(arena, sign, event);
	}

	@Override
	public String[] getUpdatedLines() {
		return lines;
	}
	
	public void setLines(String[] lines) {
		this.lines = lines;
	}

}
