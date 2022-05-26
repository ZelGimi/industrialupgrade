package com.denfop.utils;

import com.denfop.IUItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum EnumInfoUpgradeModules {
    GENDAY(2, "genday"), // 0
    GENNIGHT(2, "gennight"), // 1
    PROTECTION(4, "protect"), // 2
    EFFICIENCY(2, "speed"),// 3
    BOWENERGY(2, "bowenergy"), // 4
    SABERENERGY(2, "saberenergy"),//5
    DIG_DEPTH(2, "dig_depth"),//6
    FIRE_PROTECTION(1, "fireResistance"),//7
    WATER(1, "waterBreathing"),//8
    SPEED(1, "moveSpeed"),//9
    JUMP(1, "jump"),//10
    BOWDAMAGE(2, "bowdamage"),//11
    SABER_DAMAGE(2, "saberdamage"),//12
    AOE_DIG(2, "AOE_dig"),//13
    FLYSPEED(2, "flyspeed"),//14
    STORAGE(2, "storage"),//15
    ENERGY(2, "energy"),//16
    VAMPIRES(3, "vampires"),//17
    RESISTANCE(3, "resistance"),//18
    POISON(1, "poison"),//19
    WITHER(1, "wither"),//20
    SILK_TOUCH(1, "silk"),//21
    INVISIBILITY(1, "invisibility"),//22
    LOOT(3, "loot"),//23
    FIRE(2, "fire"),//24
    REPAIRED(3, "repaired"),//25
    LUCKY(3, "lucky"),//26
    EFFICIENT(3, "efficient"),//27
    SMELTER(1, "smelter", 35, 36, 39),//28
    NIGTHVISION(1, "nightvision"),//29
    THORNS(3, "thorns"),//30
    EXPERIENCE(3, "experience"),//31
    BLINDNESS(1, "blindness"),//32
    PROTECTION_ARROW(2, "protection_arrow"),//33
    FALLING_DAMAGE(3, "falling_damage"),//34
    MACERATOR(1, "macerator", 36, 39, 28),//35
    COMB_MACERATOR(1, "comb_macerator", 39, 35, 28),//36
    RANDOM(3, "random"),//37
    HUNGRY(1, "hungry"),//38
    GENERATOR(1, "generator", 36, 35, 28),//39
    ;
    public final int max;
    public final String name;
    public final List<Integer> list;

    EnumInfoUpgradeModules(int max, String name, Integer... enumInfoUpgradeModules) {
        this.max = max;
        this.name = name;
        this.list = Arrays.asList(enumInfoUpgradeModules);
        IUItem.list.add(name);
    }

    EnumInfoUpgradeModules(int max, String name) {
        this.max = max;
        this.name = name;
        this.list = new ArrayList<>();
        IUItem.list.add(name);
    }

    public static EnumInfoUpgradeModules getFromID(final int ID) {
        return values()[ID % values().length];
    }

}
