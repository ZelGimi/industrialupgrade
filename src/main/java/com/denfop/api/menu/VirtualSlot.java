package com.denfop.api.menu;

import com.denfop.api.storage.autocrafting.SameStack;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface VirtualSlot {

    ItemStack get(int index);

    boolean isFluid();

    List<FluidStack> getFluidStackList();

    void setFluidList(List<FluidStack> fluidStackList);

    int size();

    boolean canPlaceVirtualItem(int index, ItemStack stack);

    default void setFluid(int index, SameStack stack) {
    }
}
