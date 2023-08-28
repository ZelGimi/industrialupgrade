package com.denfop.api.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;

public interface IMultiBlockItem {

    boolean hasUniqueRender(ItemStack var1);

    ModelResourceLocation getModelLocation(ItemStack var1);

}
