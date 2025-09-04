package com.denfop.blockentity.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGasTurbineCasing extends BlockEntityMultiBlockElement implements ICasing {

    public BlockEntityGasTurbineCasing(BlockPos pos, BlockState state) {
        super(BlockGasTurbineEntity.gas_turbine_casing, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasTurbineEntity.gas_turbine_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

}
