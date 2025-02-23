package com.denfop.tiles.reactors.graphite.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntitySimpleController extends TileEntityMainController {

    public TileEntitySimpleController(
    ) {
        super(InitMultiBlockSystem.GraphiteReactorMultiBlock, EnumGraphiteReactors.S);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
