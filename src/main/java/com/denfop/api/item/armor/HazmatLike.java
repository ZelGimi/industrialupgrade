package com.denfop.api.item.armor;


import com.denfop.api.pollution.radiation.EnumLevelRadiation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface HazmatLike {

    static boolean hasCompleteHazmat(LivingEntity living) {
        return hasCompleteHazmat(living, EnumLevelRadiation.LOW);
    }

    static boolean hasCompleteHazmat(LivingEntity living, EnumLevelRadiation levelRadiation) {
        EquipmentSlot[] var1 = EquipmentSlot.values();
        int var2 = var1.length;

        for (EquipmentSlot slot : var1) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack stack = living.getItemBySlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof HazmatLike hazmat)) {
                    return false;
                }

                if (!hazmat.addsProtection(living, slot, stack)) {
                    return false;
                }

                if (hazmat.fullyProtects(living, slot, stack)) {
                    switch (levelRadiation) {
                        case LOW:
                        case DEFAULT:
                        case MEDIUM:
                            return hazmat.getLevel() >= 1;
                        case HIGH:
                        case VERY_HIGH:
                            return hazmat.getLevel() > 1;
                    }
                    return false;
                }
                switch (levelRadiation) {
                    case LOW:
                    case DEFAULT:
                    case MEDIUM:
                        if (!(hazmat.getLevel() >= 1)) {
                            return false;
                        }
                        break;
                    case HIGH:
                    case VERY_HIGH:
                        if (!(hazmat.getLevel() > 1)) {
                            return false;
                        }

                        break;
                }
            }

        }

        return true;
    }

    default int getLevel() {
        return 1;
    }

    boolean addsProtection(LivingEntity var1, EquipmentSlot var2, ItemStack var3);

    default boolean fullyProtects(LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
        return false;
    }

}
