package com.denfop.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ItemFluidHandler implements IItemHandler, IFluidHandler {

    private final IItemHandler ItemHandler;
    private final IFluidHandler fluidHandler;

    public ItemFluidHandler(IItemHandler iItemHandler, IFluidHandler fluidHandler) {
        this.fluidHandler = fluidHandler;
        this.ItemHandler = iItemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public IItemHandler getItemHandler() {
        return ItemHandler;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return fluidHandler.getTankProperties();
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
        return fluidHandler.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        return fluidHandler.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        return fluidHandler.drain(maxDrain, doDrain);
    }

    @Override
    public int getSlots() {
        return this.ItemHandler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(final int slot) {
        return this.ItemHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
        return this.ItemHandler.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
        return this.ItemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(final int slot) {
        return this.ItemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
        return this.ItemHandler.isItemValid(slot, stack);
    }

}
