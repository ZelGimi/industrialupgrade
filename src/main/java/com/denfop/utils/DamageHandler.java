package com.denfop.utils;


import com.denfop.api.item.DamageItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DamageHandler {

    public DamageHandler() {
    }

    public static int getDamage(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ItemStack.EMPTY.getItem()) {
            return 0;
        } else {
            return item instanceof DamageItem ? stack.getDamageValue() : stack.getDamageValue();
        }
    }

    public static void setDamage(ItemStack stack, int damage) {
        Item item = stack.getItem();
        if (item != ItemStack.EMPTY.getItem()) {
            if (item instanceof DamageItem) {
                ((DamageItem) item).setCustomDamage(stack, damage);
            } else {
                stack.setDamageValue(damage);
            }

        }
    }

    public static int getMaxDamage(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ItemStack.EMPTY.getItem()) {
            return 0;
        } else {
            return item instanceof DamageItem
                    ? ((DamageItem) item).getMaxCustomDamage(stack)
                    : stack.getMaxDamage();
        }
    }

    public static boolean damage(ItemStack stack, int damage, LivingEntity src) {
        Item item = stack.getItem();
        if (item == ItemStack.EMPTY.getItem()) {
            return false;
        } else if (item instanceof DamageItem) {
            return ((DamageItem) item).applyCustomDamage(stack, damage, src);
        } else
            stack.hurtAndBreak(damage, src, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return false;
    }

}
