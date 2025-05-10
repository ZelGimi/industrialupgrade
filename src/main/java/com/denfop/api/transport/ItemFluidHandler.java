package com.denfop.api.transport;


import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

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
    public int getTanks() {
        return fluidHandler.getTanks();
    }


    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return fluidHandler.getFluidInTank(tank);
    }


    @Override
    public int getTankCapacity(int tank) {
        return fluidHandler.getTankCapacity(tank);
    }


    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return fluidHandler.isFluidValid(tank, stack);
    }


    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fluidHandler.fill(resource, action);
    }


    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return fluidHandler.drain(resource, action);
    }


    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return fluidHandler.drain(maxDrain, action);
    }


    @Override
    public int getSlots() {
        return ItemHandler.getSlots();
    }


    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return ItemHandler.getStackInSlot(slot);
    }


    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return ItemHandler.insertItem(slot, stack, simulate);
    }


    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemHandler.extractItem(slot, amount, simulate);
    }


    @Override
    public int getSlotLimit(int slot) {
        return ItemHandler.getSlotLimit(slot);
    }


    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return ItemHandler.isItemValid(slot, stack);
    }
}
