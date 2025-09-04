package com.denfop.blockentity.hydroturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHydroTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHydroTurbineCasing1 extends BlockEntityMultiBlockElement implements ICasing1 {

    public BlockEntityHydroTurbineCasing1(BlockPos pos, BlockState state) {
        super(BlockHydroTurbineEntity.hydro_turbine_casing_2, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHydroTurbineEntity.hydro_turbine_casing_2;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine.getBlock(getTeBlock());
    }


}
