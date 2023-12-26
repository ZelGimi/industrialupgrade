package com.denfop.tiles.reactors.gas.compressor;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.ICompressor;

public class TileEntityPerCompressor   extends TileEntityBaseCompressor   {
    public TileEntityPerCompressor() {
        super(3);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.per_gas_compressor;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
