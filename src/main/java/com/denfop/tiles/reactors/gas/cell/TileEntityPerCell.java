package com.denfop.tiles.reactors.gas.cell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.reactors.gas.ICell;

public class TileEntityPerCell extends TileEntityMainTank implements ICell {

    public TileEntityPerCell() {
        super(240000);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.per_gas_cell;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }

    @Override
    public int getBlockLevel() {
        return 3;
    }

}
