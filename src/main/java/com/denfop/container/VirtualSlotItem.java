package com.denfop.container;

import com.denfop.api.inv.VirtualSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class VirtualSlotItem implements VirtualSlot {

    private final ItemStack[] itemStacks;
    private final int length;

    public VirtualSlotItem(ItemStack[] itemStacks, int length) {
        this.itemStacks = itemStacks;
        this.length = length;
    }

    @Override
    public ItemStack get(final int index) {
        return itemStacks[index - length];
    }

    @Override
    public boolean isFluid() {
        return false;
    }

    @Override
    public List<FluidStack> getFluidStackList() {
        return Collections.emptyList();
    }

    @Override
    public void setFluidList(final List<FluidStack> fluidStackList) {

    }

    @Override
    public int size() {
        return itemStacks.length;
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return true;
    }

}
