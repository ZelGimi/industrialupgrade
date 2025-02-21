package com.denfop.tiles.hydroturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.blocks.mechanism.BlockHydroTurbine;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityHydroTurbineStabilizer extends TileEntityMultiBlockElement implements IStabilizer {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHydroTurbine.hydro_turbine_stabilizer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine;
    }

}
