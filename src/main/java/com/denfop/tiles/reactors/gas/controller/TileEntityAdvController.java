package com.denfop.tiles.reactors.gas.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGasReactor;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntityAdvController extends TileEntityMainController {

    public TileEntityAdvController() {
        super(InitMultiBlockSystem.advGasReactorMultiBlock, EnumGasReactors.A);
    }


    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGasReactor.adv_gas_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.gas_reactor;
    }


}
