package com.denfop.tiles.panels.entity;

import cofh.redstoneflux.api.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TransferRFEnergy {

    private final IEnergyReceiver sink;
    private final TileEntity tile;
    private final EnumFacing facing;

    public TransferRFEnergy(TileEntity tile, IEnergyReceiver sink, EnumFacing facing) {
        this.tile = tile;
        this.sink = sink;
        this.facing = facing;
    }

    public IEnergyReceiver getSink() {
        return sink;
    }

    public TileEntity getTile() {
        return tile;
    }

    public EnumFacing getFacing() {
        return facing;
    }

}
