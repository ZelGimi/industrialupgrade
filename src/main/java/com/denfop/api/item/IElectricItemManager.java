package com.denfop.api.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IElectricItemManager {

    double charge(ItemStack var1, double var2, int var4, boolean var5, boolean var6);

    double discharge(ItemStack var1, double var2, int var4, boolean var5, boolean var6, boolean var7);

    double getCharge(ItemStack var1);

    double getMaxCharge(ItemStack var1);

    boolean canUse(ItemStack var1, double var2);

    boolean use(ItemStack var1, double var2, EntityLivingBase var4);


    String getToolTip(ItemStack var1);

    int getTier(ItemStack var1);

}
