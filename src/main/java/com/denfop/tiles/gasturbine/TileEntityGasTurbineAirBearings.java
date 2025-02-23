package com.denfop.tiles.gasturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityGasTurbineAirBearings extends TileEntityMultiBlockElement implements IAirBearings {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasTurbine.gas_turbine_airbearings;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gasTurbine;
    }

}
