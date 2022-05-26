package com.denfop.api.upgrade;

import net.minecraft.item.ItemStack;

public class UpgradeModificator {

    public final ItemStack itemstack;

    public UpgradeModificator(ItemStack stack){
      this.itemstack = stack;
  }
  public boolean matches(ItemStack stack){
        return this.itemstack.isItemEqual(stack);
  }
}
