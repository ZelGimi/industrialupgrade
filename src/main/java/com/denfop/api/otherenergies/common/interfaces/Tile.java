package com.denfop.api.otherenergies.common.interfaces;


import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.api.otherenergies.common.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public interface Tile {

    BlockPos getPos();

    BlockEntity getTile();

    long getIdNetwork();

    void setId(long id);

    void AddTile(EnergyType energyType, Tile tile, final Direction dir);

    void RemoveTile(EnergyType energyType, Tile tile, final Direction dir);

    Map<Direction, Tile> getTiles(EnergyType energyType);

    List<InfoTile<Tile>> getValidReceivers(EnergyType energyType);

    int getHashCodeSource();

    void setHashCodeSource(int hashCode);
}
