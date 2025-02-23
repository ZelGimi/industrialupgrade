package com.denfop.api.agriculture;

import net.minecraft.item.ItemStack;

public interface ICropItem {

    ICrop getCrop(int meta, final ItemStack stack);

}
