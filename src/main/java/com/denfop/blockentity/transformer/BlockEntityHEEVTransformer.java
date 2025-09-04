package com.denfop.blockentity.transformer;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityTransformer;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTransformerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHEEVTransformer extends BlockEntityTransformer {

    public BlockEntityHEEVTransformer(BlockPos pos, BlockState state) {
        super(11, BlockTransformerEntity.heev, pos, state);
    }

    public MultiBlockEntity getTeBlock() {
        return BlockTransformerEntity.heev;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tranformer.getBlock(getTeBlock().getId());
    }

}
