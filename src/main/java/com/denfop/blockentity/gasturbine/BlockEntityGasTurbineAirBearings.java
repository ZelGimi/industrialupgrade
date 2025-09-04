package com.denfop.blockentity.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityGasTurbineAirBearings extends BlockEntityMultiBlockElement implements IAirBearings {

    public BlockEntityGasTurbineAirBearings(BlockPos pos, BlockState state) {
        super(BlockGasTurbineEntity.gas_turbine_airbearings, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGasTurbineEntity.gas_turbine_airbearings;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine.getBlock(getTeBlock());
    }

}
