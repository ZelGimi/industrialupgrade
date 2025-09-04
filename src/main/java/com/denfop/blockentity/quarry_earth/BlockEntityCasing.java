package com.denfop.blockentity.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarryEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityCasing extends BlockEntityMultiBlockElement implements ICasing {


    public BlockEntityCasing(BlockPos pos, BlockState state) {
        super(BlockEarthQuarryEntity.earth_casing, pos, state);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockEarthQuarryEntity.earth_casing;
    }

}
