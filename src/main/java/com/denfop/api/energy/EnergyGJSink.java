package com.denfop.api.energy;


import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EnergyGJSink extends BasicSink {


    private final TileBaseUniversalElectrical parent;

    public EnergyGJSink(final TileBaseUniversalElectrical parent) {
        super(parent, 0, Integer.MAX_VALUE);
        this.parent = parent;
    }

    public double getDemandedEnergy() {
        return this.parent.storage.receiveEnergyGC(Integer.MAX_VALUE, true) / 6.557D;
    }

    public double injectEnergy(EnumFacing directionFrom, double amt, double voltage) {
        this.parent.storage.receiveEnergyGC((float) (amt * 6.557), false);
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
        return parent.getElectricalInputDirections().contains(side);
    }

}
