package com.denfop.api.energy.interfaces;

import com.denfop.api.otherenergies.common.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;
import java.util.Map;

public interface EnergyTile {
    List<InfoTile<EnergyTile>> getValidReceivers();

    BlockPos getPos();

    long getIdNetwork();

    void setId(long id);

    void AddTile(EnergyTile tile, final Direction dir);

    void RemoveTile(EnergyTile tile, final Direction dir);

    Map<Direction, EnergyTile> getTiles();

    int getHashCodeSource();

    void setHashCodeSource(int hashCode);


    default boolean isRemovedTile() {
        return false;
    }

    ;
}
