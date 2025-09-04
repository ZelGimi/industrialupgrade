package com.denfop.blockentity.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityLiquedTank;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockTankEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAdvTank extends BlockEntityLiquedTank {

    public BlockEntityAdvTank(BlockPos pos, BlockState state) {
        super(160, BlockTankEntity.adv_tank, pos, state);
    }


    public MultiBlockEntity getTeBlock() {
        return BlockTankEntity.adv_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.tank.getBlock(getTeBlock().getId());
    }

}
