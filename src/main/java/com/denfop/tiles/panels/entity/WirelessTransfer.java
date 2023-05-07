package com.denfop.tiles.panels.entity;

import com.denfop.api.energy.IAdvEnergySink;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class WirelessTransfer {

    private final IAdvEnergySink sink;
    private final TileEntity tile;

    public WirelessTransfer(TileEntity tile, IAdvEnergySink sink) {
        this.tile = tile;
        this.sink = sink;
    }

    public IAdvEnergySink getSink() {
        return sink;
    }

    public TileEntity getTile() {
        return tile;
    }

    public void work(double energy) {
        this.sink.injectEnergy(EnumFacing.DOWN, energy, 0);
    }

}
