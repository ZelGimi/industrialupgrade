package com.denfop.items;

import com.denfop.api.menu.VirtualSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class UpgradeSlot implements VirtualSlot {

    private final ItemStackUpgradeModules itemStacks;

    public UpgradeSlot(ItemStackUpgradeModules itemStacks) {
        this.itemStacks = itemStacks;
    }

    @Override
    public ItemStack get(final int index) {
        return itemStacks.getItem(index);
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
        return itemStacks.getContainerSize();
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        return true;
    }

}
