package com.denfop.blockentity.reactors.graphite.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerTank extends BlockEntityMainTank {

    public BlockEntityPerTank(BlockPos pos, BlockState state) {
        super(80000, BlocksGraphiteReactors.graphite_per_tank, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlocksGraphiteReactors.graphite_per_tank;
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
