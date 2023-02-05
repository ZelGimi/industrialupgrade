package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyFESink extends BasicSink {

    IEnergyStorage energyfe;

    public EnergyFESink(final IEnergyStorage parent, TileEntity tile) {
        super(tile, 0, Integer.MAX_VALUE);
        this.energyfe = parent;

    }

    public double getDemandedEnergy() {
        return this.energyfe.receiveEnergy(Integer.MAX_VALUE, true) / 4D;
    }

    public double injectEnergy(EnumFacing directionFrom, double amt, double voltage) {
        this.energyfe.receiveEnergy((int) (amt * 4), false);
        return 0;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing side) {
        return this.energyfe.canReceive();
    }

}
