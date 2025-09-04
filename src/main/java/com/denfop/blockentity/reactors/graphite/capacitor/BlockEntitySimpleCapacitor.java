package com.denfop.blockentity.reactors.graphite.capacitor;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleCapacitor extends BlockEntityCapacitor {

    public BlockEntitySimpleCapacitor(BlockPos pos, BlockState state) {
        super(0, BlocksGraphiteReactors.graphite_capacitor, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlocksGraphiteReactors.graphite_capacitor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor.getBlock(getTeBlock());
    }

}
