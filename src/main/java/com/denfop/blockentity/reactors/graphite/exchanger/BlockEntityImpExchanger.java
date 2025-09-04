package com.denfop.blockentity.reactors.graphite.exchanger;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpExchanger extends BlockEntityExchanger {

    public BlockEntityImpExchanger(BlockPos pos, BlockState state) {
        super(2, BlocksGraphiteReactors.graphite_imp_exchanger, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlocksGraphiteReactors.graphite_imp_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor.getBlock(getTeBlock());
    }

}
