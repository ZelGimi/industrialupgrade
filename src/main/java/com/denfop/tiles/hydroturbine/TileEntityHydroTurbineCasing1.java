package com.denfop.tiles.hydroturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHydroTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityHydroTurbineCasing1 extends TileEntityMultiBlockElement implements ICasing1 {

    public TileEntityHydroTurbineCasing1(BlockPos pos, BlockState state) {
        super(BlockHydroTurbine.hydro_turbine_casing_2, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHydroTurbine.hydro_turbine_casing_2;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine.getBlock(getTeBlock());
    }


}
