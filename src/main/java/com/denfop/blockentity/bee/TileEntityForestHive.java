package com.denfop.blockentity.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.BeeInit;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHiveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityForestHive extends TileEntityHive {

    public TileEntityForestHive(BlockPos pos, BlockState state) {
        super(BeeInit.FOREST_BEE, BlockHiveEntity.forest_hive, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hive.getBlock(getTeBlock().getId());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHiveEntity.forest_hive;
    }

}
