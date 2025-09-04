package com.denfop.blockentity.windturbine;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWindTurbineEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityWindTurbineCasing2 extends BlockEntityMultiBlockElement implements ICasing2 {

    public BlockEntityWindTurbineCasing2(BlockPos pos, BlockState state) {
        super(BlockWindTurbineEntity.wind_turbine_casing_3, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockWindTurbineEntity.wind_turbine_casing_3;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine.getBlock(getTeBlock());
    }


}
