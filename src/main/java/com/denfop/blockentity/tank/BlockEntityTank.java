package com.denfop.blockentity.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityLiquedTank;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTankEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityTank extends BlockEntityLiquedTank {

    public BlockEntityTank(BlockPos pos, BlockState state) {
        super(40, BlockTankEntity.tank_iu, pos, state);
    }


    public MultiBlockEntity getTeBlock() {
        return BlockTankEntity.tank_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank.getBlock(getTeBlock().getId());
    }

}
