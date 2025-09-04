package com.denfop.blockentity.reactors.water.levelfuel;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.reactors.water.ILevelFuel;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerLevelFuel extends BlockEntityMainLevelFuel implements ILevelFuel {

    public BlockEntityPerLevelFuel(BlockPos pos, BlockState state) {
        super(BlockWaterReactorsEntity.water_per_levelfuel, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_per_levelfuel;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 3;
    }

}
