package com.denfop.blockentity.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGeothermalPumpEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGeothermalCasing extends BlockEntityMultiBlockElement implements ICasing {


    public BlockEntityGeothermalCasing(BlockPos pos, BlockState state) {
        super(BlockGeothermalPumpEntity.geothermal_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGeothermalPumpEntity.geothermal_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
