package com.denfop.api.item.armor;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface VolcanoArmor {

    static boolean hasCompleteHazmat(LivingEntity living) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = living.getItemBySlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof VolcanoArmor)) {
                    return false;
                }

                VolcanoArmor hazmat = (VolcanoArmor) stack.getItem();
                if (!hazmat.addsProtection(living, slot, stack)) {
                    return false;
                }

                if (hazmat.fullyProtects(living, slot, stack)) {
                    return true;
                }
            }
        }
        return true;
    }


    boolean addsProtection(LivingEntity entity, EquipmentSlot slot, ItemStack stack);


    default boolean fullyProtects(LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
        return false;
    }
}

