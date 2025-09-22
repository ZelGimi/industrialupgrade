package com.denfop.items.armour.special;

import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Arrays;
import java.util.List;

public enum EnumSubTypeArmor {
    HELMET(
            EntityEquipmentSlot.HEAD,
            EnumCapability.NIGHT_VISION_WITH,
            EnumCapability.NIGHT_VISION_WITHOUT,
            EnumCapability.ACTIVE_EFFECT,
            EnumCapability.FOOD,
            EnumCapability.ALL_ACTIVE_EFFECT, EnumCapability.NIGHT_VISION_AUTO
    ),
    CHESTPLATE(
            EntityEquipmentSlot.CHEST,
            EnumCapability.JETPACK,
            EnumCapability.JETPACK_FLY,
            EnumCapability.FLY,
            EnumCapability.VERTICAL_FLY
    ),
    LEGGINGS(EntityEquipmentSlot.LEGS, EnumCapability.SPEED, EnumCapability.BAGS, EnumCapability.MAGNET),
    BOOTS(EntityEquipmentSlot.FEET, EnumCapability.BIG_JUMP, EnumCapability.AUTO_JUMP);

    private final EntityEquipmentSlot entityEquipmentSlot;
    private final List<EnumCapability> capabilities;

    EnumSubTypeArmor(EntityEquipmentSlot entityEquipmentSlot, EnumCapability... capabilities) {
        this.entityEquipmentSlot = entityEquipmentSlot;
        this.capabilities = Arrays.asList(capabilities);
    }

    public EntityEquipmentSlot getEntityEquipmentSlot() {
        return entityEquipmentSlot;
    }

    public List<EnumCapability> getCapabilities() {
        return capabilities;
    }
}
