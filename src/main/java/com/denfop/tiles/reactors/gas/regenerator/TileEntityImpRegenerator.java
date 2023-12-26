package com.denfop.tiles.reactors.gas.regenerator;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IRegenerator;

public class TileEntityImpRegenerator   extends TileEntityRegenerator   {
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
