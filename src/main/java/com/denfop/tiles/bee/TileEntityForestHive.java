package com.denfop.tiles.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.BeeInit;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHive;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityForestHive extends TileEntityHive {

    public TileEntityForestHive(BlockPos pos, BlockState state) {
        super(BeeInit.FOREST_BEE, BlockHive.forest_hive, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hive.getBlock(getTeBlock().getId());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHive.forest_hive;
    }

}
