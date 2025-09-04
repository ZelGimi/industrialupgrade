package com.denfop.blockentity.reactors.heat.fueltank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpFuelTank extends BlockEntityMainTank {

    public BlockEntityImpFuelTank(BlockPos pos, BlockState state) {
        super(80000, BlockHeatReactorEntity.heat_imp_fueltank, pos, state);
    }

    @Override
    public int getBlockLevel() {
        return 2;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHeatReactorEntity.heat_imp_fueltank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor.getBlock(getTeBlock());
    }

}
