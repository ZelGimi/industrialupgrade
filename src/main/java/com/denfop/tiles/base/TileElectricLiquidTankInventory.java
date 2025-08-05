package com.denfop.tiles.base;

import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Fluids;
import com.google.common.base.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;


public abstract class TileElectricLiquidTankInventory extends TileElectricMachine {

    public Fluids fluids;
    public Fluids.InternalFluidTank fluidTank;

    public TileElectricLiquidTankInventory(
            final double MaxEnergy, final int tier, final int tanksize,
            Predicate<Fluid> fluids_list, IMultiTileBlock block, BlockPos pos, BlockState state
    ) {
        super(MaxEnergy, tier, 1, block, pos, state);

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", tanksize * 1000, fluids_list);

    }

    public TileElectricLiquidTankInventory(
            final double MaxEnergy, final int tier, final int tanksize, IMultiTileBlock block, BlockPos pos, BlockState state
    ) {
        super(MaxEnergy, tier, 1, block, pos, state);

        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluidTank", tanksize * 1000);

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


}
