package com.denfop.blockentity.reactors.heat.casing;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.heat.ICasing;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleCasing extends BlockEntityMultiBlockElement implements ICasing {

    public BlockEntitySimpleCasing(BlockPos pos, BlockState state) {
        super(BlockHeatReactorEntity.heat_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }


    @Override
    public int getBlockLevel() {
        return 0;
    }

}
