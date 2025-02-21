package com.denfop.api.sytem;


import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface ITile {

    BlockPos getBlockPos();

    TileEntity getTile();

    long getIdNetwork();

    void setId(long id);

    void AddTile(EnergyType energyType, ITile tile, final EnumFacing dir);

    void RemoveTile(EnergyType energyType, ITile tile, final EnumFacing dir);

    Map<EnumFacing, ITile> getTiles(EnergyType energyType);

    List<InfoTile<ITile>> getValidReceivers(EnergyType energyType);
    int hashCode();

    void setHashCodeSource(int hashCode);

    int getHashCodeSource();
}
