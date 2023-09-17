package com.denfop.items.armour.special;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.denfop.items.armour.special.EnumCapability.ACTIVE_EFFECT;
import static com.denfop.items.armour.special.EnumCapability.ALL_ACTIVE_EFFECT;
import static com.denfop.items.armour.special.EnumCapability.AUTO_JUMP;
import static com.denfop.items.armour.special.EnumCapability.BAGS;
import static com.denfop.items.armour.special.EnumCapability.BIG_JUMP;
import static com.denfop.items.armour.special.EnumCapability.FLY;
import static com.denfop.items.armour.special.EnumCapability.FOOD;
import static com.denfop.items.armour.special.EnumCapability.JETPACK;
import static com.denfop.items.armour.special.EnumCapability.JETPACK_FLY;
import static com.denfop.items.armour.special.EnumCapability.MAGNET;
import static com.denfop.items.armour.special.EnumCapability.NIGHT_VISION_AUTO;
import static com.denfop.items.armour.special.EnumCapability.NIGHT_VISION_WITH;
import static com.denfop.items.armour.special.EnumCapability.NIGHT_VISION_WITHOUT;
import static com.denfop.items.armour.special.EnumCapability.SPEED;
import static com.denfop.items.armour.special.EnumCapability.VERTICAL_FLY;

public enum EnumTypeArmor {
    NANO("nano", Arrays.asList("Zelen","Demon","Emerald","Cold","Dark","Desert","Fire","Snow","Taiga","Ukraine","Ender"), 3, 512,
            1000000,
            new ArmorMulDamage(0.1,
            0.25,
            0.2,
            0.1), 5000,
            NIGHT_VISION_WITH),
    ADV_NANO("adv_nano", Arrays.asList("Zelen","Snow","Desert"), 4, 2048, 10000000, new ArmorMulDamage(0.1, 0.3, 0.3, 0.1), 7500,
            NIGHT_VISION_WITHOUT,
            JETPACK, SPEED, BIG_JUMP
    ),
    QUANTUM("quantum",  Arrays.asList("Zelen","Snow","Desert","Demon","Emerald","Cold"), 5, 8192, 50000000, new ArmorMulDamage(0.2
            , 0.4,
            0.3, 0.2),
            10000,
            NIGHT_VISION_AUTO,
            ACTIVE_EFFECT, FOOD,
            JETPACK_FLY, SPEED, BAGS, BIG_JUMP, AUTO_JUMP
    ),
    SPECTRAL(
            "spectral",
            Arrays.asList("Zelen","Snow","Desert"),
            7,
            131072,
            300000000,
            new ArmorMulDamage(0.2, 0.5, 0.4, 0.2),
            20000,
            NIGHT_VISION_AUTO,
            ALL_ACTIVE_EFFECT,
            FOOD,
            FLY,
            VERTICAL_FLY,
            SPEED,
            BAGS,
            MAGNET,
            BIG_JUMP,
            AUTO_JUMP
    );
    private final String texture;
    private final List<String> skinsList;
    private final double maxTransfer;
    private final double maxEnergy;
    private final ArmorMulDamage armorMulDamage;
    private final double damageEnergy;
    private final List<EnumCapability> listCapability;
    private final int tier;

    EnumTypeArmor(
            String texture, List<String> skinsList, int tier, double maxTransfer, double maxEnergy,
            ArmorMulDamage armorMulDamage,
            double damageEnergy, EnumCapability... capabilities
    ) {
        this.texture = texture;
        this.skinsList = skinsList;
        this.maxTransfer = maxTransfer;
        this.maxEnergy = maxEnergy;
        this.armorMulDamage = armorMulDamage;
        this.damageEnergy = damageEnergy;
        this.tier = tier;
        this.listCapability = Arrays.asList(capabilities);
    }

    public int getTier() {
        return tier;
    }

    public double getDamageEnergy() {
        return damageEnergy;
    }

    public List<EnumCapability> getListCapability() {
        return listCapability;
    }

    public ArmorMulDamage getArmorMulDamage() {
        return armorMulDamage;
    }

    public double getMaxEnergy() {
        return maxEnergy;
    }

    public double getMaxTransfer() {
        return maxTransfer;
    }

    public List<String> getSkinsList() {
        return skinsList;
    }

    public String getTexture() {
        return texture;
    }
}
