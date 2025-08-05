package com.denfop.utils;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

public class FluidHandlerFix {
    public static IFluidHandlerItem getFluidHandler(ItemStack stack) {
        return stack.getCapability(Capabilities.FluidHandler.ITEM);
    }

    public static boolean hasFluidHandler(ItemStack stack) {
        return getFluidHandler(stack) != null;
    }
}
