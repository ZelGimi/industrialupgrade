package com.denfop.tiles.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntitySimpleController extends TileEntityMainController {

    public TileEntitySimpleController() {
        super(InitMultiBlockSystem.GasReactorMultiBlock, EnumGasReactors.S);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
