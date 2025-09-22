package com.denfop.api.energy;

import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface IEnergyTile {

    List<InfoTile<IEnergyTile>> getValidReceivers();

    TileEntity getTileEntity();

    BlockPos getBlockPos();

    long getIdNetwork();

    void setId(long id);

    void AddTile(IEnergyTile tile, final EnumFacing dir);

    void RemoveTile(IEnergyTile tile, final EnumFacing dir);

    Map<EnumFacing, IEnergyTile> getTiles();


    int getHashCodeSource();

    void setHashCodeSource(int hashCode);

}
