package com.denfop.api.exp;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEXPNet {

    IEXPTile getTile(World var1, BlockPos var2);

    IEXPTile getSubTile(World var1, BlockPos var2);

    <T extends TileEntity & IEXPTile> void addTile(T var1);

    void removeTile(IEXPTile var1);

}
