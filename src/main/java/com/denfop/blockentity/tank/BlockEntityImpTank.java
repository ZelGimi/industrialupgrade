package com.denfop.blockentity.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityLiquedTank;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTankEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpTank extends BlockEntityLiquedTank {

    public BlockEntityImpTank(BlockPos pos, BlockState state) {
        super(480, BlockTankEntity.imp_tank, pos, state);
    }


    public MultiBlockEntity getTeBlock() {
        return BlockTankEntity.imp_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank.getBlock(getTeBlock().getId());
    }

}
