package com.denfop.api.cool;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICoolNet {

    ICoolTile getTile(World var1, BlockPos var2);

    ICoolTile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & ICoolTile> void addTile(T var1);

    void removeTile(ICoolTile var1);


}
