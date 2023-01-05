package com.denfop.items.modules;

public enum EnumSpawnerModules {
    LUCKY_I(EnumSpawnerType.LUCKY, 0, 1),
    LUCKY_II(EnumSpawnerType.LUCKY, 1, 2),
    LUCKY_III(EnumSpawnerType.LUCKY, 2, 3),
    SPEED(EnumSpawnerType.SPEED, 3, 20),
    EXPERIENCE_I(EnumSpawnerType.EXPERIENCE, 4, 25),
    EXPERIENCE_II(EnumSpawnerType.EXPERIENCE, 5, 50),
    SPAWN(EnumSpawnerType.SPAWN, 6, 2),
    SPAWN_I(EnumSpawnerType.SPAWN, 7, 3),
    FIRE(EnumSpawnerType.FIRE, 8, 40),
    ;


    public final EnumSpawnerType type;
    public final int meta;
    public final int percent;

    EnumSpawnerModules(EnumSpawnerType type, int meta, int percent) {
        this.type = type;
        this.meta = meta;
        this.percent = percent;
    }

    public static EnumSpawnerModules getFromID(final int ID) {
        return values()[ID % values().length];
    }

}
