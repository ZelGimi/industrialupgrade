package com.denfop.items.armour.special;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

import java.util.Arrays;
import java.util.List;

public enum EnumSubTypeArmor {
    HELMET(
            ArmorItem.Type.HELMET,
            EnumCapability.NIGHT_VISION_WITH,
            EnumCapability.NIGHT_VISION_WITHOUT,
            EnumCapability.ACTIVE_EFFECT,
            EnumCapability.FOOD,
            EnumCapability.ALL_ACTIVE_EFFECT, EnumCapability.NIGHT_VISION_AUTO
    ),
    CHESTPLATE(
            ArmorItem.Type.CHESTPLATE,
            EnumCapability.JETPACK,
            EnumCapability.JETPACK_FLY,
            EnumCapability.FLY,
            EnumCapability.VERTICAL_FLY
    ),
    LEGGINGS(ArmorItem.Type.LEGGINGS, EnumCapability.SPEED, EnumCapability.BAGS, EnumCapability.MAGNET),
    BOOTS(ArmorItem.Type.BOOTS, EnumCapability.BIG_JUMP, EnumCapability.AUTO_JUMP);

    private final ArmorItem.Type entityEquipmentSlot;
    private final List<EnumCapability> capabilities;

    EnumSubTypeArmor(ArmorItem.Type entityEquipmentSlot, EnumCapability... capabilities) {
        this.entityEquipmentSlot = entityEquipmentSlot;
        this.capabilities = Arrays.asList(capabilities);
    }

    public EquipmentSlot getEntityEquipmentSlot() {
        return entityEquipmentSlot.getSlot();
    }

    public ArmorItem.Type getEntityType() {
        return entityEquipmentSlot;
    }

    public List<EnumCapability> getCapabilities() {
        return capabilities;
    }
}
