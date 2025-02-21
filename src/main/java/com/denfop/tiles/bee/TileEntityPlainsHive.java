package com.denfop.tiles.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.BeeInit;
import com.denfop.api.bee.IBee;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHive;

public class TileEntityPlainsHive extends TileEntityHive{

    public TileEntityPlainsHive() {
        super(BeeInit.PLAINS_BEE);
    }
    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hive;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHive.plains_hive;
    }
}
