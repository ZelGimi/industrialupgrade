package com.denfop.items.armour.special;

import net.minecraft.world.entity.EquipmentSlot;

import java.util.Arrays;
import java.util.List;

public enum EnumSubTypeArmor {
    HELMET(
            EquipmentSlot.HEAD,
            EnumCapability.NIGHT_VISION_WITH,
            EnumCapability.NIGHT_VISION_WITHOUT,
            EnumCapability.ACTIVE_EFFECT,
            EnumCapability.FOOD,
            EnumCapability.ALL_ACTIVE_EFFECT, EnumCapability.NIGHT_VISION_AUTO
    ),
    CHESTPLATE(
            EquipmentSlot.CHEST,
            EnumCapability.JETPACK,
            EnumCapability.JETPACK_FLY,
            EnumCapability.FLY,
            EnumCapability.VERTICAL_FLY
    ),
    LEGGINGS(EquipmentSlot.LEGS, EnumCapability.SPEED, EnumCapability.BAGS, EnumCapability.MAGNET),
    BOOTS(EquipmentSlot.FEET, EnumCapability.BIG_JUMP, EnumCapability.AUTO_JUMP);

    private final EquipmentSlot entityEquipmentSlot;
    private final List<EnumCapability> capabilities;

    EnumSubTypeArmor(EquipmentSlot entityEquipmentSlot, EnumCapability... capabilities) {
        this.entityEquipmentSlot = entityEquipmentSlot;
        this.capabilities = Arrays.asList(capabilities);
    }

    public EquipmentSlot getEntityEquipmentSlot() {
        return entityEquipmentSlot;
    }

    public List<EnumCapability> getCapabilities() {
        return capabilities;
    }
}
