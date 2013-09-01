package me.ampayne2.ultimategames.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import me.ampayne2.ultimategames.chests.RandomChest;
import me.ampayne2.ultimategames.chests.StaticChest;

public enum ChestType {
    
    RANDOM(RandomChest.class, false), 
    STATIC(StaticChest.class, true);

    private static final Map<Class<?>, ChestType> CLASS_TYPES = new HashMap<Class<?>, ChestType>();

    static {
        for (ChestType chestType : EnumSet.allOf(ChestType.class)) {
            CLASS_TYPES.put(chestType.getClassType(), chestType);
        }
    }

    private Class<?> chestClass;
    private boolean hasLabel;

    private ChestType(Class<?> chestClass, boolean hasLabel) {
        this.hasLabel = hasLabel;
        this.chestClass = chestClass;
    }

    public boolean hasLabel() {
        return hasLabel;
    }

    public Class<?> getClassType() {
        return chestClass;
    }

    public static ChestType getChestTypeFromClass(Class<?> signClass) {
        return CLASS_TYPES.get(signClass);
    }

}
