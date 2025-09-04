package com.denfop.blockentity.reactors.water.casing;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.ICasing;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWaterReactorsEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntitySimpleCasing extends BlockEntityMultiBlockElement implements ICasing {


    public BlockEntitySimpleCasing(BlockPos pos, BlockState state) {
        super(BlockWaterReactorsEntity.water_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWaterReactorsEntity.water_casing;
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
