package com.denfop.api.inv;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface VirtualSlot {

    ItemStack get(int index);

    boolean isFluid();

    List<FluidStack> getFluidStackList();

    void setFluidList(List<FluidStack> fluidStackList);

    int size();

    boolean accepts(ItemStack stack, int index);

}
