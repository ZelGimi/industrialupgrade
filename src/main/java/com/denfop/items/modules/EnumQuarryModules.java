package com.denfop.items.modules;


public enum EnumQuarryModules {
    FURNACE(0, EnumQuarryType.FURNACE, 1),
    SPEED_I(1, EnumQuarryType.SPEED, 1),
    SPEED_II(2, EnumQuarryType.SPEED, 2),
    SPEED_III(3, EnumQuarryType.SPEED, 3),
    SPEED_IV(4, EnumQuarryType.SPEED, 4),
    SPEED_V(5, EnumQuarryType.SPEED, 5),
    LUCKY_I(6, EnumQuarryType.LUCKY, 1),
    LUCKY_II(7, EnumQuarryType.LUCKY, 2),
    LUCKY_III(8, EnumQuarryType.LUCKY, 3),
    DEPTH_I(9, EnumQuarryType.DEPTH, 3),
    DEPTH_II(10, EnumQuarryType.DEPTH, 5),
    DEPTH_III(11, EnumQuarryType.DEPTH, 7),

    BLACKLIST(12, EnumQuarryType.BLACKLIST, 1),
    WHITELIST(13, EnumQuarryType.WHITELIST, 1);
    public final int meta;
    public final EnumQuarryType type;
    public final int efficiency;

    EnumQuarryModules(int meta, EnumQuarryType type, int efficiency) {
        this.meta = meta;
        this.type = type;
        this.efficiency = efficiency;
    }
    public static EnumQuarryModules getFromID(final int ID) {
        return values()[ID % values().length];
    }



}
