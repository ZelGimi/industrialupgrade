package com.denfop.api.energy;

import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class EnergyTileInfo {

    private final BlockPos pos;
    private final IEnergyTile energyTile;
    private TileEntity tile = null;

    public EnergyTileInfo(IEnergyTile energyTile, BlockPos pos) {
        this.pos = pos;
        this.energyTile = energyTile;
    }

    public TileEntity getTile() {
        return tile;
    }

    public void setTile(final TileEntity tile) {
        this.tile = tile;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IEnergyTile getEnergyTile() {
        return energyTile;
    }

    @Override
    public boolean equals(final Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnergyTileInfo that = (EnergyTileInfo) o;
        if (this.energyTile == null) {

            if (this.pos != null) {
                     return that.pos.getX() == this.getPos().getX()
                        && that.pos.getY() == this.getPos().getY() &&
                        that.pos.getZ() == this.getPos().getZ();
            } else {
                return false;
            }
        } else {
            return that.energyTile == this.energyTile;
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, energyTile);
    }

}
