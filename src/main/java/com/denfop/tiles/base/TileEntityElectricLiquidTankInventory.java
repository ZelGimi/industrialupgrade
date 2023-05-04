package com.denfop.tiles.base;

import com.denfop.componets.Fluids;
import com.google.common.base.Predicate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public abstract class TileEntityElectricLiquidTankInventory extends TileEntityElectricMachine implements IFluidHandler {

    public Fluids fluids;
    public Fluids.InternalFluidTank fluidTank;

    public TileEntityElectricLiquidTankInventory(
            final double MaxEnergy, final int tier, final int tanksize,
            Predicate<Fluid> fluids_list
    ) {
        super(MaxEnergy, tier, 1);

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", tanksize * 1000, fluids_list);

    }

    public TileEntityElectricLiquidTankInventory(
            final double MaxEnergy, final int tier, final int tanksize
    ) {
        super(MaxEnergy, tier, 1);

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", tanksize * 1000);

    }


    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        if (this.energy.canUseEnergy(amount)) {
            this.energy.useEnergy(amount);
            return true;
        }
        return false;
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }


    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
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
