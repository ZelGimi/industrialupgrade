package com.denfop.items;

import com.denfop.api.inv.VirtualSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class UpgradeSlot implements VirtualSlot {

    private final ItemStackUpgradeModules itemStacks;

    public UpgradeSlot(ItemStackUpgradeModules itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    public ItemStack get(final int index) {
        return itemStacks.getStackInSlot(index);
    }

    @Override
    public boolean isFluid() {
        return true;
    }

    @Override
    public List<FluidStack> getFluidStackList() {
        return itemStacks.fluidStackList;
    }

    @Override
    public void setFluidList(final List<FluidStack> fluidStackList) {
        itemStacks.fluidStackList = fluidStackList;
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
