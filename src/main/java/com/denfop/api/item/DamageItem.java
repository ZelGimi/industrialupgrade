package com.denfop.api.item;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface DamageItem {

    int getCustomDamage(ItemStack var1);

    int getMaxCustomDamage(ItemStack var1);

    void setCustomDamage(ItemStack var1, int var2);

    boolean applyCustomDamage(ItemStack var1, int var2, LivingEntity var3);

}
