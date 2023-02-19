package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyFESinkSource extends BasicSinkSource {

    IEnergyStorage input;

    public EnergyFESinkSource(final IEnergyStorage input, TileEntity tile) {
        super(tile, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.input = input;

    }

    public double getDemandedEnergy() {
        return this.input.receiveEnergy(Integer.MAX_VALUE, true) / 4D;
    }

    public double injectEnergy(EnumFacing directionFrom, double amt, double voltage) {
        this.input.receiveEnergy((int) (amt * 4), false);
        return 0;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing side) {
        if(tile == null)
            tile = this.world.getTileEntity(this.pos);

        IEnergyStorage cap = tile.getCapability(CapabilityEnergy.ENERGY, side);
        if (cap == null) {
            return false;
        } else {
            return cap.canReceive();
        }
    }

    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing direction) {
        if(tile == null)
            tile = this.world.getTileEntity(this.pos);
        final IEnergyStorage cap = tile.getCapability(CapabilityEnergy.ENERGY, direction);
        if (cap == null) {
            return false;
        } else {
            return cap.canExtract();
        }
    }

    public double getOfferedEnergy() {
        return Math.min(input.getEnergyStored(), input.extractEnergy(input.getEnergyStored(), true)) / 4D;
    }

    public void drawEnergy(double amount) {
        input.extractEnergy((int) (amount * 4), false);
    }

}
