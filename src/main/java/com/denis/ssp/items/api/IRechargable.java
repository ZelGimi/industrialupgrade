package com.Denfop.ssp.items.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IRechargable {
  int getMaxCharge(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
  
  EnumChargeDisplay showInHud(ItemStack paramItemStack, EntityLivingBase paramEntityLivingBase);
  
  public enum EnumChargeDisplay {
    NEVER, NORMAL, PERIODIC;
  }
}
