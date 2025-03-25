package com.denfop.api.transport;

import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.sytem.InfoTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface ITransportTile<T, E> {

    E getHandler(EnumFacing facing);

    BlockPos getBlockPos();

    List<InfoTile<ITransportTile>> getValidReceivers();

    TileEntity getTileEntity();
    long getIdNetwork();

    void setId(long id);

    void AddTile(ITransportTile tile, final EnumFacing dir);

    void RemoveTile(ITransportTile tile, final EnumFacing dir);

    Map<EnumFacing, ITransportTile> getTiles();

    int hashCode();

    void setHashCodeSource(int hashCode);

    int getHashCodeSource();
}
