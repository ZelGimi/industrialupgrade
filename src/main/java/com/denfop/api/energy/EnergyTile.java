package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public class EnergyTile implements IEnergyTile {

    @Override
    public List<InfoTile<IEnergyTile>> getValidReceivers() {
        return null;
    }

    @Override
    public BlockEntity getTileEntity() {
        return null;
    }

    @Override
    public BlockPos getPos() {
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
    public void AddTile(final IEnergyTile tile, final Direction dir) {

    }

    @Override
    public void RemoveTile(final IEnergyTile tile, final Direction dir) {

    }

    @Override
    public Map<Direction, IEnergyTile> getTiles() {
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
