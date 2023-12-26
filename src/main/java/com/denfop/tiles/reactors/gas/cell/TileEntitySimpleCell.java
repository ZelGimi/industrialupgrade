package com.denfop.tiles.reactors.gas.cell;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.gas.ICell;

public class TileEntitySimpleCell  extends TileEntityMainTank implements ICell {
    public TileEntitySimpleCell() {
        super(30000);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_cell;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
