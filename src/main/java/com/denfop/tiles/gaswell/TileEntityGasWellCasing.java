package com.denfop.tiles.gaswell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasWell;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityGasWellCasing extends TileEntityMultiBlockElement implements ICasing {
    public TileEntityGasWellCasing(){}
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasWell.gas_well_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_well;
    }

}
