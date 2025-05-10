package com.denfop.items.modules;


public enum EnumQuarryModules {
    FURNACE(0, EnumQuarryType.FURNACE, 1, 5),
    SPEED_I(1, EnumQuarryType.SPEED, 1, -5),
    SPEED_II(2, EnumQuarryType.SPEED, 2, -10),
    SPEED_III(3, EnumQuarryType.SPEED, 3, -15),
    SPEED_IV(4, EnumQuarryType.SPEED, 4, -20),
    SPEED_V(5, EnumQuarryType.SPEED, 5, -25),
    LUCKY_I(6, EnumQuarryType.LUCKY, 1, 2),
    LUCKY_II(7, EnumQuarryType.LUCKY, 2, 4),
    LUCKY_III(8, EnumQuarryType.LUCKY, 3, 6),
    DEPTH_I(9, EnumQuarryType.DEPTH, 3, Math.pow(4, 1)),
    DEPTH_II(10, EnumQuarryType.DEPTH, 5, Math.pow(4, 2)),
    DEPTH_III(11, EnumQuarryType.DEPTH, 7, Math.pow(4, 3)),

    BLACKLIST(12, EnumQuarryType.BLACKLIST, 1, 0),
    WHITELIST(13, EnumQuarryType.WHITELIST, 1, 0),
    MACERATOR(14, EnumQuarryType.MACERATOR, 1, 5),
    COMBMAC(15, EnumQuarryType.COMB_MAC, 1, 5),
    POLISHER(16, EnumQuarryType.POLISHER, 1, 8),
    ;
    public final int meta;
    public final EnumQuarryType type;
    public final int efficiency;
    public final double cost;

    EnumQuarryModules(int meta, EnumQuarryType type, int efficiency, double cost) {
        this.meta = meta;
        this.type = type;
        this.efficiency = efficiency;
        this.cost = cost / 100;
    }

    public static EnumQuarryModules getFromID(final int ID) {
        return values()[ID % values().length];
    }


}
