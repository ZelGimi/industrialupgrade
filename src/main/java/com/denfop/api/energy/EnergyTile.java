package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public class EnergyTile implements IEnergyTile {

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return null;
    }

    @Override
    public TileEntity getTileEntity() {
        return null;
    }

    @Override
    public BlockPos getBlockPos() {
        return null;
    }

    @Override
    public long getIdNetwork() {
        return 0;
    }

    @Override
    public void setId(final long id) {

    }

    @Override
    public void AddTile(final IEnergyTile tile, final EnumFacing dir) {

    }

    @Override
    public void RemoveTile(final IEnergyTile tile, final EnumFacing dir) {

    }

    @Override
    public Map<EnumFacing, IEnergyTile> getTiles() {
        return null;
    }

    @Override
    public int getHashCodeSource() {
        return 0;
    }

    @Override
    public void setHashCodeSource(final int hashCode) {

    }

}
