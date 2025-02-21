package com.denfop.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemStack;

public interface IVolcanoArmor {


    static boolean hasCompleteHazmat(EntityLivingBase living) {
        EntityEquipmentSlot[] var1 = EntityEquipmentSlot.values();
        int var2 = var1.length;

        for (EntityEquipmentSlot slot : var1) {
            if (slot.getSlotType() == Type.ARMOR) {
                ItemStack stack = living.getItemStackFromSlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof IVolcanoArmor)) {
                    return false;
                }

                IVolcanoArmor hazmat = (IVolcanoArmor) stack.getItem();
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

    boolean addsProtection(EntityLivingBase var1, EntityEquipmentSlot var2, ItemStack var3);

    default boolean fullyProtects(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return false;
    }

}
