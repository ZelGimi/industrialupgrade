package com.denfop.tiles.reactors.gas.compressor;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;

public class TileEntitySimpleCompressor extends TileEntityBaseCompressor {

    public TileEntitySimpleCompressor() {
        super(0);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_compressor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
