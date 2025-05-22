package com.denfop.tiles.reactors.gas.cell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.reactors.gas.ICell;

public class TileEntityImpCell extends TileEntityMainTank implements ICell {

    public TileEntityImpCell() {
        super(120000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.imp_gas_cell;
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
