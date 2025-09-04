package com.denfop.blockentity.hydroturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHydroTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHydroTurbineCasing extends BlockEntityMultiBlockElement implements ICasing {

    public BlockEntityHydroTurbineCasing(BlockPos pos, BlockState state) {
        super(BlockHydroTurbineEntity.hydro_turbine_casing_1, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHydroTurbineEntity.hydro_turbine_casing_1;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine.getBlock(getTeBlock());
    }

}
