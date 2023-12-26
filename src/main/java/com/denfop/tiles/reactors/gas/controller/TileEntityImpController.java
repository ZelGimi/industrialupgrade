package com.denfop.tiles.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityImpController   extends TileEntityMainController {

    public TileEntityImpController() {
        super(InitMultiBlockSystem.impGasReactorMultiBlock, EnumGasReactors.I);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.imp_gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
