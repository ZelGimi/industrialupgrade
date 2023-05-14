package com.denfop.api.energy;


import appeng.api.config.Actionable;
import appeng.api.config.PowerUnits;
import appeng.tile.powersink.IExternalPowerSink;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class EnergyAESink extends BasicSink {

    IExternalPowerSink energyfe;

    public EnergyAESink(final IExternalPowerSink parent, TileEntity tile) {
        super(tile, 0, Integer.MAX_VALUE);
        this.energyfe = parent;

    }

    public double getDemandedEnergy() {
       return (this.energyfe.getExternalPowerDemand(PowerUnits.EU, this.energyfe.getAEMaxPower()));
    }

    public double injectEnergy(EnumFacing directionFrom, double amt, double voltage) {

        this.energyfe.injectExternalPower(PowerUnits.EU, amt, Actionable.MODULATE);
        return 0;
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing side) {
        return true;
    }

}
