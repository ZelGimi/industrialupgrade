package com.denfop.api.heat;

import com.denfop.api.cool.ICoolTile;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface IHeatTile {

    BlockPos getBlockPos();

    TileEntity getTile();

    long getIdNetwork();

    void setId(long id);

    void AddHeatTile(IHeatTile tile, final EnumFacing dir);

    void RemoveHeatTile(IHeatTile tile, final EnumFacing dir);

    Map<EnumFacing, IHeatTile> getHeatTiles();

    List<InfoTile<IHeatTile>> getHeatValidReceivers();
    int hashCode();

    void setHashCodeSource(int hashCode);

    int getHashCodeSource();
}
