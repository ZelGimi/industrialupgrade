package com.denfop.blockentity.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpSocket extends BlockEntityMainSocket {

    public BlockEntityImpSocket(BlockPos pos, BlockState state) {
        super(40000, BlockHeatReactorEntity.heat_imp_socket, pos, state);
    }

    @Override
    public int getBlockLevel() {
        return 2;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_imp_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
