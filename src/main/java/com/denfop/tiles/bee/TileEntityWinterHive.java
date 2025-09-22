package com.denfop.tiles.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.BeeInit;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHive;

public class TileEntityWinterHive extends TileEntityHive {

    public TileEntityWinterHive() {
        super(BeeInit.WINTER_BEE);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hive;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHive.winter_hive;
    }

}
