package com.denfop.api.item;


import com.denfop.api.radiationsystem.EnumLevelRadiation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IHazmatLike {

    static boolean hasCompleteHazmat(LivingEntity living) {
        return hasCompleteHazmat(living, EnumLevelRadiation.LOW);
    }

    static boolean hasCompleteHazmat(LivingEntity living, EnumLevelRadiation levelRadiation) {
        EquipmentSlot[] var1 = EquipmentSlot.values();
        int var2 = var1.length;

        for (EquipmentSlot slot : var1) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = living.getItemBySlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof IHazmatLike hazmat)) {
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
