package com.denfop.api.otherenergies.heat;

import com.denfop.api.otherenergies.common.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public interface IHeatTile {

    BlockPos getPos();

    BlockEntity getTile();

    long getIdNetwork();

    void setId(long id);

    void AddHeatTile(IHeatTile tile, final Direction dir);

    void RemoveHeatTile(IHeatTile tile, final Direction dir);

    Map<Direction, IHeatTile> getHeatTiles();

    List<InfoTile<IHeatTile>> getHeatValidReceivers();

    int hashCode();

    int getHashCodeSource();

    void setHashCodeSource(int hashCode);
}
