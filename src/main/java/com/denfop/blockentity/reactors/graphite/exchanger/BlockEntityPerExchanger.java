package com.denfop.blockentity.reactors.graphite.exchanger;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerExchanger extends BlockEntityExchanger {

    public BlockEntityPerExchanger(BlockPos pos, BlockState state) {
        super(3, BlocksGraphiteReactors.graphite_per_exchanger, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlocksGraphiteReactors.graphite_per_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor.getBlock(getTeBlock());
    }

}
