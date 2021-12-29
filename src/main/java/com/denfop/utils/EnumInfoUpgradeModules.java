package com.denfop.utils;

public enum EnumInfoUpgradeModules {
    AOE_DIG(2, "AOE_dig"),
    DIG_DEPTH(2, "dig_depth"),
    ENERGY(2, "energy"),
    EFFICIENCY(2, "speed"),
    GENDAY(2, "genday"),
    GENNIGHT(2, "gennight"),
    STORAGE(2, "storage"),
    PROTECTION(4, "protect"),
    FLYSPEED(2, "flyspeed"),
    BOWENERGY(2, "bowenergy"),
    BOWDAMAGE(2, "bowdamage"),
    SABERENERGY(2, "saberenergy"),
    FIRE_PROTECTION(1, "fireResistance"),
    JUMP(1, "jump"),
    WATER(1, "waterBreathing"),
    SPEED(1, "moveSpeed"),
    SABER_DAMAGE(2, "saberdamage"),
    VAMPIRES(3, "vampires"),
    RESISTANCE(3, "resistance"),
    POISON(1, "poison"),
    WITHER(1, "wither"),
    SILK_TOUCH(1, "silk"),
    INVISIBILITY(1, "invisibility"),
    LOOT(3, "loot"),
    FIRE(2, "fire"),
    REPAIRED(3, "repaired"),
    ;
    public final int max;
    public final String name;

    EnumInfoUpgradeModules(int max, String name) {
        this.max = max;
        this.name = name;
    }
}
