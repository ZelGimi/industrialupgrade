package com.denfop.blockentity.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleSocket extends BlockEntityMainSocket {

    public BlockEntitySimpleSocket(BlockPos pos, BlockState state) {
        super(10000, BlockHeatReactorEntity.heat_socket, pos, state);
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
