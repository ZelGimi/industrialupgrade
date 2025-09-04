package com.denfop.containermenu;

import com.denfop.api.menu.VirtualSlot;
import com.denfop.items.bags.ItemStackBags;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class VirtualSlotItemBags implements VirtualSlot {

    private final ItemStackBags itemStacks;
    private final int length;


    public VirtualSlotItemBags(ItemStackBags itemStacks, int length) {
        this.itemStacks = itemStacks;
        this.length = length;
    }

    @Override
    public ItemStack get(final int index) {
        return itemStacks.list.get(index - length);
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
        return itemStacks.containerAdditionItem.listItem().size();
    }

    @Override
    public boolean canPlaceItem(final int index, final ItemStack stack) {
        return true;
    }

}
