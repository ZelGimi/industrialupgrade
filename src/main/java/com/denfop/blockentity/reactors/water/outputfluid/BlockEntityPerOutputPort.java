package com.denfop.blockentity.reactors.water.outputfluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.reactors.water.IOutput;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityPerOutputPort extends BlockEntityOutputFluid implements IOutput {

    public BlockEntityPerOutputPort(BlockPos pos, BlockState state) {
        super(BlockWaterReactorsEntity.water_per_output, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_per_output;
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
