package com.denfop.api.cool;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface ICoolTile {

    BlockPos getBlockPos();

    TileEntity getTile();


    long getIdNetwork();

    void setId(long id);

    void AddCoolTile( ICoolTile tile, final EnumFacing dir);

    void RemoveCoolTile(ICoolTile tile, final EnumFacing dir);

    Map<EnumFacing, ICoolTile> getCoolTiles();

    List<InfoTile<ICoolTile>> getCoolValidReceivers();
    int hashCode();

    void setHashCodeSource(int hashCode);

    int getHashCodeSource();
}
