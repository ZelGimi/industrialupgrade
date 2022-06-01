package com.denfop.api.os;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IOSNet {

    IOSTile getTile(World var1, BlockPos var2);

    IOSTile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & IOSTile> void addTile(T var1);

    void removeTile(IOSTile var1);

    NodeOSStats getNodeStats(IOSTile var1, World world);


}
