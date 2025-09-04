package com.denfop.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class FluidHandlerFix {
    public static IFluidHandlerItem getFluidHandler(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
    }

    public static boolean hasFluidHandler(ItemStack stack) {
        return getFluidHandler(stack) != null;
    }
}
