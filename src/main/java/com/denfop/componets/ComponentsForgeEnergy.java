package com.denfop.componets;

import com.denfop.api.energy.IAdvEnergySink;
import com.denfop.api.energy.IAdvEnergySource;
import com.denfop.api.energy.IAdvEnergyTile;
import net.minecraftforge.energy.IEnergyStorage;

public class ComponentsForgeEnergy implements IEnergyStorage {

    private final AdvEnergy advEnergy;
    private final boolean isSink;
    private final boolean isSource;
    private final IAdvEnergyTile tile;

    public ComponentsForgeEnergy(AdvEnergy inventory, boolean isSink, boolean isSource, IAdvEnergyTile tile) {
        this.advEnergy = inventory;
        this.isSink = isSink;
        this.isSource = isSource;
        this.tile = tile;
    }

    @Override
    public int receiveEnergy(final int maxReceive, final boolean simulate) {
        double demand = ((IAdvEnergySink) tile).getDemandedEnergy() * 4;
        demand = Math.max(demand, 0);
        demand = Math.min(demand, maxReceive);
        if (!simulate) {
            ((IAdvEnergySink) tile).injectEnergy(null, demand / 4, 0);
        }

        if (demand > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE - 1;
        } else {
            return (int) demand;
        }
    }

    @Override
    public int extractEnergy(final int maxExtract, final boolean simulate) {
        double offeredEnergy = ((IAdvEnergySource) tile).getOfferedEnergy() * 4;
        offeredEnergy = Math.max(offeredEnergy, 0);
        offeredEnergy = Math.min(offeredEnergy, maxExtract);
        if (!simulate) {
            ((IAdvEnergySource) tile).drawEnergy(offeredEnergy / 4);
        }
        if (offeredEnergy > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE - 1;
        } else {
            return (int) offeredEnergy;
        }
    }

    @Override
    public int getEnergyStored() {
        if (advEnergy.getEnergy() * 4 > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE - 1;
        }
        return (int) advEnergy.getEnergy() * 4;
    }

    @Override
    public int getMaxEnergyStored() {
        if (advEnergy.getCapacity() * 4 > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE - 1;
        }
        return (int) advEnergy.getCapacity() * 4;
    }

    @Override
    public boolean canExtract() {
        return isSource;
    }

    @Override
    public boolean canReceive() {
        return isSink;
    }

}
