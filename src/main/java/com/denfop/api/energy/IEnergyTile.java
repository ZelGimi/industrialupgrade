package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Map;

public interface IEnergyTile {
    List<InfoTile<IEnergyTile>> getValidReceivers();

    BlockPos getPos();

    long getIdNetwork();

    void setId(long id);

    void AddTile(IEnergyTile tile, final Direction dir);

    void RemoveTile(IEnergyTile tile, final Direction dir);

    Map<Direction, IEnergyTile> getTiles();

    int getHashCodeSource();

    void setHashCodeSource(int hashCode);


    default boolean isRemovedTile() {
        return false;
    }

    ;
}
