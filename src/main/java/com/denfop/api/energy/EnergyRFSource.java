package com.denfop.api.energy;

import cofh.redstoneflux.api.IEnergyProvider;
import ic2.api.energy.tile.IEnergyAcceptor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class EnergyRFSource extends BasicSource {

    IEnergyProvider energyfe;

    public EnergyRFSource(final TileEntity parent) {
        super(parent, 0, Integer.MAX_VALUE);
        this.energyfe = (IEnergyProvider) parent;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return true;
    }

    public double getOfferedEnergy() {
        try {
            return Math.min(
                    energyfe.getEnergyStored(null),
                    energyfe.extractEnergy(null, energyfe.getEnergyStored(null), true)
            ) / 4D;
        }catch (Exception ignored){
            return 0;
        }
    }

    public void drawEnergy(double amount) {
        energyfe.extractEnergy(null, (int) (amount * 4), false);
    }

}
