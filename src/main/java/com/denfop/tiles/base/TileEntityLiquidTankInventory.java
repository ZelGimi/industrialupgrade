package com.denfop.tiles.base;

import ic2.core.block.TileEntityInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public abstract class TileEntityLiquidTankInventory extends TileEntityInventory implements IFluidHandler {

    public final FluidTank fluidTank;

    public TileEntityLiquidTankInventory(int tanksize) {
        this.fluidTank = new FluidTank(1000 * tanksize);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }

    public FluidStack getFluidStackfromTank() {
        return this.getFluidTank().getFluid();
    }

    public Fluid getFluidfromTank() {
        return this.getFluidStackfromTank().getFluid();
    }

    public int getTankAmount() {
        return this.getFluidTank().getFluidAmount();
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }

    public boolean needsFluid() {
        return this.getFluidTank().getFluidAmount() <= this.getFluidTank().getCapacity();
    }


    public abstract boolean canFill(Fluid var2);

    public abstract boolean canDrain(Fluid var2);

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return this.getFluidTank().getTankProperties();
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
        return this.canFill(resource.getFluid()) ? this.getFluidTank().fill(resource, doFill) : 0;

    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        if (resource != null && resource.isFluidEqual(this.getFluidTank().getFluid())) {
            return !this.canDrain(resource.getFluid()) ? null : this.getFluidTank().drain(resource.amount, doDrain);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
        return !this.canDrain(null) ? null : this.getFluidTank().drain(maxDrain, doDrain);

    }

}
