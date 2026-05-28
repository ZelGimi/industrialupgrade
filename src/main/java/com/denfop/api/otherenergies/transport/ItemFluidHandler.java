package com.denfop.api.otherenergies.transport;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class ItemFluidHandler implements IItemHandler, IFluidHandler {

    private final IItemHandler itemHandler;
    private final IFluidHandler fluidHandler;

    public ItemFluidHandler(IItemHandler itemHandler, IFluidHandler fluidHandler) {
        this.fluidHandler = fluidHandler;
        this.itemHandler = itemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public int getTanks() {
        return fluidHandler != null ? fluidHandler.getTanks() : 0;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return fluidHandler != null ? fluidHandler.getFluidInTank(tank) : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidHandler != null ? fluidHandler.getTankCapacity(tank) : 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return fluidHandler != null && fluidHandler.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fluidHandler != null ? fluidHandler.fill(resource, action) : 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return fluidHandler != null ? fluidHandler.drain(resource, action) : FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return fluidHandler != null ? fluidHandler.drain(maxDrain, action) : FluidStack.EMPTY;
    }

    @Override
    public int getSlots() {
        return itemHandler != null ? itemHandler.getSlots() : 0;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return itemHandler != null ? itemHandler.getStackInSlot(slot) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return itemHandler != null ? itemHandler.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemHandler != null ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler != null ? itemHandler.getSlotLimit(slot) : 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return itemHandler != null && itemHandler.isItemValid(slot, stack);
    }
}