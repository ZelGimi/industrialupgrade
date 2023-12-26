package com.denfop.tiles.reactors.graphite.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlocksGraphiteReactors;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.base.TileEntityBlock;

public class TileEntityPerController  extends TileEntityMainController {
    public TileEntityPerController(
    ) {
        super(InitMultiBlockSystem.perGraphiteReactorMultiBlock, EnumGraphiteReactors.P);
    }
    @Override
    public IMultiTileBlock getTeBlock() {
        return BlocksGraphiteReactors.graphite_per_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.graphite_reactor;
    }

}
