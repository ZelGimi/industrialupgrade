package com.denfop.container;

import com.denfop.api.inv.VirtualSlot;
import com.denfop.items.energy.ItemStackMagnet;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class VirtualSlotItem implements VirtualSlot {

    private final ItemStackMagnet itemStacks;
    private final int length;


    public VirtualSlotItem(ItemStackMagnet itemStacks, int length) {
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
    public boolean accepts(final ItemStack stack, final int index) {
        return true;
    }

}
