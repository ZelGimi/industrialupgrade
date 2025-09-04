package com.denfop.api.energy;

import com.denfop.api.otherenergies.common.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Map;

public class EnergyTile implements com.denfop.api.energy.interfaces.EnergyTile {

    @Override
    public List<InfoTile<com.denfop.api.energy.interfaces.EnergyTile>> getValidReceivers() {
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
    public void AddTile(final com.denfop.api.energy.interfaces.EnergyTile tile, final Direction dir) {

    }

    @Override
    public void RemoveTile(final com.denfop.api.energy.interfaces.EnergyTile tile, final Direction dir) {

    }

    @Override
    public Map<Direction, com.denfop.api.energy.interfaces.EnergyTile> getTiles() {
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
