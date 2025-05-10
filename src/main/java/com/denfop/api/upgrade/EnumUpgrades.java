package com.denfop.api.upgrade;

import com.denfop.items.EnumInfoUpgradeModules;

import java.util.Arrays;
import java.util.List;

public enum EnumUpgrades {
    INSTRUMENTS(
            EnumInfoUpgradeModules.LUCKY,
            EnumInfoUpgradeModules.MACERATOR,
            EnumInfoUpgradeModules.GENERATOR,
            EnumInfoUpgradeModules.RANDOM,
            EnumInfoUpgradeModules.COMB_MACERATOR,
            EnumInfoUpgradeModules.EFFICIENT,
            EnumInfoUpgradeModules.SILK_TOUCH,
            EnumInfoUpgradeModules.ENERGY,
            EnumInfoUpgradeModules.EXPERIENCE,
            EnumInfoUpgradeModules.AOE_DIG,
            EnumInfoUpgradeModules.DIG_DEPTH,
            EnumInfoUpgradeModules.SMELTER
    ),
    PURIFIER(
            EnumInfoUpgradeModules.PURIFIER,
            EnumInfoUpgradeModules.ENERGY
    ),
    GRAVITOOL(
            EnumInfoUpgradeModules.PURIFIER,
            EnumInfoUpgradeModules.LATEX,
            EnumInfoUpgradeModules.WRENCH,
            EnumInfoUpgradeModules.ENERGY
    ),
    SOLAR_HELMET(
            EnumInfoUpgradeModules.GENDAY,
            EnumInfoUpgradeModules.GENNIGHT,
            EnumInfoUpgradeModules.PROTECTION,
            EnumInfoUpgradeModules.WATER,
            EnumInfoUpgradeModules.STORAGE,
            EnumInfoUpgradeModules.INVISIBILITY,
            EnumInfoUpgradeModules.NIGTHVISION,
            EnumInfoUpgradeModules.REPAIRED,
            EnumInfoUpgradeModules.RESISTANCE
    ),
    HELMET(
            EnumInfoUpgradeModules.PROTECTION,
            EnumInfoUpgradeModules.WATER,
            EnumInfoUpgradeModules.INVISIBILITY,
            EnumInfoUpgradeModules.NIGTHVISION,
            EnumInfoUpgradeModules.REPAIRED,
            EnumInfoUpgradeModules.RESISTANCE
    ),
    BODY(
            EnumInfoUpgradeModules.PROTECTION,
            EnumInfoUpgradeModules.FIRE_PROTECTION,
            EnumInfoUpgradeModules.FLYSPEED,
            EnumInfoUpgradeModules.RESISTANCE,
            EnumInfoUpgradeModules.INVISIBILITY,
            EnumInfoUpgradeModules.REPAIRED,
            EnumInfoUpgradeModules.THORNS,
            EnumInfoUpgradeModules.PROTECTION_ARROW
    ),
    LEGGINGS(
            EnumInfoUpgradeModules.PROTECTION,
            EnumInfoUpgradeModules.SPEED,
            EnumInfoUpgradeModules.RESISTANCE,
            EnumInfoUpgradeModules.INVISIBILITY,
            EnumInfoUpgradeModules.REPAIRED
    ),
    BOOTS(
            EnumInfoUpgradeModules.PROTECTION,
            EnumInfoUpgradeModules.JUMP,
            EnumInfoUpgradeModules.RESISTANCE,
            EnumInfoUpgradeModules.INVISIBILITY,
            EnumInfoUpgradeModules.REPAIRED,
            EnumInfoUpgradeModules.FALLING_DAMAGE
    ),
    SABERS(
            EnumInfoUpgradeModules.SABER_DAMAGE,
            EnumInfoUpgradeModules.SABERENERGY,
            EnumInfoUpgradeModules.VAMPIRES,
            EnumInfoUpgradeModules.POISON,
            EnumInfoUpgradeModules.WITHER,
            EnumInfoUpgradeModules.LOOT,
            EnumInfoUpgradeModules.FIRE,
            EnumInfoUpgradeModules.HUNGRY
    ),
    BOW(
            EnumInfoUpgradeModules.BOWENERGY,
            EnumInfoUpgradeModules.BOWDAMAGE,
            EnumInfoUpgradeModules.BLINDNESS
    ),
    BAGS(EnumInfoUpgradeModules.BAGS, EnumInfoUpgradeModules.ENERGY),
    MAGNET(EnumInfoUpgradeModules.SIZE, EnumInfoUpgradeModules.ENERGY),
    LAPPACK(
            EnumInfoUpgradeModules.LAPPACK_ENERGY,
            EnumInfoUpgradeModules.PROTECTION,
            EnumInfoUpgradeModules.FIRE_PROTECTION,
            EnumInfoUpgradeModules.FLYSPEED,
            EnumInfoUpgradeModules.RESISTANCE,
            EnumInfoUpgradeModules.INVISIBILITY,
            EnumInfoUpgradeModules.THORNS,
            EnumInfoUpgradeModules.PROTECTION_ARROW
    ),
    JETPACK(
            EnumInfoUpgradeModules.FLY,
            EnumInfoUpgradeModules.FIRE_PROTECTION,
            EnumInfoUpgradeModules.FLYSPEED,
            EnumInfoUpgradeModules.RESISTANCE,
            EnumInfoUpgradeModules.INVISIBILITY,
            EnumInfoUpgradeModules.THORNS,
            EnumInfoUpgradeModules.PROTECTION_ARROW
    ),
    ;
    public List<EnumInfoUpgradeModules> list;

    EnumUpgrades(EnumInfoUpgradeModules... updates) {
        this.list = Arrays.asList(updates);
    }
}
