package com.denfop.api.sytem;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public interface ITile {

    BlockPos getPos();

    BlockEntity getTile();

    long getIdNetwork();

    void setId(long id);

    void AddTile(EnergyType energyType, ITile tile, final Direction dir);

    void RemoveTile(EnergyType energyType, ITile tile, final Direction dir);

    Map<Direction, ITile> getTiles(EnergyType energyType);

    List<InfoTile<ITile>> getValidReceivers(EnergyType energyType);

    int getHashCodeSource();

    void setHashCodeSource(int hashCode);
}
