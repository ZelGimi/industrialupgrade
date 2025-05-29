package com.denfop.tiles.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.BeeInit;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySwampHive extends TileEntityHive {

    public TileEntitySwampHive(BlockPos pos, BlockState state) {
        super(BeeInit.SWAMP_BEE,BlockHive.swamp_hive, pos ,state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hive.getBlock(getTeBlock().getId());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHive.swamp_hive;
    }

}
