package com.denfop.api.crop;


import net.minecraft.world.item.ItemStack;

public interface ICropItem {

    ICrop getCrop(int meta, final ItemStack stack);

}
