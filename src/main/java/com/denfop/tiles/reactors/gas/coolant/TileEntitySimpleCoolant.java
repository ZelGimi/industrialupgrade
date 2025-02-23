package com.denfop.tiles.reactors.gas.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;

public class TileEntitySimpleCoolant extends TileEntityCoolant {

    public TileEntitySimpleCoolant() {
        super(0);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
