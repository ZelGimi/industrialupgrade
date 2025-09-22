package com.denfop.api.inv;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface VirtualSlot {

    ItemStack get(int index);

    boolean isFluid();

    List<FluidStack> getFluidStackList();

    void setFluidList(List<FluidStack> fluidStackList);

    int size();

    boolean accepts(int index, ItemStack stack);

}
