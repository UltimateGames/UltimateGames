package me.ampayne2.ultimategames.enums;

public enum SignType {
	LOBBY(false),
	CLICK_INPUT(true),
	REDSTONE_INPUT(true),
	TEXT_OUTPUT(true),
	REDSTONE_OUTPUT(true);
	
	private Boolean hasLabel;
	
	private SignType(Boolean hasLabel) {
		this.hasLabel = hasLabel;
	}
	
	public Boolean hasLabel() {
		return hasLabel;
	}
}
