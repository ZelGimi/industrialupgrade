package com.denfop.blockentity.hydroturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHydroTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityHydroTurbineStabilizer extends BlockEntityMultiBlockElement implements IStabilizer {

    public BlockEntityHydroTurbineStabilizer(BlockPos pos, BlockState state) {
        super(BlockHydroTurbineEntity.hydro_turbine_stabilizer, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockHydroTurbineEntity.hydro_turbine_stabilizer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine.getBlock(getTeBlock());
    }

}
