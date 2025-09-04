package com.denfop.blockentity.reactors.heat.coolant;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleCoolant extends BlockEntityBaseCoolant {

    public BlockEntitySimpleCoolant(BlockPos pos, BlockState state) {
        super(0, 5000, BlockHeatReactorEntity.heat_coolant, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
