package com.denfop.items;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public interface ItemFluidCapabilities {
    IFluidHandlerItem initCapabilities(ItemStack stack);
}
