package com.denfop.componets;

import com.denfop.api.sytem.EnergyType;
import com.denfop.blocks.FluidName;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ComponentBioFuelEnergy extends ComponentBaseEnergy {

    FluidTank fluidTank;

    public ComponentBioFuelEnergy(EnergyType type, TileEntityInventory parent, double capacity) {
        this(type, parent, capacity, Collections.emptySet(), Collections.emptySet(), 1);
    }

    public ComponentBioFuelEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int tier
    ) {
        this(type, parent, capacity, sinkDirections, sourceDirections, tier, tier, false);
    }

    public ComponentBioFuelEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            Set<Direction> sinkDirections,
            Set<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier);
    }

    public ComponentBioFuelEnergy(
            EnergyType type, TileEntityInventory parent,
            double capacity,
            List<Direction> sinkDirections,
            List<Direction> sourceDirections,
            int sinkTier,
            int sourceTier,
            boolean fullEnergy
    ) {
        super(type, parent, capacity, sinkDirections, sourceDirections, sinkTier, sourceTier);
    }

    public static ComponentBioFuelEnergy asBasicSink(TileEntityInventory parent, double capacity) {
        return asBasicSink(parent, capacity, 1);
    }
    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (fluidTank.getFluid().getAmount() != this.buffer.storage && (this.buffer.storage > fluidTank.getFluid().getAmount())){
            fluidTank.fill(new FluidStack(FluidName.fluidbiomass.getInstance().get(),  (int)this.buffer.storage-fluidTank.getFluid().getAmount()), IFluidHandler.FluidAction.EXECUTE);
        }
        if (fluidTank.getFluid().getAmount() != this.buffer.storage && (this.buffer.storage < fluidTank.getFluid().getAmount())){
            fluidTank.drain(fluidTank.getFluid().getAmount()- (int)this.buffer.storage, IFluidHandler.FluidAction.EXECUTE);
        }
    }
    public static ComponentBioFuelEnergy asBasicSink(TileEntityInventory parent, double capacity, int tier) {
        return new ComponentBioFuelEnergy(EnergyType.BIOFUEL, parent, capacity, ModUtils.allFacings, Collections.emptySet(), tier);
    }

    public static ComponentBioFuelEnergy asBasicSource(TileEntityInventory parent, double capacity) {
        return asBasicSource(parent, capacity, 1);
    }

    public static ComponentBioFuelEnergy asBasicSource(TileEntityInventory parent, double capacity, int tier) {
        return new ComponentBioFuelEnergy(EnergyType.BIOFUEL, parent, capacity, Collections.emptySet(), ModUtils.allFacings, tier);
    }

    public void setFluidTank(final FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public double addEnergy(final double amount) {
        super.addEnergy(amount);
        if (fluidTank.getFluid().isEmpty() && amount >= 1) {
            fluidTank.fill(new FluidStack(FluidName.fluidbiomass.getInstance().get(), (int) this.buffer.storage), IFluidHandler.FluidAction.EXECUTE);
        } else if (!fluidTank.getFluid().isEmpty()) {
            fluidTank.getFluid().setAmount((int) this.buffer.storage);
        }
        return amount;
    }

    @Override
    public boolean isClient() {
        return true;
    }


    @Override
    public boolean useEnergy(final double amount) {
        super.useEnergy(amount);
        if (!fluidTank.getFluid().isEmpty()) {
            fluidTank.getFluid().setAmount((int) this.buffer.storage);
            if (fluidTank.getFluid().getAmount() == 0) {
                fluidTank.setFluid(FluidStack.EMPTY);
            }
        }
        return true;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }
}
