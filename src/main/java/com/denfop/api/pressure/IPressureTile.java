package com.denfop.api.pressure;


import com.denfop.api.sytem.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public interface IPressureTile {

    BlockPos getPos();

    BlockEntity getTile();
    long getIdNetwork();

    void setId(long id);

    void AddTile(IPressureTile tile, final Direction dir);

    void RemoveTile(IPressureTile tile, final Direction dir);

    Map<Direction, IPressureTile> getTiles();

    List<InfoTile<IPressureTile>> getValidReceivers();

    int getHashCodeSource();

    void setHashCodeSource(int hashCode);

}
