package com.denfop.tiles.reactors.gas.regenerator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;

public class TileEntityImpRegenerator extends TileEntityRegenerator {

    public TileEntityImpRegenerator() {
        super(2, 1000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.imp_gas_regenerator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
