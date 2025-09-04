package com.denfop.blockentity.reactors.graphite.casing;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.graphite.ICasing;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerCasing extends BlockEntityMultiBlockElement implements ICasing {

    public BlockEntityPerCasing(BlockPos pos, BlockState state) {
        super(BlocksGraphiteReactors.graphite_per_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlocksGraphiteReactors.graphite_per_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 3;
    }

}
