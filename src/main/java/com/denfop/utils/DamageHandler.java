package com.denfop.utils;


import com.denfop.IUCore;
import com.denfop.api.item.IDamageItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DamageHandler {

    public DamageHandler() {
    }

    public static int getDamage(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ItemStack.EMPTY.getItem()) {
            return 0;
        } else {
            return item instanceof IDamageItem ? ((IDamageItem) item).getCustomDamage(stack) : stack.getItemDamage();
        }
    }

    public static void setDamage(ItemStack stack, int damage) {
        Item item = stack.getItem();
        if (item != ItemStack.EMPTY.getItem()) {
            if (item instanceof IDamageItem) {
                ((IDamageItem) item).setCustomDamage(stack, damage);
            } else {
                stack.setItemDamage(damage);
            }

        }
    }

    public static int getMaxDamage(ItemStack stack) {
        Item item = stack.getItem();
        if (item == ItemStack.EMPTY.getItem()) {
            return 0;
        } else {
            return item instanceof IDamageItem
                    ? ((IDamageItem) item).getMaxCustomDamage(stack)
                    : stack.getMaxDamage();
        }
    }

    public static boolean damage(ItemStack stack, int damage, EntityLivingBase src) {
        Item item = stack.getItem();
        if (item == ItemStack.EMPTY.getItem()) {
            return false;
        } else if (item instanceof IDamageItem) {
            return ((IDamageItem) item).applyCustomDamage(stack, damage, src);
        } else if (src != null) {
            stack.damageItem(damage, src);
            return true;
        } else {
            return stack.attemptDamageItem(damage, IUCore.random, null);
        }
    }

}
