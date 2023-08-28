package com.denfop.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IDamageItem {

    int getCustomDamage(ItemStack var1);

    int getMaxCustomDamage(ItemStack var1);

    void setCustomDamage(ItemStack var1, int var2);

    boolean applyCustomDamage(ItemStack var1, int var2, EntityLivingBase var3);

}
