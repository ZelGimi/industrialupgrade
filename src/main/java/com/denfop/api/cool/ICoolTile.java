package com.denfop.api.cool;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public interface ICoolTile {

    BlockPos getPos();

    BlockEntity getTile();


    long getIdNetwork();

    void setId(long id);

    void AddCoolTile(ICoolTile tile, final Direction dir);

    void RemoveCoolTile(ICoolTile tile, final Direction dir);

    Map<Direction, ICoolTile> getCoolTiles();

    List<InfoTile<ICoolTile>> getCoolValidReceivers();

    int hashCode();

    int getHashCodeSource();

    void setHashCodeSource(int hashCode);
}
