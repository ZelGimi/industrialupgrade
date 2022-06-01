package com.denfop.tiles.base;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Fluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public abstract class TileEntityLiquidTankInventory extends TileEntityInventory {

    public final FluidTank fluidTank;

    public TileEntityLiquidTankInventory(int tanksize) {
        Fluids fluids = this.addComponent(new Fluids(this));
        this.fluidTank = fluids.addTank("fluidTank", tanksize * 1000);

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


}
