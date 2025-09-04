package com.denfop.blockentity.transformer;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityTransformer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTransformerLV extends BlockEntityTransformer {

    public BlockEntityTransformerLV(BlockPos pos, BlockState state) {
        super(1, BlockTransformerEntity.lv, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockTransformerEntity.lv;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }
}
