package com.denfop.blockentity.smeltery;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockSmelteryEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySmelteryCasing extends BlockEntityMultiBlockElement implements ICasing {

    public BlockEntitySmelteryCasing(BlockPos pos, BlockState state) {
        super(BlockSmelteryEntity.smeltery_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockSmelteryEntity.smeltery_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }

}
