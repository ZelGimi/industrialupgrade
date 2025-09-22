package com.denfop.tiles.hydroturbine;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHydroTurbine;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityHydroTurbineCasing extends TileEntityMultiBlockElement implements ICasing {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHydroTurbine.hydro_turbine_casing_1;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.hydroTurbine;
    }

}
