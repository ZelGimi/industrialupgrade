package com.denfop.blockentity.reactors.water.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.reactors.water.ITank;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleTank extends BlockEntityMainTank implements ITank {

    public BlockEntitySimpleTank(BlockPos pos, BlockState state) {
        super(10000, BlockWaterReactorsEntity.water_tank, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_tank;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 0;
    }

}
