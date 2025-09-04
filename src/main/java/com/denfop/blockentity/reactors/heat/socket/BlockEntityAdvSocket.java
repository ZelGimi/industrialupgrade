package com.denfop.blockentity.reactors.heat.socket;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvSocket extends BlockEntityMainSocket {

    public BlockEntityAdvSocket(BlockPos pos, BlockState state) {
        super(20000, BlockHeatReactorEntity.heat_adv_socket, pos, state);
    }

    @Override
    public int getBlockLevel() {
        return 1;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_adv_socket;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
