package com.denfop.api.agriculture;


import net.minecraft.world.item.ItemStack;

public interface ICropItem {

    ICrop getCrop(int meta, final ItemStack stack);

}
