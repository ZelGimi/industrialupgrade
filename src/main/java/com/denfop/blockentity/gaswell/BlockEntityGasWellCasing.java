package com.denfop.blockentity.gaswell;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWellEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGasWellCasing extends BlockEntityMultiBlockElement implements ICasing {


    public BlockEntityGasWellCasing(BlockPos pos, BlockState state) {
        super(BlockGasWellEntity.gas_well_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasWellEntity.gas_well_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well.getBlock(getTeBlock());
    }

}
