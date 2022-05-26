
package com.denfop.api.heat;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface IHeatNet {

    IHeatTile getTile(World var1, BlockPos var2);

    IHeatTile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & IHeatTile> void addTile(T var1);

    void removeTile(IHeatTile var1);

    NodeHeatStats getNodeStats(IHeatTile var1);

    List<IHeatTile> getListHeatInChunk(Chunk chunk);

    void transferTemperatureWireless(IWirelessSource source);

}
