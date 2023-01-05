package com.denfop.tiles.panels.entity;

import ic2.api.energy.tile.IEnergySink;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class WirelessTransfer {

    private final IEnergySink sink;
    private final TileEntity tile;

    public WirelessTransfer(TileEntity tile, IEnergySink sink) {
        this.tile = tile;
        this.sink = sink;
    }

    public IEnergySink getSink() {
        return sink;
    }

    public TileEntity getTile() {
        return tile;
    }

    public void work(double energy) {
        this.sink.injectEnergy(EnumFacing.DOWN, energy, 0);
    }

}
