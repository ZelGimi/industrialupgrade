package com.denfop.api.crop;


import net.minecraft.world.item.ItemStack;

public interface CropItem {

    Crop getCrop(int meta, final ItemStack stack);

}
