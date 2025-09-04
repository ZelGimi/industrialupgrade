package com.denfop.blockentity.reactors.graphite.exchanger;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvExchanger extends BlockEntityExchanger {

    public BlockEntityAdvExchanger(BlockPos pos, BlockState state) {
        super(1, BlocksGraphiteReactors.graphite_adv_exchanger, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlocksGraphiteReactors.graphite_adv_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor.getBlock(getTeBlock());
    }

}
