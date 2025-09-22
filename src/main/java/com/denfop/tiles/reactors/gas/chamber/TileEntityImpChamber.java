package com.denfop.tiles.reactors.gas.chamber;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IChamber;

public class TileEntityImpChamber extends TileEntityMultiBlockElement implements IChamber {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.imp_gas_chamber;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }

    @Override
    public int getBlockLevel() {
        return 2;
    }

}
