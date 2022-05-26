
package com.denfop.api.cooling;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

public interface ICoolNet {

    ICoolTile getTile(World var1, BlockPos var2);

    ICoolTile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & ICoolTile> void addTile(T var1);

    void removeTile(ICoolTile var1);

    NodeCoolStats getNodeStats(ICoolTile var1,World world);

    List<ICoolSink> getListHeatInChunk(Chunk chunk);

    void transferTemperatureWireless(ICoolWirelessSource source);

}
