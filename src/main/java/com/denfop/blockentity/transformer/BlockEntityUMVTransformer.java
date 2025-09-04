package com.denfop.blockentity.transformer;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityTransformer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityUMVTransformer extends BlockEntityTransformer {

    public BlockEntityUMVTransformer(BlockPos pos, BlockState state) {
        super(5, BlockTransformerEntity.umv, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockTransformerEntity.umv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }
}
