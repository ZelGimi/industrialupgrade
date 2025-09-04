package com.denfop.blockentity.transformer;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityTransformer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityUHVTransformer extends BlockEntityTransformer {

    public BlockEntityUHVTransformer(BlockPos pos, BlockState state) {
        super(6, BlockTransformerEntity.uhv, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockTransformerEntity.uhv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }
}
