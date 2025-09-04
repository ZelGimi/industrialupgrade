package com.denfop.blockentity.bee;

import com.denfop.IUItem;
import com.denfop.api.bee.BeeInit;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHiveEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntitySwampHive extends TileEntityHive {

    public TileEntitySwampHive(BlockPos pos, BlockState state) {
        super(BeeInit.SWAMP_BEE, BlockHiveEntity.swamp_hive, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hive.getBlock(getTeBlock().getId());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHiveEntity.swamp_hive;
    }

}
