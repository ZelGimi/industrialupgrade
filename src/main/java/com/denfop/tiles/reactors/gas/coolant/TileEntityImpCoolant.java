package com.denfop.tiles.reactors.gas.coolant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;

public class TileEntityImpCoolant extends TileEntityCoolant {

    public TileEntityImpCoolant() {
        super(2);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.imp_gas_coolant;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
