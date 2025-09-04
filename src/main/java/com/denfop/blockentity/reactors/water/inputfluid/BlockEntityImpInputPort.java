package com.denfop.blockentity.reactors.water.inputfluid;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpInputPort extends BlockEntityInputFluid {

    public BlockEntityImpInputPort(BlockPos pos, BlockState state) {
        super(BlockWaterReactorsEntity.water_imp_input, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_imp_input;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.water_reactors_component.getBlock(getTeBlock());
    }

    @Override
    public int getBlockLevel() {
        return 2;
    }

}
