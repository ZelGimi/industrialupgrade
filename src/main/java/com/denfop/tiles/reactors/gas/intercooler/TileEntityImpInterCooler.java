package com.denfop.tiles.reactors.gas.intercooler;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.IInterCooler;

public class TileEntityImpInterCooler   extends TileEntityBaseInterCooler   {

    public TileEntityImpInterCooler() {
        super(2);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.imp_gas_intercooler;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
