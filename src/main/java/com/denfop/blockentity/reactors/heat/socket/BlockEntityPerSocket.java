package com.denfop.blockentity.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerSocket extends BlockEntityMainSocket {

    public BlockEntityPerSocket(BlockPos pos, BlockState state) {
        super(80000, BlockHeatReactorEntity.heat_per_socket, pos, state);
    }

    @Override
    public int getBlockLevel() {
        return 3;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_per_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
