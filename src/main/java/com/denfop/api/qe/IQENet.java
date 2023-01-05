package com.denfop.api.qe;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IQENet {

    IQETile getTile(World var1, BlockPos var2);

    IQETile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & IQETile> void addTile(T var1);

    void removeTile(IQETile var1);

    NodeQEStats getNodeStats(IQETile var1, World world);


}
