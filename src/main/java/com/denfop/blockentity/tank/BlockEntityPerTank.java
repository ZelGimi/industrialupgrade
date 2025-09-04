package com.denfop.blockentity.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityLiquedTank;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTankEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerTank extends BlockEntityLiquedTank {

    public BlockEntityPerTank(BlockPos pos, BlockState state) {
        super(2560, BlockTankEntity.per_tank, pos, state);
    }


    public MultiBlockEntity getTeBlock() {
        return BlockTankEntity.per_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank.getBlock(getTeBlock().getId());
    }

}
