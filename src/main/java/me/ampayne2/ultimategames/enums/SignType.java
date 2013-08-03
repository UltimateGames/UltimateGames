package me.ampayne2.ultimategames.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import me.ampayne2.ultimategames.signs.ClickInputSign;
import me.ampayne2.ultimategames.signs.LobbySign;
import me.ampayne2.ultimategames.signs.RedstoneInputSign;
import me.ampayne2.ultimategames.signs.RedstoneOutputSign;
import me.ampayne2.ultimategames.signs.TextOutputSign;

public enum SignType {
	LOBBY(LobbySign.class, false),
	CLICK_INPUT(ClickInputSign.class, true),
	REDSTONE_INPUT(RedstoneInputSign.class, true),
	TEXT_OUTPUT(TextOutputSign.class, true),
	REDSTONE_OUTPUT(RedstoneOutputSign.class, true);
	
	private static final Map<Class<?>, SignType> classTypes = new HashMap<Class<?>, SignType>();
	
    static {
        for(SignType signType : EnumSet.allOf(SignType.class)) {
            classTypes.put(signType.getClassType(), signType);
        }
    }
	
    private Class<?> signClass;
	private Boolean hasLabel;
	
	private SignType(Class<?> signClass, Boolean hasLabel) {
		this.hasLabel = hasLabel;
		this.signClass = signClass;
	}
	
	public Boolean hasLabel() {
		return hasLabel;
	}
	
	public Class<?> getClassType() {
		return signClass;
	}
	
	public static SignType getSignTypeFromClass(Class<?> signClass) {
		return classTypes.get(signClass);
	}
}
