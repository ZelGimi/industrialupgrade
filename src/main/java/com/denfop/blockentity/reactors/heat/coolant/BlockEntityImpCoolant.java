package com.denfop.blockentity.reactors.heat.coolant;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpCoolant extends BlockEntityBaseCoolant {

    public BlockEntityImpCoolant(BlockPos pos, BlockState state) {
        super(2, 15000, BlockHeatReactorEntity.heat_imp_coolant, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_imp_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
