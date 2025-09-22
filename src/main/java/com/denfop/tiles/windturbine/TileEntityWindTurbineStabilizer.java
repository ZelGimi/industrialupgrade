package com.denfop.tiles.windturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockWindTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityWindTurbineStabilizer extends TileEntityMultiBlockElement implements IStabilizer {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockWindTurbine.wind_turbine_stabilizer;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.windTurbine;
    }

}
