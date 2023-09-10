package com.denfop.tiles.panels.entity;

import com.denfop.api.energy.IEnergySink;
import net.minecraft.tileentity.TileEntity;

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
        this.sink.receiveEnergy(energy);
    }

}
