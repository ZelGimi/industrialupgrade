package com.denfop.items;

import com.denfop.api.inv.VirtualSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class UpgradeSlot1 implements VirtualSlot {

    private final ItemStackUpgradeModules itemStacks;

    public UpgradeSlot1(ItemStackUpgradeModules itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    public ItemStack get(final int index) {
        return itemStacks.getStackInSlot(index);
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
        return itemStacks.getSizeInventory();
    }

    @Override
    public boolean accepts(final ItemStack stack, final int index) {
        return true;
    }

}
