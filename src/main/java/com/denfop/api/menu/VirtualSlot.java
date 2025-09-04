package com.denfop.api.menu;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface VirtualSlot {

    ItemStack get(int index);

    boolean isFluid();

    List<FluidStack> getFluidStackList();

    void setFluidList(List<FluidStack> fluidStackList);

    int size();

    boolean canPlaceItem(int index, ItemStack stack);

}
