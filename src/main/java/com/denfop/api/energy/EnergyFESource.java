package com.denfop.api.energy;

import ic2.api.energy.prefab.BasicSource;
import ic2.api.energy.tile.IEnergyAcceptor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyFESource extends BasicSource {

    IEnergyStorage energyfe;

    public EnergyFESource(final IEnergyStorage parent, TileEntity tile) {
        super(tile, 0, Integer.MAX_VALUE);
        this.energyfe = parent;
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        return true;
    }

    public double getOfferedEnergy() {
        return Math.min(energyfe.getEnergyStored(), energyfe.extractEnergy(energyfe.getEnergyStored(), true)) / 4D;
    }

    public void drawEnergy(double amount) {
        energyfe.extractEnergy((int) (amount * 4), false);
    }

}
