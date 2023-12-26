package com.denfop.tiles.reactors.gas.intercooler;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IInterCooler;

public class TileEntityPerInterCooler   extends TileEntityBaseInterCooler   {

    public TileEntityPerInterCooler() {
        super(3);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.per_gas_intercooler;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
