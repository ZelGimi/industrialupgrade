package com.denfop.api.item;

import com.denfop.api.radiationsystem.EnumLevelRadiation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemStack;

public interface IHazmatLike {

    static boolean hasCompleteHazmat(EntityLivingBase living) {
        return hasCompleteHazmat(living, EnumLevelRadiation.LOW);
    }

    static boolean hasCompleteHazmat(EntityLivingBase living, EnumLevelRadiation levelRadiation) {
        EntityEquipmentSlot[] var1 = EntityEquipmentSlot.values();
        int var2 = var1.length;

        for (EntityEquipmentSlot slot : var1) {
            if (slot.getSlotType() == Type.ARMOR) {
                ItemStack stack = living.getItemStackFromSlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof IHazmatLike)) {
                    return false;
                }

                IHazmatLike hazmat = (IHazmatLike) stack.getItem();
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
                    case HIGH:
                    case VERY_HIGH:
                        if (!(hazmat.getLevel() > 1)) {
                            return false;
                        }
                }
            }

        }

        return true;
    }

    default int getLevel() {
        return 1;
    }

    boolean addsProtection(EntityLivingBase var1, EntityEquipmentSlot var2, ItemStack var3);

    default boolean fullyProtects(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return false;
    }

}
