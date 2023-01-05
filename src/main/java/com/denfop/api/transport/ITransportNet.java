package com.denfop.api.transport;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITransportNet {

    ITransportTile getTile(World var1, BlockPos var2);

    ITransportTile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & ITransportTile> void addTile(T var1);

    void removeTile(ITransportTile var1);

}
