package com.denfop.tiles.reactors.heat.controller;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockHeatReactor;
import com.denfop.register.InitMultiBlockSystem;

public class TileEntityPerController extends TileEntityMainController {

    public TileEntityPerController() {
        super(InitMultiBlockSystem.perHeatReactorMultiBlock, EnumHeatReactors.P);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockHeatReactor.heat_per_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.heat_reactor;
    }

}
