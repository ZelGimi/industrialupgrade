package com.denfop.api.se;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISENet {

    ISETile getTile(World var1, BlockPos var2);

    ISETile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & ISETile> void addTile(T var1);

    void removeTile(ISETile var1);

}
