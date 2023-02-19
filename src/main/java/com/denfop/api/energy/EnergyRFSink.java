package com.denfop.api.energy;

import cofh.redstoneflux.api.IEnergyReceiver;
import ic2.api.energy.tile.IEnergyEmitter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergyRFSink extends BasicSink {

    private final TileEntity parent;
    IEnergyReceiver energyfe;

    public EnergyRFSink(final TileEntity parent) {
        super(parent, 0, Integer.MAX_VALUE);
        this.energyfe = (IEnergyReceiver) parent;
        this.parent = parent;
    }

    public double getDemandedEnergy() {
        return this.energyfe.receiveEnergy(null, Integer.MAX_VALUE, true) / 4D;
    }

    public double injectEnergy(EnumFacing directionFrom, double amt, double voltage) {
        this.energyfe.receiveEnergy(directionFrom, (int) (amt * 4), false);
        return 0;
    }

    public World getWorldObj() {
        if (this.world == null) {
            this.initLocation();
        }

        return this.world;
    }

    public BlockPos getPosition() {
        if (this.pos == null) {
            this.initLocation();
        }

        return this.pos;
    }

    private void initLocation() {
        TileEntity provider = this.parent;
        this.world = provider.getWorld();
        this.pos = provider.getPos();
    }

    public boolean acceptsEnergyFrom(IEnergyEmitter iEnergyEmitter, EnumFacing side) {
        return true;
    }

}
